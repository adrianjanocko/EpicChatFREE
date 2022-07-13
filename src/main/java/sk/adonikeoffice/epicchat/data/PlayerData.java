package sk.adonikeoffice.epicchat.data;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.Collection;

@Getter
public class PlayerData extends YamlConfig {

	private static final ConfigItems<PlayerData> loadedPlayers = ConfigItems.fromFolder("players", PlayerData.class);

	private int reactedTimes;

	private PlayerData(final String name) {
		this.loadConfiguration(NO_DEFAULT, "players/" + name + ".yml");
	}

	@Override
	protected void onLoad() {
		this.reactedTimes = this.getInteger("Reacted_Times", 0);
	}

	@Override
	protected void onSave() {
		this.set("Reacted_Times", this.reactedTimes);
	}

	public void increaseReactedTimes() {
		this.reactedTimes++;

		this.save();
	}

	public static void createPlayer(final String name) {
		final PlayerData player = loadedPlayers.loadOrCreateItem(name, () -> new PlayerData(name));

		player.save();
	}

	public static boolean isLoaded(final Player player) {
		return isLoaded(player.getName());
	}

	public static boolean isLoaded(final String name) {
		return loadedPlayers.isItemLoaded(name);
	}

	public static void loadPlayers() {
		loadedPlayers.loadItems();
	}

	public static PlayerData findPlayer(final Player player) {
		return findPlayer(player.getName());
	}

	public static PlayerData findPlayer(final String name) {
		for (final PlayerData player : getLoadedPlayers()) {
			final String playerName = player.getName();

			if (playerName.equals(name))
				if (PlayerData.isLoaded(playerName))
					return player;
		}

		return null;
	}

	public static Collection<PlayerData> getLoadedPlayers() {
		return loadedPlayers.getItems();
	}

}