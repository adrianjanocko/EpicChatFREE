package sk.adonikeoffice.epicchat.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SimpleCommand {

	public ReloadCommand() {
		super("epicchat|ec|chat");

		this.setPermission(null);
		this.setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		if (!this.isPlayer())
			this.returnTell(Settings.Message.NO_CONSOLE);

		final Player player = getPlayer();
		final boolean hasAccess = Util.hasPermission(player, Settings.Command.Reload.PERMISSION);

		if (this.args.length == 0) {
			final List<String> helpMessage = new ArrayList<>();

			helpMessage.add("&8" + Common.chatLine());
			helpMessage.add(Settings.PLUGIN_PREFIX + " Running EpicChatᵀᴹ &f" + SimplePlugin.getVersion());
			helpMessage.add(Settings.PLUGIN_PREFIX + " Made by &fAdoNikeOFFICE &7in &4²⁰²²");

			if (hasAccess) {
				helpMessage.add(" ");
				helpMessage.add(Settings.PLUGIN_PREFIX + " &7Type &4/" + this.getCurrentLabel() + " reload &7to reload this plugin.");
			}

			helpMessage.add("&8" + Common.chatLine());

			this.returnTell(helpMessage);
		}

		final String param = this.args[0].toLowerCase();

		if (hasAccess) {
			if ("reload".equals(param)) {
				try {
					SimplePlugin.getInstance().reload();

					this.tell("Plugin has been reloaded.");
				} catch (final Throwable t) {
					Common.error(t, "Join EpicChat discord, create a ticket and report this to the author (AdoNikeOFFICE).");
				}
			} else
				this.tell(Settings.Message.INVALID_ARGS);
		} else
			this.tell(Settings.Message.PERMISSION_MESSAGE);
	}
	
	@Override
	protected List<String> tabComplete() {
		if (this.args.length == 1 && Util.hasPermission(this.getPlayer(), Settings.Command.Reload.PERMISSION))
			return this.completeLastWord("reload");

		return NO_COMPLETE;
	}

}