package sk.adonikeoffice.epicchat;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
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
				"",
				"If you found a bug or you have an idea, please,",
				"post it on the GitHub in the Issues section:",
				"https://github.com/AdoNikeOFFICE/EpicChat/issues",
				Common.consoleLineSmooth()
		);

	}

	@Override
	protected void onReloadablesStart() {
		Messenger.setSuccessPrefix(Settings.PREFIX);

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

	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Collections.singletonList(Settings.class);
	}

}
