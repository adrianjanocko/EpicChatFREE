package sk.adonikeoffice.epicchat.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
	ACTIONBAR("{actionbar}"), CHAT("{chat}"), BOSSBAR("{bossbar}");

	private final String type;
}
