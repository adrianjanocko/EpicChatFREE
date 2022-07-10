package sk.adonikeoffice.epicchat;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import sk.adonikeoffice.epicchat.command.ReloadCommand;
import sk.adonikeoffice.epicchat.listener.ChatListener;
import sk.adonikeoffice.epicchat.settings.Settings;

public class EpicChatPlugin extends SimplePlugin {

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
					"if you want to use Group_Format.",
					" ",
					"Ignore this message, if you don't want to.",
					Common.consoleLine()
			);

	}

	@Override
	protected void onReloadablesStart() {
		this.registerCommand(new ReloadCommand());

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
	public MinecraftVersion.V getMinimumVersion() {
		return MinecraftVersion.V.v1_8;
	}

}