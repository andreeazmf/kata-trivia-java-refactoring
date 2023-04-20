package trivia;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionCategory {

    POP(0, "Pop"),
    SCIENCE(1, "Science"),
    SPORTS(2, "Sports"),
    ROCK(3, "Rock");

    private final int order;
    private final String value;
}
