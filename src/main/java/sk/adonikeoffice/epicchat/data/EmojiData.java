package sk.adonikeoffice.epicchat.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;

@Getter
@RequiredArgsConstructor
public final class EmojiData implements ConfigSerializable {

	private final String whatToReplace;
	private final String replaceTo;

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"What_To_Replace", this.whatToReplace,
				"Replace_To", this.replaceTo
		);
	}

	public static EmojiData deserialize(final SerializedMap map) {
		final String whatToReplace = map.getString("What_To_Replace");
		final String replaceTo = map.getString("Replace_To");

		return new EmojiData(whatToReplace, replaceTo);
	}

}