package trivia;

import trivia.question.QuestionCategory;
import trivia.question.QuestionDealer;

import java.util.ArrayList;
import java.util.List;

import static trivia.constants.GameConstants.MIN_PLAYER_COUNT;
import static trivia.constants.GameConstants.PLACE_COUNT;
import static trivia.constants.GameConstants.QUESTION_COUNT;
import static trivia.constants.GameConstants.WINNING_SCORE;

// REFACTOR ME
public class GameBetter implements IGame {

    private final List<Player> players = new ArrayList<>();

    private final QuestionDealer questionDealer;

    private int currentPlayerIndex = 0;
    private boolean isGettingOutOfPenaltyBox;

    public GameBetter() {
        questionDealer = QuestionDealer.init(QUESTION_COUNT);
    }

    public boolean add(String playerName) {
        players.add(new Player(playerName));
        logNewPlayer(playerName);

        return true;
    }

    public void roll(int roll) {
        logCurrentPlayer();
        logRoll(roll);

        if (currentPlayer().isInPenaltyBox()) {
            if (isEven(roll)) {
                isGettingOutOfPenaltyBox = false;
                logNotGettingOutOfPenaltyBox();
                return;
            }

            isGettingOutOfPenaltyBox = true;
            logGettingOutOfPenaltyBox();
        }

        executeRoll(roll);
    }

    public boolean wasCorrectlyAnswered() {
        if (currentPlayer().isInPenaltyBox() && !isGettingOutOfPenaltyBox) {
            goToNextPlayer();
            return true;
        }

        logCorrectAnswer();

        currentPlayer().earnOneCoin();
        logCurrentPlayerCoins();

        boolean isNotWinner = isNotWinner();

        goToNextPlayer();

        return isNotWinner;
    }

    public boolean wrongAnswer() {
        logWrongAnswer();
        logPlayerSentToPenaltyBox();

        currentPlayer().setInPenaltyBox(true);

        goToNextPlayer();

        return true;
    }

    public boolean isPlayable() {
        return (howManyPlayers() >= MIN_PLAYER_COUNT);
    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private int howManyPlayers() {
        return players.size();
    }

    private void executeRoll(int roll) {
        moveCurrentPlayer(roll);
        askQuestion();
    }

    private boolean isOdd(int roll) {
        return roll % 2 != 0;
    }

    private boolean isEven(int roll) {
        return roll % 2 == 0;
    }

    private void moveCurrentPlayer(int roll) {
        currentPlayer().move(roll, PLACE_COUNT);

        logNewLocation();
        logCurrentCategory();
    }

    private void askQuestion() {
        questionDealer.askQuestion(currentCategory());
    }

    private QuestionCategory currentCategory() {
        int questionOrder = currentPlayer().getPlace() % QuestionCategory.values().length;

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

    private void goToNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == howManyPlayers()) {
            currentPlayerIndex = 0;
        }
    }

    private boolean isNotWinner() {
        return !isWinner();
    }

    private boolean isWinner() {
        return currentPlayer().getCoins() == WINNING_SCORE;
    }

    private void logNewPlayer(String playerName) {
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + howManyPlayers());
    }

    private void logCurrentPlayer() {
        System.out.println(currentPlayer().getName() + " is the current player");
    }

    private void logRoll(int roll) {
        System.out.println("They have rolled a " + roll);
    }

    private void logGettingOutOfPenaltyBox() {
        System.out.println(currentPlayer().getName() + " is getting out of the penalty box");
    }

    private void logNotGettingOutOfPenaltyBox() {
        System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
    }

    private void logNewLocation() {
        System.out.println(currentPlayer().getName()
                + "'s new location is "
                + currentPlayer().getPlace());
    }

    private void logCurrentCategory() {
        System.out.println("The category is " + currentCategory().getValue());
    }

    private void logCurrentPlayerCoins() {
        System.out.println(currentPlayer().getName()
                + " now has "
                + currentPlayer().getCoins()
                + " Gold Coins.");
    }

    private void logCorrectAnswer() {
        System.out.println("Answer was correct!!!!");
    }

    private void logPlayerSentToPenaltyBox() {
        System.out.println(currentPlayer().getName() + " was sent to the penalty box");
    }

    private void logWrongAnswer() {
        System.out.println("Question was incorrectly answered");
    }
}
