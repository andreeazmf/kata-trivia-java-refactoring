package trivia;

import trivia.player.PlayerManager;
import trivia.question.QuestionDealer;

import static trivia.constants.GameConstants.MAX_PLAYER_COUNT;
import static trivia.constants.GameConstants.MIN_PLAYER_COUNT;
import static trivia.constants.GameConstants.PLACE_COUNT;
import static trivia.constants.GameConstants.QUESTION_COUNT;

// REFACTOR ME
public class GameBetter implements IGame {

    private final PlayerManager playerManager;
    private final QuestionDealer questionDealer;

    public GameBetter() {
        playerManager = PlayerManager.init(MAX_PLAYER_COUNT, PLACE_COUNT);
        questionDealer = QuestionDealer.init(QUESTION_COUNT);
    }

    public boolean add(String playerName) {
        return playerManager.add(playerName);
    }

    public void roll(int roll) {
        logCurrentPlayer();
        logRoll(roll);

        if (playerManager.isCurrentPlayerInPenaltyBox()) {
            if (isEven(roll)) {
                playerManager.setCurrentPlayerIsNotGettingOutOfPenaltyBox();
                return;
            }

            playerManager.setCurrentPlayerIsGettingOutOfPenaltyBox();
        }

        executeRoll(roll);
    }

    public boolean wasCorrectlyAnswered() {
        if (playerManager.canCurrentPlayEarnCoins()) {
            playerManager.goToNextPlayer();
            return true;
        }

        logCorrectAnswer();

        playerManager.currentPlayerEarnsOneCoin();

        boolean isNotWinner = playerManager.isCurrentPlayerNotWinner();

        playerManager.goToNextPlayer();

        return isNotWinner;
    }

    public boolean wrongAnswer() {
        logWrongAnswer();

        playerManager.moveCurrentPlayerToPenaltyBox();
        playerManager.goToNextPlayer();

        return true;
    }

    public boolean isPlayable() {
        return (playerManager.howManyPlayers() >= MIN_PLAYER_COUNT);
    }

    private void executeRoll(int roll) {
        playerManager.moveCurrentPlayer(roll);
        questionDealer.askQuestion(playerManager.getCurrentPlayerQuestionCategory());
    }

    private boolean isEven(int roll) {
        return roll % 2 == 0;
    }

    private void logCurrentPlayer() {
        System.out.println(playerManager.getCurrentPlayerName() + " is the current player");
    }

    private void logRoll(int roll) {
        System.out.println("They have rolled a " + roll);
    }

    private void logCorrectAnswer() {
        System.out.println("Answer was correct!!!!");
    }


    private void logWrongAnswer() {
        System.out.println("Question was incorrectly answered");
    }
}
