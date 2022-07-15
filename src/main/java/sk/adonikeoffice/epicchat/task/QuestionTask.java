package sk.adonikeoffice.epicchat.task;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.data.QuestionData;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.util.Util;

import static sk.adonikeoffice.epicchat.settings.Settings.Chat.Question;

public final class QuestionTask extends BukkitRunnable {
	
	@Getter
	public static QuestionData question = null;

	@Getter
	public static boolean breakCycle = false;

	@Getter
	public static final long timeTicks = TimeUtil.currentTimeTicks();

	@Override
	public void run() {
		for (final Player player : Remain.getOnlinePlayers())
			if (!player.isOnline())
				return;

		if (questionIsRunning())
			return;

		question = RandomUtil.nextItem(Question.QUESTIONS);

		Common.runTimerAsync(20, () -> {
			for (final Player player : Remain.getOnlinePlayers())
				if (questionIsRunning())
					if (breakCycle)
						return;
					else
						Util.sendType(player, question.getQuestion(), true);

			if ((getTimeTicks() - TimeUtil.currentTimeTicks()) <= -Question.INACTIVE_CANCEL.getTimeTicks()) {
				if (questionIsRunning())
					for (final Player player : Remain.getOnlinePlayers())
						Common.tell(player, Settings.Message.Question.INACTIVE_CANCEL);

				stopQuestion();
			}
		});
	}

	public static boolean questionIsRunning() {
		return question != null;
	}

	public static void stopQuestion() {
		question = null;
	}

}
