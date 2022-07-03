package sk.adonikeoffice.epicchat.util;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

public class Util {

	@Getter
	private static final Util instance = new Util();

	@Getter
	@Setter
	private int lastMessageTime = 0;

	public static void sendType(final Player player, final String message) {
		final String actionBarType = MessageType.ACTIONBAR.getType();
		final String chatType = MessageType.CHAT.getType();

		if (message.startsWith(actionBarType))
			Remain.sendActionBar(player, message.replace(actionBarType, ""));
		else if (message.startsWith(chatType))
			Common.tell(player, message.replace(chatType, ""));
		else
			Common.log("Unknown message type. Available: " + actionBarType + ", " + chatType);
	}

	public static boolean hasPermission(final Player player, final String permission) {
		if (permission.equals("none"))
			return true;

		return player.hasPermission(permission) || player.isOp();
	}

	@RequiredArgsConstructor
	private enum MessageType {
		ACTIONBAR("{actionbar}"), CHAT("{chat}");

		@Getter
		private final String type;
	}

}