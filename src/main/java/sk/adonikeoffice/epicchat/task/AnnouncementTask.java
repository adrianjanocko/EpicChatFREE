package sk.adonikeoffice.epicchat.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.data.AnnouncementData;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.util.Util;

import java.util.List;

public class AnnouncementTask extends BukkitRunnable {

	@Override
	public void run() {
		final AnnouncementData announcement = RandomUtil.nextItem(Settings.Chat.Announcement.ANNOUNCEMENTS);

		for (final Player player : Remain.getOnlinePlayers()) {
			if (!player.isOnline())
				return;

			final boolean has = Util.hasPermission(player, announcement.getPermission());

			if (has) {
				final List<String> chatMessage = announcement.getChatMessage();

				for (String message : chatMessage) {
					if (message.startsWith("<center>")) {
						message = message.replace("<center>", "");

						message = ChatUtil.center(message);
					}

					Common.tellNoPrefix(player, message);
				}

				announcement.getSound().play(player);
			}
		}
	}

}
