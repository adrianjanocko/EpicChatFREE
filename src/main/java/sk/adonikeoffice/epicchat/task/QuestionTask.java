package sk.adonikeoffice.epicchat.task;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.data.QuestionData;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.util.Util;

public class QuestionTask extends BukkitRunnable {

	@Getter
	public static QuestionData question = null;

	@Getter
	public static boolean breakCycle = false;

	private static int questionTime;

	private static int inactiveTime;

	@Override
	public void run() {
		if (questionTime == Settings.Chat.Question.REPEAT_EVERY.getTimeSeconds()) {
			question = RandomUtil.nextItem(Settings.Chat.Question.QUESTIONS);

			for (final Player player : Remain.getOnlinePlayers())
				Util.sendType(player, question.getQuestion(), true);
		}

		if (questionIsRunning())
			inactiveTime++;

		if (inactiveTime >= Settings.Chat.Question.INACTIVE_CANCEL.getTimeSeconds())
			for (final Player player : Remain.getOnlinePlayers())
				stopQuestion(player);

		for (final Player player : Remain.getOnlinePlayers())
			if (player.isOnline())
				questionTime++;
	}

	public static boolean questionIsRunning() {
		return question != null;
	}

	public static void stopQuestion() {
		question = null;

		questionTime = 0;
		inactiveTime = 0;
	}

	public static void stopQuestion(final Player player) {
		stopQuestion();

		Common.tell(player, Settings.Message.Question.INACTIVE_CANCEL);
	}

}