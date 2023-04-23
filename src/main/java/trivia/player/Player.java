package trivia.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public void earnOneCoin() {
        coins++;
        logCoins();
    }

    public void move(int distance, int placeCount) {
        place = (place + distance) % placeCount;
        logNewLocation();
    }

    public void moveToPenaltyBox() {
        isInPenaltyBox = true;
        isGettingOutOfPenaltyBox = false;
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

    public boolean canEarnCoins() {
        return isInPenaltyBox && !isGettingOutOfPenaltyBox;
    }

    public boolean isNotWinner() {
        return !isWinner();
    }

    private boolean isWinner() {
        return coins == WINNING_SCORE;
    }

    private void logNewLocation() {
        log.info(name + "'s new location is " + place);
    }

    private void logCoins() {
        log.info(name + " now has " + coins + " Gold Coins.");
    }

    private void logIsGettingOutOfPenaltyBox() {
        log.info(name + " is getting out of the penalty box");
    }

    private void logIsNotGettingOutOfPenaltyBox() {
        log.info(name + " is not getting out of the penalty box");
    }
}