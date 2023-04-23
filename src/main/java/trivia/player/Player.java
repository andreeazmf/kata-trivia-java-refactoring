package trivia.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trivia.question.QuestionCategory;

import static trivia.constants.GameConstants.WINNING_SCORE;

@Getter
@RequiredArgsConstructor
@Slf4j
public class Player {

    private final String name;
    private int place;
    private int coins;
    private boolean isInPenaltyBox;
    private boolean isGettingOutOfPenaltyBox;
    private int winningStreak;

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
        if (winningStreak > 0) {
            winningStreak = 0;
            return;
        }

        sendToPenaltyBox();
    }

    public void sendToPenaltyBox() {
        isInPenaltyBox = true;
        isGettingOutOfPenaltyBox = false;
        logSendToPenaltyBox();
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
            default:
                return QuestionCategory.ROCK;
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
