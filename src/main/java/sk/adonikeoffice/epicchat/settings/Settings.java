package sk.adonikeoffice.epicchat.settings;

import org.mineacademy.fo.remain.CompChatColor;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 2;
	}

	private static void init() {
	}

	public static class Chat {
		public static Boolean ENABLED;
		public static Boolean PERMISSION_ENABLED;
		public static String PERMISSION;
		public static String PERMISSION_COLOR;

		public static String FORMAT;
		public static CompChatColor MESSAGE_COLOR;

		private static void init() {
			pathPrefix("Chat");
			ENABLED = getBoolean("Enabled");
			PERMISSION_ENABLED = getBoolean("Permission_Enabled");
			PERMISSION = getString("Permission");
			PERMISSION_COLOR = getString("Permission_Color");

			FORMAT = getString("Format");
			MESSAGE_COLOR = get("Message_Color", CompChatColor.class);
		}

		public static class Mention {

			public static Boolean ENABLED;
			public static String MESSAGE;
			public static CompChatColor COLOR;
			public static CompSound SOUND;

			private static void init() {
				pathPrefix("Chat.Mention");
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
				pathPrefix("Chat.Cooldown");
				ENABLED = getBoolean("Enabled");
				PERMISSION = getString("Permission");
				MESSAGE = getString("Message");
				DELAY = getInteger("Delay");
			}

		}

	}

	public static class Command {

		public static class Reload {

			public static String PERMISSION;

			private static void init() {
				pathPrefix("Command.Reload");
				PERMISSION = getString("Permission");
			}

		}

	}

	public static class Message {

		public static String NO_CONSOLE;
		public static String PERMISSION_MESSAGE;
		public static String INVALID_ARGS;

		private static void init() {
			pathPrefix("Message");
			NO_CONSOLE = getString("No_Console");
			PERMISSION_MESSAGE = getString("Permission");
			INVALID_ARGS = getString("Invalid_Args");
		}

	}

}
