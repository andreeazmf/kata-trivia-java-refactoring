package trivia.question;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public class QuestionDealer {

    private final int questionCount;
    private final QuestionDeck popQuestions;
    private final QuestionDeck scienceQuestions;
    private final QuestionDeck sportsQuestions;
    private final QuestionDeck rockQuestions;
    private final QuestionDeck geographyQuestions;

    public static QuestionDealer init(int questionCount) {
        return QuestionDealer.builder()
                .popQuestions(QuestionDeck.createPopQuestionDeck(questionCount))
                .scienceQuestions(QuestionDeck.createScienceQuestionDeck(questionCount))
                .sportsQuestions(QuestionDeck.createSportsQuestionDeck(questionCount))
                .rockQuestions(QuestionDeck.createRockQuestionDeck(questionCount))
                .geographyQuestions(QuestionDeck.createGeographyQuestionDeck(questionCount))
                .build();
    }

    public void askQuestion(QuestionCategory questionCategory) {
        switch (questionCategory) {
            case POP:
                popQuestions.askQuestion();
                break;
            case SCIENCE:
                scienceQuestions.askQuestion();
                break;
            case SPORTS:
                sportsQuestions.askQuestion();
                break;
            case ROCK:
                rockQuestions.askQuestion();
        }
    }
}
