package sk.adonikeoffice.epicchat.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;

@Getter
@RequiredArgsConstructor
public class GroupData implements ConfigSerializable {

	private final String name;
	private final String format;

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"Name", this.name,
				"Format", this.format
		);
	}

	public static GroupData deserialize(final SerializedMap map) {
		final String name = map.getString("Name");
		final String format = map.getString("Format");

		return new GroupData(name, format);
	}

}
