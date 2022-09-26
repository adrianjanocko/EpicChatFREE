package sk.adonikeoffice.epicchat;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import sk.adonikeoffice.epicchat.command.ReloadCommand;
import sk.adonikeoffice.epicchat.data.PlayerData;
import sk.adonikeoffice.epicchat.listener.ChatListener;
import sk.adonikeoffice.epicchat.settings.Settings;
import sk.adonikeoffice.epicchat.task.AnnouncementTask;
import sk.adonikeoffice.epicchat.task.QuestionTask;

public class EpicChatPlugin extends SimplePlugin {

    @Override
    protected String[] getStartupLogo() {
        return new String[]{
                "&4____ ___  _ ____    ____ _  _ ____ ___ ",
                "&4|___ |__] | |       |    |__| |__|  |  ",
                "&4|___ |    | |___    |___ |  | |  |  |  ",
        };
    }

    @Override
    protected void onPluginStart() {
        Messenger.ENABLED = false;
        Common.setLogPrefix("EpicChat |");

        Common.log(
                Common.consoleLine(),
                getNamed() + " " + getVersion() + " by " + getAuthor(),
                " ",
                "THIS VERSION IS NO LONGER UPDATED & SUPPORTED",
                " ",
                "Did you know that there is a",
                "premium version of this plugin?",
                " | Check it out right now:",
                " | https://bit.ly/3BB02WV",
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

        if (Settings.Chat.ENABLED)
            this.registerEvents(new ChatListener());

        if (Settings.Chat.Question.ENABLED) {
            Common.runTimerAsync(20, new QuestionTask());

            HookManager.addPlaceholder("question_answers", (target) -> String.valueOf(PlayerData.findPlayer(target).getReactedTimes()));
        }

        if (Settings.Chat.Announcement.ENABLED)
            Common.runTimerAsync(20, new AnnouncementTask());
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