package trivia;

import lombok.extern.slf4j.Slf4j;
import trivia.player.PlayerManager;
import trivia.question.QuestionDealer;

import static trivia.constants.ErrorConstants.ERR_ADD_AFTER_START;
import static trivia.constants.ErrorConstants.ERR_PLAY_BEFORE_START;
import static trivia.constants.GameConstants.MAX_PLAYER_COUNT;
import static trivia.constants.GameConstants.MIN_PLAYER_COUNT;
import static trivia.constants.GameConstants.PLACE_COUNT;
import static trivia.constants.GameConstants.QUESTION_COUNT;

// REFACTOR ME
@Slf4j
public class GameBetter implements IGame {

    private final PlayerManager playerManager;
    private final QuestionDealer questionDealer;
    private boolean start;

    public GameBetter() {
        playerManager = PlayerManager.init(MAX_PLAYER_COUNT, PLACE_COUNT);
        questionDealer = QuestionDealer.init(QUESTION_COUNT);
    }

    public boolean start() {
        start = isPlayable();
        return start;
    }

    public boolean add(String playerName) {
        if (start) {
            logAddPlayerAfterGameStart();
            return false;  // or throw error?
        }

        return playerManager.add(playerName);
    }

    public void roll(int roll) {
        if (!start) {
            logPlayGameBeforeStart();
            return;  // or throw error?
        }

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
        if (!start) {
            logPlayGameBeforeStart();
            return true;  // or throw error?
        }

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
        if (!start) {
            logPlayGameBeforeStart();
            return false;  // or throw error?
        }

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
        log.info(playerManager.getCurrentPlayerName() + " is the current player");
    }

    private void logRoll(int roll) {
        log.info("They have rolled a " + roll);
    }

    private void logCorrectAnswer() {
        log.info("Answer was correct!!!!");
    }

    private void logWrongAnswer() {
        log.info("Question was incorrectly answered");
    }

    private void logAddPlayerAfterGameStart() {
        log.error(ERR_ADD_AFTER_START);
    }

    private void logPlayGameBeforeStart() {
        log.error(ERR_PLAY_BEFORE_START);
    }
}
