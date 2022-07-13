package sk.adonikeoffice.epicchat.settings;

import org.mineacademy.fo.remain.CompChatColor;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.settings.SimpleSettings;
import sk.adonikeoffice.epicchat.data.EmojiData;
import sk.adonikeoffice.epicchat.data.GroupData;

import java.util.List;
import java.util.Set;

public final class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 4;
	}

	private static void init() {
	}

	public static class Chat {
		public static Boolean ENABLED;
		public static String ALIASES;
		public static String PERMISSION;
		public static String PERMISSION_COLOR;
		public static Boolean LOG_ENABLED;

		public static String FORMAT;
		public static CompChatColor MESSAGE_COLOR;
		public static Set<GroupData> GROUP_FORMAT;
		public static List<String> HOVER;
		public static Set<EmojiData> EMOJIS;

		private static void init() {
			setPathPrefix("Chat");
			ENABLED = getBoolean("Enabled");
			ALIASES = getString("Aliases");
			PERMISSION = getString("Permission");
			PERMISSION_COLOR = getString("Permission_Color");
			LOG_ENABLED = getBoolean("Log_Enabled");

			FORMAT = getString("Format");
			MESSAGE_COLOR = get("Message_Color", CompChatColor.class);
			GROUP_FORMAT = getSet("Group_Format", GroupData.class);
			HOVER = getStringList("Hover");
			EMOJIS = getSet("Emojis", EmojiData.class);
		}

		public static class Mention {

			public static Boolean ENABLED;
			public static String MESSAGE;
			public static CompChatColor COLOR;
			public static CompSound SOUND;

			private static void init() {
				setPathPrefix("Chat.Mention");
				ENABLED = getBoolean("Enabled");
				MESSAGE = getString("Message");
				COLOR = get("Color", CompChatColor.class);
				SOUND = get("Sound", CompSound.class);
			}

		}

		/*public static class AntiSwear {

			public static Boolean ENABLED;
			public static String PERMISSION;
			public static String MESSAGE;
			public static List<String> WORDS;

			private static void init() {
				pathPrefix("Chat.Anti_Swear");
				ENABLED = getBoolean("Enabled");
				PERMISSION = getString("Permission");
				MESSAGE = getString("Message");
				WORDS = getStringList("Words");
			}

		}*/

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

		public static class Discord {

			public static Boolean ENABLED;
			public static Boolean LOG_ENABLED;
			public static String TOKEN;
			public static Long CHAT_CHANNEL_ID;
			public static String CHAT_FORMAT;
			public static String DISCORD_FORMAT;

			private static void init() {
				setPathPrefix("Chat.Discord");
				ENABLED = getBoolean("Enabled");
				LOG_ENABLED = getBoolean("Log_Enabled");
				TOKEN = getString("Token");
				CHAT_CHANNEL_ID = getInstance().getLong("Chat_Channel_ID");
				CHAT_FORMAT = getString("Chat_Format");
				DISCORD_FORMAT = getString("Discord_Format");
			}

			public static boolean isEnabled() {
				return ENABLED && Chat.ENABLED;
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

	}

}
