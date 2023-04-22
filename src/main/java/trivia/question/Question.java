package trivia.question;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Question {

    private final QuestionCategory category;
    private final int index;

    public static Question createPopQuestion(int index) {
        return createQuestion(QuestionCategory.POP, index);
    }

    public static Question createScienceQuestion(int index) {
        return createQuestion(QuestionCategory.SCIENCE, index);
    }

    public static Question createSportsQuestion(int index) {
        return createQuestion(QuestionCategory.SPORTS, index);
    }

    public static Question createRockQuestion(int index) {
        return createQuestion(QuestionCategory.ROCK, index);
    }

    private static Question createQuestion(QuestionCategory questionCategory, int index) {
        return new Question(questionCategory, index);
    }

    @Override
    public String toString() {
        return category.getValue() + " Question " + index;
    }
}
