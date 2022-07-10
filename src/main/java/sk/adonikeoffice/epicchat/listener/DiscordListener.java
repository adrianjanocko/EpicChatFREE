package sk.adonikeoffice.epicchat.listener;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.Replacer;
import sk.adonikeoffice.epicchat.settings.Settings;

public class DiscordListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
		final TextChannel channel = event.getJDA().getTextChannelById(Settings.Chat.Discord.CHAT_CHANNEL_ID);

		if (channel != null) {
			final Message message = event.getMessage();
			
			if (message.getAuthor().isBot())
				return;

			if (message.getTextChannel().getId().equals(channel.getId())) {
				final Member member = event.getMember();

				final String replacedMessage = Replacer.replaceArray(Settings.Chat.Discord.CHAT_FORMAT,
						"member_mention", member.getNickname() != null ? member.getNickname() : member.getUser().getName(),
						"message", ChatUtil.removeEmoji(event.getMessage().getContentRaw())
				);

				Common.broadcast(replacedMessage);
			}
		} else
			Common.log("[!] The Discord Channel is null.");
	}

}