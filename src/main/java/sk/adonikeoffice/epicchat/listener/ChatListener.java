package sk.adonikeoffice.epicchat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.Remain;
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
					final String firstPart = message.substring(0, thisIndex);
					final String lastColor = Common.lastColor(Settings.Chat.MESSAGE_COLOR + firstPart);

					message = message.replace(targetName, Settings.Chat.Mention.COLOR + "@" + targetName + (lastColor != null ? lastColor : Settings.Chat.MESSAGE_COLOR));

					Util.sendType(player, Settings.Chat.Mention.MESSAGE.replace("{0}", player.getName()));
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
		final String format = Variables.replace(Settings.Chat.FORMAT, player);

		final boolean hasColorPermission = Util.hasPermission(player, Settings.Chat.PERMISSION_COLOR);
		final String replacedFormat = Replacer.replaceArray(format, "message", hasColorPermission ? Settings.Chat.MESSAGE_COLOR + message : Settings.Chat.MESSAGE_COLOR + Common.stripColors(message));

		this.sendMessage(player, replacedFormat);
	}

	private void sendMessage(final Player player, final String message) {
		final SimpleComponent chatComponent = SimpleComponent.of(message);

		final List<String> hoverMessages = Settings.Chat.HOVER;
		hoverMessages.replaceAll(string -> Variables.replace(string, player));

		chatComponent.onHover(hoverMessages);

		for (final Player online : Remain.getOnlinePlayers())
			chatComponent.send(online);

		if (Settings.Chat.LOG_ENABLED)
			Common.log(message);
	}

}
