package sk.adonikeoffice.epicchat;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import sk.adonikeoffice.epicchat.command.ReloadCommand;
import sk.adonikeoffice.epicchat.data.PlayerData;
import sk.adonikeoffice.epicchat.listener.ChatListener;
import sk.adonikeoffice.epicchat.listener.DiscordListener;
import sk.adonikeoffice.epicchat.task.ActivityTask;
import sk.adonikeoffice.epicchat.task.AnnouncementTask;
import sk.adonikeoffice.epicchat.task.QuestionTask;

import javax.security.auth.login.LoginException;

import static sk.adonikeoffice.epicchat.settings.Settings.Chat;
import static sk.adonikeoffice.epicchat.settings.Settings.Chat.Discord;

public class EpicChatPlugin extends SimplePlugin {

	//
	// TODO: 1.
	// Test added Activity_Type and Activity_Message,
	//

	@Getter
	public static JDA jda;

	@Override
	protected void onPluginStart() {
		Messenger.ENABLED = false;
		Common.setLogPrefix("EpicChat |");

		Common.log(
				Common.consoleLine(),
				getNamed() + " " + getVersion() + " by " + getAuthor(),
				" ",
				"If you found a bug or you have an idea, please,",
				"post it on the GitHub in the Issues section:",
				"https://github.com/AdoNikeOFFICE/EpicChat/issues",
				" ",
				"Or, you can join our Discord:",
				"discord.epic-central.eu",
				Common.consoleLine()
		);

		if (Discord.isEnabled() && jda == null) {
			final String token = Discord.TOKEN;

			if (!token.equals("BOT_TOKEN")) {
				Common.log("[+] Discord Bot has been enabled.");

				try {
					jda = JDABuilder.createDefault(token)
							.addEventListeners(new DiscordListener())
							.build();
				} catch (final LoginException e) {
					e.printStackTrace();
				}
			} else
				Common.log("[!] The Discord Token is null or empty.");
		}

		if (!HookManager.isPlaceholderAPILoaded())
			Common.log(
					Common.consoleLine(),
					"** INFO **",
					" ",
					"You can install PlaceholderAPI,",
					"if you want to use placeholders from it.",
					" ",
					"Ignore this message, if you don't want to.",
					Common.consoleLine()
			);

		if (!HookManager.isVaultLoaded())
			Common.log(
					Common.consoleLine(),
					"** INFO **",
					" ",
					"You can install Vault and Permission plugin,",
					"if you want to use 'Group_Format' feature.",
					" ",
					"Ignore this message, if you don't want to.",
					Common.consoleLine()
			);
	}

	@Override
	protected void onReloadablesStart() {
		PlayerData.loadPlayers();

		this.registerCommand(new ReloadCommand());

		if (Chat.ENABLED)
			this.registerEvents(new ChatListener());

		if (Chat.Question.ENABLED) {
			Common.runTimerAsync(20, new QuestionTask());

			HookManager.addPlaceholder("question_answers", (target) -> String.valueOf(PlayerData.findPlayer(target).getReactedTimes()));
		}

		if (Chat.Announcement.ENABLED)
			Common.runTimerAsync(20, new AnnouncementTask());

		if (Discord.isEnabled())
			Common.runTimerAsync(20, new ActivityTask());
	}

	@Override
	protected void onPluginStop() {
		if (jda != null)
			jda.shutdownNow();
	}

	public static String getAuthor() {
		return "AdoNikeOFFICE";
	}

	@Override
	public int getFoundedYear() {
		return 2022; // 10.04.2022
	}

	/**
	 * <a href="https://bstats.org/plugin/bukkit/EpicChatPlugin/14898">Epic Chat plugin bStats page</a>
	 * <a href="https://bstats.org/signatures/bukkit/EpicChatPlugin.svg">Epic Chat plugin Graph</a>
	 *
	 * @return Metrics ID
	 */
	@Override
	public int getMetricsPluginId() {
		return 14898;
	}

	@Override
	public MinecraftVersion.V getMinimumVersion() {
		return MinecraftVersion.V.v1_8;
	}

}