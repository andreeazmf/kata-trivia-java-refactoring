package trivia.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trivia.question.QuestionCategory;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static trivia.constants.GameConstants.WINNING_SCORE;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Player {

    private final String name;
    private int place;
    private int coins;
    private boolean isInPenaltyBox;
    private boolean isGettingOutOfPenaltyBox;
    private int winningStreak;
    private final Map<QuestionCategory, Integer> wrongAnswersCount;

    public static Player init(String name) {
        return new Player(name, initWrongAnswersCount());
    }

    private static Map<QuestionCategory, Integer> initWrongAnswersCount() {
        return Stream.of(QuestionCategory.values())
                .collect(Collectors.toMap(
                        questionCategory -> questionCategory,
                        questionCategory -> 0,
                        (v1, v2) -> v1,
                        () -> new EnumMap<>(QuestionCategory.class)
                ));
    }

    public void obtainBenefits() {
        winningStreak++;

        if (winningStreak <= 3) {
            earnOneCoin();
        } else {
            earnTwoCoins();
        }

        logCoins();
    }

    public void move(int distance, int placeCount) {
        place = (place + distance) % placeCount;
        logNewLocation();
    }

    public void gaveWrongAnswer() {
        wrongAnswersCount.compute(getCurrentQuestionCategory(), this::updateWrongAnswerCount);

        if (shouldBeSendToPenaltyBox()) {
            sendToPenaltyBox();
        }
    }

    public QuestionCategory getCurrentQuestionCategory() {
        int questionOrder = place % QuestionCategory.values().length;

        switch (questionOrder) {
            case 0:
                return QuestionCategory.POP;
            case 1:
                return QuestionCategory.SCIENCE;
            case 2:
                return QuestionCategory.SPORTS;
            case 3:
                return QuestionCategory.ROCK;
            default:
                return QuestionCategory.GEOGRAPHY;
        }
    }

    public void setIsGettingOutOfPenaltyBox() {
        if (isInPenaltyBox) {
            isGettingOutOfPenaltyBox = true;
            isInPenaltyBox = false;
        }

        logIsGettingOutOfPenaltyBox();
    }

    public void setIsNotGettingOutOfPenaltyBox() {
        if (isInPenaltyBox) {
            isGettingOutOfPenaltyBox = false;
        }

        logIsNotGettingOutOfPenaltyBox();
    }

    public boolean canObtainBenefits() {
        if (!isInPenaltyBox) {
            return true;
        }

        return isGettingOutOfPenaltyBox;
    }

    public boolean isNotWinner() {
        return !isWinner();
    }

    public boolean isWinner() {
        return coins >= WINNING_SCORE;
    }

    private int updateWrongAnswerCount(QuestionCategory questionCategory, Integer count) {
        if (count == null) {
            return 1;
        }

        return count + 1;
    }

    private boolean shouldBeSendToPenaltyBox() {
        if (winningStreak > 0) {
            winningStreak = 0;
            return false;
        }

        return hasTwoOrMoreWrongAnswersFromSameCategory();
    }

    private boolean hasTwoOrMoreWrongAnswersFromSameCategory() {
        return wrongAnswersCount.entrySet().stream()
                .anyMatch(entry -> entry.getValue() >= 2);
    }

    private void sendToPenaltyBox() {
        isInPenaltyBox = true;
        isGettingOutOfPenaltyBox = false;
        logSendToPenaltyBox();
    }

    private void earnOneCoin() {
        coins++;
        logCoins();
    }

    private void earnTwoCoins() {
        coins += 2;
    }

    private void logNewLocation() {
        log.info(name + "'s new location is " + place);
    }

    private void logCoins() {
        log.info(name + " now has " + coins + " Gold Coins.");
    }

    private void logSendToPenaltyBox() {
        log.info(name + " was sent to the penalty box");
    }

    private void logIsGettingOutOfPenaltyBox() {
        log.info(name + " is getting out of the penalty box");
    }

    private void logIsNotGettingOutOfPenaltyBox() {
        log.info(name + " is not getting out of the penalty box");
    }
}
