package sk.adonikeoffice.epicchat.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.model.SimpleExpansion;
import sk.adonikeoffice.epicchat.data.PlayerData;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Placeholders extends SimpleExpansion {

	@Getter(value = AccessLevel.PRIVATE)
	private static final Placeholders instance = new Placeholders();

	@Override
	protected String onReplace(@NonNull final CommandSender sender, final String identifier) {
		final Player player = sender instanceof Player && ((Player) sender).isOnline() ? (Player) sender : null;

		if (identifier.startsWith("question_answers_")) {
			// Fix for Discord or console sender
			if (player == null)
				return "false";

			final String playerName = this.join(2);
			final Player playerToCheck = Bukkit.getPlayer(playerName);

			if (playerToCheck != null)
				return String.valueOf(PlayerData.findPlayer(playerToCheck).getReactedTimes());
		}

		return NO_REPLACE;
	}

}
