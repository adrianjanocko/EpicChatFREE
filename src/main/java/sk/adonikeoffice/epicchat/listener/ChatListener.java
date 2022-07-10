package sk.adonikeoffice.epicchat.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompChatColor;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.settings.GroupData;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.util.Util;

import java.util.List;

public final class ChatListener implements Listener {

	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {
		event.setCancelled(true);

		final Player player = event.getPlayer();
		String message = event.getMessage();

		// FULLY FUNCTIONAL
		timeCheck:
		{
			if (Settings.Chat.Cooldown.ENABLED) {
				if (Util.hasPermission(player, Settings.Chat.Cooldown.PERMISSION))
					break timeCheck;

				final long now = System.currentTimeMillis() / 1000;
				final int lastMessageTime = Util.getInstance().getLastMessageTime();
				final int messageDelay = Settings.Chat.Cooldown.DELAY;

				if ((now - lastMessageTime) < messageDelay) {
					final long time = messageDelay - (now - lastMessageTime);

					final String replacedMessage = Replacer.replaceArray(
							Settings.Chat.Cooldown.MESSAGE,
							"time", time,
							"time_plural", Common.plural(time, "second")
					);

					Util.sendType(player, replacedMessage);
					return;
				}

				Util.getInstance().setLastMessageTime(Math.toIntExact(now));
			}
		}

		// FULLY FUNCTIONAL
		if (Settings.Chat.Mention.ENABLED)
			for (final Player target : Remain.getOnlinePlayers()) {
				final String targetName = target.getName();
				final int thisIndex = message.indexOf(targetName);

				if (thisIndex != -1) {
					CompChatColor messageColor = Settings.Chat.MESSAGE_COLOR;

					if (HookManager.isVaultLoaded())
						for (final GroupData group : Settings.Chat.GROUP_FORMAT)
							if (HookManager.getPlayerPermissionGroup(player).equals(group.getName()))
								messageColor = group.getMessageColor();

					message = message.replace(targetName, Settings.Chat.Mention.COLOR + "@" + targetName + (messageColor != null ? messageColor : "&f"));

					Util.sendType(target, Settings.Chat.Mention.MESSAGE.replace("{target_name}", player.getName()));
					Settings.Chat.Mention.SOUND.play(target);
				}
			}

		if (!Settings.Chat.PERMISSION.equals("none")) {
			final boolean hasAccess = Util.hasPermission(player, Settings.Chat.PERMISSION);

			if (hasAccess)
				this.chat(player, message);
			else
				Common.tell(player, Settings.Message.PERMISSION_MESSAGE);
		} else
			this.chat(player, message);
	}

	private void chat(final Player player, final String message) {
		String format = Variables.replace(Settings.Chat.FORMAT, player);
		CompChatColor messageColor = Settings.Chat.MESSAGE_COLOR;

		if (HookManager.isVaultLoaded())
			for (final GroupData group : Settings.Chat.GROUP_FORMAT)
				if (HookManager.getPlayerPermissionGroup(player).equals(group.getName())) {
					format = group.getFormat() != null ? Variables.replace(group.getFormat(), player) : format;
					messageColor = group.getMessageColor() != null ? group.getMessageColor() : messageColor;
				}

		final boolean hasColorPermission = Util.hasPermission(player, Settings.Chat.PERMISSION_COLOR);
		final String replacedFormat = Replacer.replaceArray(format, "message", hasColorPermission ? messageColor + message : messageColor + Common.stripColors(message));

		this.sendMessage(player, replacedFormat);
	}

	private void sendMessage(final Player player, final String formattedMessage) {
		final SimpleComponent chatComponent = SimpleComponent.of(formattedMessage);

		List<String> hoverMessages = Settings.Chat.HOVER;
//		hoverMessages.replaceAll(string -> Variables.replace(string, player));
		hoverMessages = PlaceholderAPI.setPlaceholders(player, hoverMessages);

		chatComponent.onHover(hoverMessages);

		for (final Player online : Remain.getOnlinePlayers())
			chatComponent.send(online);

		if (Settings.Chat.LOG_ENABLED)
			Common.log(formattedMessage);
	}

}
