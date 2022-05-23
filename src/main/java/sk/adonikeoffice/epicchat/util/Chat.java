package sk.adonikeoffice.epicchat.util;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class Chat {

	@Getter
	private static final Chat instance = new Chat();

	@Getter
	@Setter
	private int lastMessageTime = 0;

	public static boolean hasPermission(final Player player, final String permission) {
		return player.hasPermission(permission) || player.isOp();
	}
}