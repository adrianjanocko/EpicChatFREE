package sk.adonikeoffice.epicchat.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.remain.CompChatColor;

@Getter
@RequiredArgsConstructor
public final class GroupData implements ConfigSerializable {

	private final String name;
	private final String format;
	private final CompChatColor messageColor;

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"Name", this.name,
				"Format", this.format,
				"Message_Color", this.messageColor
		);
	}

	public static GroupData deserialize(final SerializedMap map) {
		final String name = map.getString("Name");
		final String format = map.getString("Format");
		final CompChatColor messageColor = map.get("Message_Color", CompChatColor.class);

		return new GroupData(name, format, messageColor);
	}

}
