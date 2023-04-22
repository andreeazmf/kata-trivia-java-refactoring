package trivia.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.IntFunction;

@AllArgsConstructor
@Getter
public enum QuestionCategory {

    POP(0, "Pop"),
    SCIENCE(1, "Science"),
    SPORTS(2, "Sports"),
    ROCK(3, "Rock");

    private final int order;
    private final String value;

    private static final Map<QuestionCategory, IntFunction<Question>> questionConstructorsByCategory;

    static {
        questionConstructorsByCategory = new EnumMap<>(QuestionCategory.class);
        questionConstructorsByCategory.put(POP, Question::createPopQuestion);
        questionConstructorsByCategory.put(SCIENCE, Question::createScienceQuestion);
        questionConstructorsByCategory.put(SPORTS, Question::createSportsQuestion);
        questionConstructorsByCategory.put(ROCK, Question::createRockQuestion);
    }

    public static IntFunction<Question> getQuestionConstructorByCategory(QuestionCategory questionCategory) {
        return questionConstructorsByCategory.get(questionCategory);
    }
}
