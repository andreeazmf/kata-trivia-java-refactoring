package trivia.question;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.IntFunction;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {

    @ParameterizedTest
    @MethodSource("createQuestionByCategoryProvider")
    void createQuestionByCategory(QuestionCategory questionCategory, IntFunction<Question> questionConstructor) {
        Question actual = questionConstructor.apply(0);

        assertEquals(0, actual.getIndex());
        assertEquals(questionCategory, actual.getCategory());
        assertEquals(questionCategory.getValue() + " Question 0", actual.toString());
    }

    private static Stream<Arguments> createQuestionByCategoryProvider() {
        return Stream.of(
                Arguments.of(QuestionCategory.POP, QuestionCategory.getQuestionConstructorByCategory(QuestionCategory.POP)),
                Arguments.of(QuestionCategory.SCIENCE, QuestionCategory.getQuestionConstructorByCategory(QuestionCategory.SCIENCE)),
                Arguments.of(QuestionCategory.SPORTS, QuestionCategory.getQuestionConstructorByCategory(QuestionCategory.SPORTS)),
                Arguments.of(QuestionCategory.ROCK, QuestionCategory.getQuestionConstructorByCategory(QuestionCategory.ROCK)),
                Arguments.of(QuestionCategory.GEOGRAPHY, QuestionCategory.getQuestionConstructorByCategory(QuestionCategory.GEOGRAPHY))
        );
    }

    // OR different tests? - got coverage with either

    @Test
    void createPopQuestion() {
        Question actual = Question.createPopQuestion(0);

        assertEquals(0, actual.getIndex());
        assertEquals(QuestionCategory.POP, actual.getCategory());
        assertEquals("Pop Question 0", actual.toString());
    }

    @Test
    void createScienceQuestion() {
        Question actual = Question.createScienceQuestion(0);

        assertEquals(0, actual.getIndex());
        assertEquals(QuestionCategory.SCIENCE, actual.getCategory());
        assertEquals("Science Question 0", actual.toString());
    }

    @Test
    void createSportsQuestion() {
        Question actual = Question.createSportsQuestion(0);

        assertEquals(0, actual.getIndex());
        assertEquals(QuestionCategory.SPORTS, actual.getCategory());
        assertEquals("Sports Question 0", actual.toString());
    }

    @Test
    void createRockQuestion() {
        Question actual = Question.createRockQuestion(0);

        assertEquals(0, actual.getIndex());
        assertEquals(QuestionCategory.ROCK, actual.getCategory());
        assertEquals("Rock Question 0", actual.toString());
    }

    @Test
    void createGeographyQuestion() {
        Question actual = Question.createGeographyQuestion(0);

        assertEquals(0, actual.getIndex());
        assertEquals(QuestionCategory.GEOGRAPHY, actual.getCategory());
        assertEquals("Geography Question 0", actual.toString());
    }
}
