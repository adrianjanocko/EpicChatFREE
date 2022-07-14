package sk.adonikeoffice.epicchat.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.remain.CompSound;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class AnnouncementData implements ConfigSerializable {

	private final CompSound sound;
	private final String permission;
	private final List<String> chatMessage;

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"Sound", this.sound,
				"Permission", this.permission,
				"Chat_Message", this.chatMessage
		);
	}

	public static AnnouncementData deserialize(final SerializedMap map) {
		final CompSound sound = map.get("Sound", CompSound.class);
		final String permission = map.getString("Permission");
		final List<String> chatMessage = map.getStringList("Chat_Message");

		return new AnnouncementData(sound, permission, chatMessage);
	}

}