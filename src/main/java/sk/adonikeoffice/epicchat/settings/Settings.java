package sk.adonikeoffice.epicchat.settings;

import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	public static String PREFIX;

	private static void init() {
		PREFIX = getString("Prefix");
	}

	public static class Chat {
		public static Boolean ENABLED;
		public static Boolean PERMISSION_ENABLED;
		public static String PERMISSION;
		public static String PERMISSION_MESSAGE;

		public static String FORMAT;

		private static void init() {
			pathPrefix("Chat");
			ENABLED = getBoolean("Enabled");
			PERMISSION_ENABLED = getBoolean("Permission_Enabled");
			PERMISSION = getString("Permission");
			PERMISSION_MESSAGE = getString("Permission_Message");

			FORMAT = getString("Format");
		}

	}

	public static class Command {

		public static class Reload {

			public static String PERMISSION;
			public static String PERMISSION_MESSAGE;

			private static void init() {
				pathPrefix("Command.Reload");
				PERMISSION = getString("Permission");
				PERMISSION_MESSAGE = getString("Permission_Message");
			}

		}

	}

	public static class Message {

		public static String NO_CONSOLE;

		private static void init() {
			pathPrefix("Message");
			NO_CONSOLE = getString("No_Console");
		}

	}

}
