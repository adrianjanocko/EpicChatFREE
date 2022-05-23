package sk.adonikeoffice.epicchat;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;
import sk.adonikeoffice.epicchat.command.ReloadCommand;
import sk.adonikeoffice.epicchat.listener.ChatListener;
import sk.adonikeoffice.epicchat.settings.Settings;

import java.util.Collections;
import java.util.List;

public class EpicChatPlugin extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		Common.ADD_TELL_PREFIX = false;
		Messenger.ENABLED = true;
		Messenger.setErrorPrefix("");

		Common.log(
				Common.consoleLineSmooth(),
				SimplePlugin.getNamed() + " " + SimplePlugin.getVersion() + " by " + getAuthor(),
				" ",
				"If you found a bug or you have an idea, please,",
				"post it on the GitHub in the Issues section:",
				"https://github.com/AdoNikeOFFICE/EpicChat/issues",
				" ",
				"Or, you can join our Discord:",
				"discord.epic-central.eu",
				Common.consoleLineSmooth()
		);

	}

	@Override
	protected void onReloadablesStart() {
		Valid.checkBoolean(HookManager.isPlaceholderAPILoaded(), "You need to install the PlaceholderAPI plugin, if you want to use placeholders in the chat.");

		Common.setLogPrefix("EpicChat |");
		Messenger.setSuccessPrefix(Settings.PLUGIN_PREFIX);

		registerCommand(new ReloadCommand());

		if (Settings.Chat.ENABLED)
			registerEvents(new ChatListener());
	}

	public static String getAuthor() {
		return "AdoNikeOFFICE";
	}

	@Override
	public int getFoundedYear() {
		return 2022; // 10.04.2022
	}

	/**
	 * https://bstats.org/plugin/bukkit/EpicChatPlugin/14898
	 * https://bstats.org/signatures/bukkit/EpicChatPlugin.svg
	 *
	 * @return Metrics ID
	 */
	@Override
	public int getMetricsPluginId() {
		return 14898;
	}

	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Collections.singletonList(Settings.class);
	}

}