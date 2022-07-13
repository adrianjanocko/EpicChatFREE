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

import static sk.adonikeoffice.epicchat.settings.Settings.Chat.Question;

public final class QuestionTask extends BukkitRunnable {
	@Getter
	public static QuestionData question = null;

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
					Remain.sendActionBar(player, question.getQuestion());

			if ((getTimeTicks() - TimeUtil.currentTimeTicks()) <= -Question.INACTIVE_CANCEL.getTimeTicks()) {
				if (questionIsRunning())
					Common.broadcast(Settings.Message.Question.INACTIVE_CANCEL);

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
