package trivia.question;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class QuestionDeck {

    private final Queue<Question> questions;

    public static QuestionDeck createPopQuestionDeck(int questionCount) {
        return createQuestionDeck(questionCount, QuestionCategory.POP);
    }

    public static QuestionDeck createScienceQuestionDeck(int questionCount) {
        return createQuestionDeck(questionCount, QuestionCategory.SCIENCE);
    }

    public static QuestionDeck createSportsQuestionDeck(int questionCount) {
        return createQuestionDeck(questionCount, QuestionCategory.SPORTS);
    }

    public static QuestionDeck createRockQuestionDeck(int questionCount) {
        return createQuestionDeck(questionCount, QuestionCategory.ROCK);
    }

    private static QuestionDeck createQuestionDeck(int questionCount, QuestionCategory questionCategory) {
        return new QuestionDeck(
                createQuestions(questionCount, QuestionCategory.getQuestionConstructorByCategory(questionCategory))
        );
    }

    private static Queue<Question> createQuestions(int questionCount, IntFunction<Question> questionConstructor) {
        return IntStream.range(0, questionCount)
                .mapToObj(questionConstructor)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void askQuestion() {
        log.info(String.valueOf(questions.remove()));
    }
}
