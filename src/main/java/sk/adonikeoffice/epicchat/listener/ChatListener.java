package sk.adonikeoffice.epicchat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompChatColor;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.data.EmojiData;
import sk.adonikeoffice.epicchat.data.GroupData;
import sk.adonikeoffice.epicchat.data.PlayerData;
import sk.adonikeoffice.epicchat.data.QuestionData;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.task.QuestionTask;
import sk.adonikeoffice.epicchat.util.Util;

import java.util.List;

import static sk.adonikeoffice.epicchat.settings.Settings.Chat;
import static sk.adonikeoffice.epicchat.settings.Settings.Chat.*;

public final class ChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (!PlayerData.isLoaded(player))
            PlayerData.createPlayer(player.getName());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChat(final AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        final Player player = event.getPlayer();
        String message = event.getMessage();

        if (Cooldown.ENABLED) {
            timeCheck:
            {
                if (Util.hasPermission(player, Cooldown.PERMISSION))
                    break timeCheck;

                final long now = System.currentTimeMillis() / 1000;
                final int lastMessageTime = Util.getInstance().getLastMessageTime();
                final int messageDelay = Cooldown.DELAY;

                if ((now - lastMessageTime) < messageDelay) {
                    final long time = messageDelay - (now - lastMessageTime);

                    final String replacedMessage = Replacer.replaceArray(
                            Cooldown.MESSAGE,
                            "time", time,
                            "time_plural", Common.plural(time, "second")
                    );

                    Util.sendType(player, replacedMessage, false);
                    return;
                }

                Util.getInstance().setLastMessageTime(Math.toIntExact(now));
            }
        }

        if (Util.canChat(player)) {
            if (Chat.Question.ENABLED) {
                final QuestionData question = QuestionTask.getQuestion();

                if (QuestionTask.questionIsRunning() && message.toLowerCase().contains(question.getAnswer().toLowerCase())) {
                    final String replacedMessage = Replacer.replaceArray(Settings.Message.Question.GUESSED,
                            "0", player.getName(),
                            "1", question.getAnswer()
                    );

                    Common.runLater(2, () -> {
                        Common.broadcast(replacedMessage);

                        PlayerData.findPlayer(player).increaseReactedTimes();

                        for (final String reward : Question.REWARDS)
                            Common.dispatchCommand(player, reward);

                        final CompSound sound = question.getSound() != null ? question.getSound() : null;

                        if (sound != null)
                            sound.play(player);
                    });

                    QuestionTask.stopQuestion();

                    return;
                }
            }

            if (Mention.ENABLED)
                for (final Player target : Remain.getOnlinePlayers()) {
                    final String targetName = target.getName();
                    final int thisIndex = message.indexOf(targetName);

                    if (thisIndex != -1) {
                        CompChatColor messageColor = Chat.MESSAGE_COLOR;

                        if (HookManager.isVaultLoaded())
                            for (final GroupData group : Chat.GROUP_FORMAT)
                                if (HookManager.getPlayerPermissionGroup(player).equals(group.getName()))
                                    messageColor = group.getMessageColor();

                        final String mentionColor = Mention.COLOR;

                        if (mentionColor.startsWith("&") || mentionColor.startsWith("#") || mentionColor.startsWith("{#"))
                            message = message.replace(targetName, Mention.COLOR + targetName + (messageColor != null ? messageColor : "&f"));
                        else
                            Common.log("Mention Color in the settings.yml must start with a color. (&, #, {#)");

                        Util.sendType(target, Mention.MESSAGE.replace("{target_name}", player.getName()), false);
                        Mention.SOUND.play(target);
                    }
                }

            if (Emoji.ENABLED)
                for (final EmojiData emoji : Emoji.EMOJIS) {
                    final String emojiToReplace = emoji.getWhatToReplace();
                    final int thisIndex = message.indexOf(emojiToReplace);

                    if (thisIndex != -1) {
                        CompChatColor messageColor = Chat.MESSAGE_COLOR;

                        if (HookManager.isVaultLoaded())
                            for (final GroupData group : Chat.GROUP_FORMAT)
                                if (HookManager.getPlayerPermissionGroup(player).equals(group.getName()))
                                    messageColor = group.getMessageColor();

                        message = message.replace(emojiToReplace, Emoji.COLOR + emoji.getReplaceTo() + (messageColor != null ? messageColor : "&f"));
                    }
                }

            this.chat(player, message);
        }
    }

    private void chat(final Player player, final String message) {
        String format = Variables.replace(Chat.FORMAT, player);
        CompChatColor messageColor = Chat.MESSAGE_COLOR;

        if (HookManager.isVaultLoaded())
            for (final GroupData group : Chat.GROUP_FORMAT)
                if (HookManager.getPlayerPermissionGroup(player).equals(group.getName())) {
                    format = group.getFormat() != null ? Variables.replace(group.getFormat(), player) : format;
                    messageColor = group.getMessageColor() != null ? group.getMessageColor() : messageColor;
                }

        final boolean hasColorPermission = Util.hasPermission(player, Chat.PERMISSION_COLOR);
        final String formattedMessage = Replacer.replaceArray(format, "message", hasColorPermission ? messageColor + message : messageColor + Common.stripColors(message));

        this.sendMessage(player, formattedMessage);
    }

    private void sendMessage(final Player player, final String message) {
        final SimpleComponent chatComponent = SimpleComponent.of(message);

        List<String> hoverMessages = Chat.HOVER;
        hoverMessages = Util.replaceVariables(player, hoverMessages);

        chatComponent.onHover(hoverMessages);
        chatComponent.onClickSuggestCmd(HOVER_CLICK_COMMAND.replace("{0}", player.getName()));

        for (final Player online : Remain.getOnlinePlayers())
            chatComponent.send(online);

        if (Chat.LOG_ENABLED)
            Common.log(message);
    }

}