package sk.adonikeoffice.epicchat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.model.Variables;
import sk.adonikeoffice.epicchat.settings.Settings;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {
		event.setCancelled(true);

		final Player player = event.getPlayer();
		final String message = event.getMessage();

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
