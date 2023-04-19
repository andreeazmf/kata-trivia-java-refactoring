package trivia.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameConstants {

    public static final int MIN_PLAYER_COUNT = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    public static final int POSITION_COUNT = 12;
    public static final int QUESTION_COUNT = 50;
}
