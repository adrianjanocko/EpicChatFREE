package sk.adonikeoffice.epicchat.util;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.type.MessageType;

public class Chat {

	@Getter
	private static final Chat instance = new Chat();

	@Getter
	@Setter
	private int lastMessageTime = 0;

	public static void sendType(final Player player, final String message) {
		if (message.startsWith(MessageType.ACTIONBAR.getType()))
			Remain.sendActionBar(player, message);
		else if (message.startsWith(MessageType.CHAT.getType()))
			Common.tell(player, message);
		else if (message.startsWith(MessageType.BOSSBAR.getType()))
			Remain.sendBossbarPercent(player, message, 100);
	}

	public static boolean hasPermission(final Player player, final String permission) {
		return player.hasPermission(permission) || player.isOp();
	}
}