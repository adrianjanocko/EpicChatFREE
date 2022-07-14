package sk.adonikeoffice.epicchat.listener;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.Remain;
import sk.adonikeoffice.epicchat.util.Util;

import static sk.adonikeoffice.epicchat.settings.Settings.Chat.Discord;

public final class DiscordListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
		final TextChannel channel = event.getJDA().getTextChannelById(Discord.CHAT_CHANNEL_ID);

		if (channel != null) {
			final Message message = event.getMessage();

			if (message.getAuthor().isBot())
				return;

			if (message.getTextChannel().getId().equals(channel.getId())) {
				final Member member = event.getMember();

				final String replacedMessage = Replacer.replaceArray(Discord.CHAT_FORMAT,
						"member_mention", member.getNickname() != null ? member.getNickname() : member.getUser().getName(),
						"message", ChatUtil.removeEmoji(event.getMessage().getContentRaw())
				);

				for (final Player player : Remain.getOnlinePlayers())
					Util.sendType(player, replacedMessage, false);

				if (Discord.LOG_ENABLED)
					Common.log(replacedMessage);
			}
		} else
			Common.log("[!] The Discord Channel is null.");
	}

}