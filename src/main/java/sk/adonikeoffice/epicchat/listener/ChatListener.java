package sk.adonikeoffice.epicchat.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import me.leoko.advancedban.manager.PunishmentManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
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
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.EpicChatPlugin;
import sk.adonikeoffice.epicchat.data.EmojiData;
import sk.adonikeoffice.epicchat.data.GroupData;
import sk.adonikeoffice.epicchat.data.PlayerData;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.task.QuestionTask;
import sk.adonikeoffice.epicchat.util.Util;

import java.util.List;

import static sk.adonikeoffice.epicchat.settings.Settings.Chat;
import static sk.adonikeoffice.epicchat.settings.Settings.Chat.*;
import static sk.adonikeoffice.epicchat.settings.Settings.Message;

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

		// FULLY FUNCTIONAL
		timeCheck:
		{
			if (Cooldown.ENABLED) {
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

					Util.sendType(player, replacedMessage);
					return;
				}

				Util.getInstance().setLastMessageTime(Math.toIntExact(now));
			}
		}

		if (!HookManager.isLogged(player)) {
			Common.tell(player, Message.NOT_LOGGED);

			return;
		}

		if (isMuted(player)) {
			Common.tell(player, Message.MUTED);

			return;
		}

		if (!Chat.PERMISSION.equals("none") && !Util.hasPermission(player, Chat.PERMISSION)) {
			Common.tell(player, Message.NO_PERMISSION.replace("{0}", Chat.PERMISSION));

			return;
		}

		// FULLY FUNCTIONAL
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

					message = message.replace(targetName, Mention.COLOR + "@" + targetName + (messageColor != null ? messageColor : "&f"));

					Util.sendType(target, Mention.MESSAGE.replace("{target_name}", player.getName()));
					Mention.SOUND.play(target);
				}
			}

		if (Chat.Question.ENABLED) {
			if (QuestionTask.questionIsRunning() && message.toLowerCase().contains(QuestionTask.getQuestion().getAnswer().toLowerCase())) {
				final String replacedMessage = Replacer.replaceArray(Settings.Message.Question.GUESSED,
						"0", player.getName(),
						"1", QuestionTask.getQuestion().getAnswer()
				);

				Common.runLater(1, () -> {
					Common.broadcast(replacedMessage);

					PlayerData.findPlayer(player).increaseReactedTimes();
				});

				QuestionTask.stopQuestion();
			}
		}

		for (final EmojiData emoji : EMOJIS) {
			final String emojiToReplace = emoji.getWhatToReplace();
			final int thisIndex = message.indexOf(emojiToReplace);

			if (thisIndex != -1)
				message = message.replace(emojiToReplace, emoji.getReplaceTo());
		}

		this.chat(player, message);
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
		this.sendDiscordMessage(player, message);
	}

	private void sendMessage(final Player player, final String message) {
		final SimpleComponent chatComponent = SimpleComponent.of(message);

		List<String> hoverMessages = Chat.HOVER;
		hoverMessages = PlaceholderAPI.setPlaceholders(player, hoverMessages);

		chatComponent.onHover(hoverMessages);

		for (final Player online : Remain.getOnlinePlayers())
			chatComponent.send(online);

		if (Chat.LOG_ENABLED)
			Common.log(message);
	}

	private void sendDiscordMessage(final Player player, final String message) {
		if (Discord.isEnabled()) {
			final JDA jda = EpicChatPlugin.getJda();
			final TextChannel channel = jda.getTextChannelById(Discord.CHAT_CHANNEL_ID);

			if (channel != null) {
				final String formattedMessage = Replacer.replaceArray(Variables.replace(Discord.DISCORD_FORMAT, player),
						"message", Common.stripColors(message)
				);

				channel.sendMessage(formattedMessage).queue();
			}
		}
	}

	private boolean isMuted(final Player player) {
		return HookManager.isMuted(player) || Common.doesPluginExist("AdvancedBan") && PunishmentManager.get().isMuted(player.getUniqueId().toString());
	}

}
