package sk.adonikeoffice.epicchat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.settings.Settings;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {
		event.setCancelled(true);

		final Player player = event.getPlayer();
		String message = event.getMessage();

		if (Settings.Chat.Mention.ENABLED)
			for (final Player target : Remain.getOnlinePlayers()) {
				final String targetName = target.getName();
				final int thisIndex = message.indexOf(targetName);

				if (thisIndex != -1) {
					final String firstPart = message.substring(0, thisIndex);
					final String lastColor = Common.lastColor("&f" + firstPart);

					message = message.replace(targetName, Settings.Chat.Mention.COLOR + "@" + targetName + (lastColor != null ? lastColor : "&f"));

					Remain.sendActionBar(target, Settings.PLUGIN_PREFIX + Settings.Message.MENTION);
					CompSound.LEVEL_UP.play(target);
				}
			}

		if (Settings.Chat.AntiSwear.ENABLED)
			for (final String word : Settings.Chat.AntiSwear.WORDS) {
				final String replacedWord = ChatUtil.replaceDiacritic(word);

				if (replacedWord.contains(message)) {
					Messenger.success(player, "cs");

					return;
				}
			}

		if (Settings.Chat.PERMISSION_ENABLED) {
			final boolean hasAccess = player.hasPermission(Settings.Chat.PERMISSION) || player.isOp();

			if (hasAccess)
				chat(player, message);
			else
				Messenger.success(player, Settings.Message.PERMISSION_MESSAGE);
		} else
			chat(player, message);
	}

	public static void chat(final Player player, final String message) {
		final String format = Variables.replace(Settings.Chat.FORMAT, player);

		final boolean hasColorPermission = player.hasPermission(Settings.Chat.PERMISSION_COLOR) || player.isOp();
		final String replacedFormat = Replacer.replaceArray(format, "message", hasColorPermission ? message : Common.stripColors(message));

		Common.broadcast(replacedFormat);
	}

}
