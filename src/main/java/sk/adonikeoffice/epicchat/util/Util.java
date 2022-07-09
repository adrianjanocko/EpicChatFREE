package sk.adonikeoffice.epicchat.util;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.settings.Settings;

public class Util {

	@Getter
	private static final Util instance = new Util();

	@Getter
	@Setter
	private int lastMessageTime = 0;

	public static void sendType(final Player player, String message) {
		message = Variables.replace(message, player);

		final String actionBarType = MessageType.ACTIONBAR.getType();
		final String chatType = MessageType.CHAT.getType();
		final String titleType = MessageType.TITLE.getType();

		if (message.startsWith(actionBarType))
			Remain.sendActionBar(player, Settings.PLUGIN_PREFIX + message.replace(actionBarType, ""));
		else if (message.startsWith(chatType))
			Common.tellLater(5, player, message.replace(chatType, ""));
		else if (message.startsWith(titleType)) {
			final String[] split = message.split("\\|");

			if (split.length == 2)
				Remain.sendTitle(player, split[0].replace(titleType, ""), split[1]);
			else
				Common.log("Invalid title message: " + message + ". Valid: " + titleType + "<title>|<subtitle>");
		} else
			Common.log("Unknown message type. Available: " + actionBarType + ", " + chatType);
	}

	public static boolean hasPermission(final Player player, final String permission) {
		if (permission.equals("none"))
			return true;

		return player.hasPermission(permission) || player.isOp();
	}

	@RequiredArgsConstructor
	private enum MessageType {
		ACTIONBAR("{actionbar}"), CHAT("{chat}"), TITLE("{title}");

		@Getter
		private final String type;
	}

}