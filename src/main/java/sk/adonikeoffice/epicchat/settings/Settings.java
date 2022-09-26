package sk.adonikeoffice.epicchat.settings;

import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.remain.CompChatColor;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.settings.SimpleSettings;
import sk.adonikeoffice.epicchat.data.AnnouncementData;
import sk.adonikeoffice.epicchat.data.EmojiData;
import sk.adonikeoffice.epicchat.data.GroupData;
import sk.adonikeoffice.epicchat.data.QuestionData;

import java.util.List;
import java.util.Set;

public final class Settings extends SimpleSettings {

    @Override
    protected int getConfigVersion() {
        return 1;
    }

    public static class Chat {

        public static Boolean ENABLED;
        public static List<String> ALIASES;
        public static String PERMISSION;
        public static String PERMISSION_COLOR;
        public static Boolean LOG_ENABLED;

        public static String FORMAT;
        public static CompChatColor MESSAGE_COLOR;
        public static Set<GroupData> GROUP_FORMAT;
        public static List<String> HOVER;
        public static String HOVER_CLICK_COMMAND;

        private static void init() {
            setPathPrefix("Chat");
            ENABLED = getBoolean("Enabled");
            ALIASES = getStringList("Aliases");
            PERMISSION = getString("Permission");
            PERMISSION_COLOR = getString("Permission_Color");
            LOG_ENABLED = getBoolean("Log_Enabled");

            FORMAT = getString("Format");
            MESSAGE_COLOR = get("Message_Color", CompChatColor.class);
            GROUP_FORMAT = getSet("Group_Format", GroupData.class);
            HOVER = getStringList("Hover");
            HOVER_CLICK_COMMAND = getString("Hover_Click_Command");
        }

        public static class Emoji {

            public static Boolean ENABLED;
            public static CompChatColor COLOR;
            public static Set<EmojiData> EMOJIS;

            private static void init() {
                setPathPrefix("Chat.Emoji");
                ENABLED = getBoolean("Enabled");
                COLOR = get("Color", CompChatColor.class);
                EMOJIS = getSet("Emojis", EmojiData.class);
            }

        }

        public static class Mention {

            public static Boolean ENABLED;
            public static String MESSAGE;
            public static String COLOR;
            public static CompSound SOUND;

            private static void init() {
                setPathPrefix("Chat.Mention");
                ENABLED = getBoolean("Enabled");
                MESSAGE = getString("Message");
                COLOR = getString("Color");
                SOUND = get("Sound", CompSound.class);
            }

        }

        public static class Cooldown {

            public static Boolean ENABLED;
            public static String PERMISSION;
            public static String MESSAGE;
            public static Integer DELAY;

            private static void init() {
                setPathPrefix("Chat.Cooldown");
                ENABLED = getBoolean("Enabled");
                PERMISSION = getString("Permission");
                MESSAGE = getString("Message");
                DELAY = getInteger("Delay_Seconds");
            }

        }

        public static class Question {

            public static Boolean ENABLED;
            public static SimpleTime REPEAT_EVERY;
            public static SimpleTime INACTIVE_CANCEL;
            public static Set<QuestionData> QUESTIONS;
            public static Set<String> REWARDS;

            private static void init() {
                setPathPrefix("Chat.Question");
                ENABLED = getBoolean("Enabled");
                REPEAT_EVERY = getTime("Repeat_Every");
                INACTIVE_CANCEL = getTime("Inactive_Cancel");
                QUESTIONS = getSet("Questions", QuestionData.class);
                REWARDS = getSet("Rewards", String.class);
            }

        }

        public static class Announcement {

            public static Boolean ENABLED;
            public static SimpleTime REPEAT_EVERY;
            public static Set<AnnouncementData> ANNOUNCEMENTS;

            private static void init() {
                setPathPrefix("Chat.Announcement");
                ENABLED = getBoolean("Enabled");
                REPEAT_EVERY = getTime("Repeat_Every");
                ANNOUNCEMENTS = getSet("Announcements", AnnouncementData.class);
            }

        }

    }

    public static class Command {

        public static class Reload {

            public static String PERMISSION;

            private static void init() {
                setPathPrefix("Command.Reload");
                PERMISSION = getString("Permission");
            }

        }

    }

    public static class Message {

        public static String NO_CONSOLE;
        public static String NO_PERMISSION;
        public static String INVALID_ARGS;
        public static String MUTED;
        public static String NOT_LOGGED;

        private static void init() {
            setPathPrefix("Message");
            NO_CONSOLE = getString("No_Console");
            NO_PERMISSION = getString("No_Permission");
            INVALID_ARGS = getString("Invalid_Args");
            MUTED = getString("Muted");
            NOT_LOGGED = getString("Not_Logged");
        }

        public static class Question {

            public static String GUESSED;
            public static String INACTIVE_CANCEL;

            private static void init() {
                setPathPrefix("Message.Question_Message");
                GUESSED = getString("Guessed");
                INACTIVE_CANCEL = getString("Inactive_Cancel");
            }

        }

    }

}
