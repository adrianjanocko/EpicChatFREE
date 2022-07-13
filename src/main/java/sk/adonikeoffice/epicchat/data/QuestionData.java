package sk.adonikeoffice.epicchat.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.ConfigSerializable;

@Getter
@RequiredArgsConstructor
public final class QuestionData implements ConfigSerializable {

	private final String question;
	private final String answer;

	@Override
	public SerializedMap serialize() {
		return SerializedMap.ofArray(
				"Question", this.question,
				"Answer", this.answer
		);
	}

	public static QuestionData deserialize(final SerializedMap map) {
		final String question = map.getString("Question");
		final String answer = map.getString("Answer");

		return new QuestionData(question, answer);
	}

}