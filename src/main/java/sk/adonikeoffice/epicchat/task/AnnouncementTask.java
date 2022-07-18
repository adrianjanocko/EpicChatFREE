package sk.adonikeoffice.epicchat.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.data.AnnouncementData;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.util.Util;

import java.util.List;

public class AnnouncementTask extends BukkitRunnable {

	private int time;

	@Override
	public void run() {

		if (this.time == Settings.Chat.Announcement.REPEAT_EVERY.getTimeSeconds()) {
			final AnnouncementData announcement = RandomUtil.nextItem(Settings.Chat.Announcement.ANNOUNCEMENTS);

			for (final Player player : Remain.getOnlinePlayers()) {
				final boolean has = Util.hasPermission(player, announcement.getPermission());

				if (has) {
					final List<String> chatMessages = announcement.getChatMessage();

					for (String chatMessage : chatMessages) {
						if (chatMessage.startsWith("<center>")) {
							chatMessage = chatMessage.replace("<center>", "");

							chatMessage = ChatUtil.center(chatMessage);
						}

						Common.tellNoPrefix(player, chatMessage);
					}

					final CompSound sound = announcement.getSound() != null ? announcement.getSound() : null;

					if (sound != null)
						sound.play(player);

					time = 0;
				}
			}

		}

		for (final Player player : Remain.getOnlinePlayers())
			if (player.isOnline())
				time++;

	}

}