package sk.adonikeoffice.epicchat.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import sk.adonikeoffice.epicchat.EpicChatPlugin;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.util.Chat;

import java.util.List;

public class ReloadCommand extends SimpleCommand {

	public ReloadCommand() {
		super("epicchat|ec");

		setPermission(null);
		setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		if (!isPlayer())
			returnTell(Messenger.getSuccessPrefix() + Settings.Message.NO_CONSOLE);

		final Player player = getPlayer();
		final boolean hasAccess = player.hasPermission(Settings.Command.Reload.PERMISSION) || player.isOp();

		if (args.length == 0)
			returnTell(
					"&8" + Common.chatLineSmooth(),
					" &4&l" + SimplePlugin.getNamed() + " &7" + SimplePlugin.getVersion(),
					" &f&oMade by " + EpicChatPlugin.getAuthor(),
					"&f",
					hasAccess ? " &7Use &f/{label} reload &7to reload the plugin" : "",
					"&8" + Common.chatLineSmooth()
			);

		final String param = args[0];

		if (hasAccess) {
			if ("reload".equalsIgnoreCase(param)) {
				try {
					SimplePlugin.getInstance().reload();

					Messenger.success(player, "Plugin has been reloaded.");
				} catch (final Throwable t) {
					Common.error(t, "Contact the Author of the plugin. (DISCORD AdoNikeOFFICE(hashtag)9999)");
				}
			} else
				Messenger.success(player, Settings.Message.INVALID_ARGS);
		} else
			Messenger.success(player, Settings.Message.PERMISSION_MESSAGE);

	}

	@Override
	protected List<String> tabComplete() {
		if (Chat.hasPermission(getPlayer(), Settings.Command.Reload.PERMISSION) && args.length == 1)
			return completeLastWord("reload");

		return NO_COMPLETE;
	}

}