package trivia.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

    public static final String ERR_PLAY_BEFORE_START = "Cannot execute this step before the game has started";
    public static final String ERR_ADD_AFTER_START = "Cannot add players after the game has started";
}
