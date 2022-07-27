package sk.adonikeoffice.epicchat.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import sk.adonikeoffice.epicchat.settings.Settings;

public class ActivityTask extends BukkitRunnable {

	private int time;

	@Override
	public void run() {

		Common.log(" " + Settings.Chat.Discord.Activity.TYPE);
		Common.log(" " + Settings.Chat.Discord.Activity.MESSAGE);
		Common.log(" " + Settings.Chat.Discord.Activity.REPEAT_EVERY);

//		if (this.time == Settings.Chat.Discord.Activity.REPEAT_EVERY.getTimeSeconds()) {
//			final JDA jda = EpicChatPlugin.getJda();
//
//			if (jda != null)
//				jda.getPresence().setActivity(Activity.of(Settings.Chat.Discord.Activity.TYPE,
//						Replacer.replaceArray(
//								Settings.Chat.Discord.Activity.MESSAGE,
//								"online", Remain.getOnlinePlayers().size(),
//								"max", Bukkit.getMaxPlayers(),
//								"tps", Remain.getTPS(),
//								"server_name", Remain.getServerName(),
//								"timestamp_short", TimeUtil.getFormattedDateShort()
//						)));
//
//			this.time = 0;
//		}

		this.time++;

	}

}
