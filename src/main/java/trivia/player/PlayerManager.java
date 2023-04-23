package trivia.player;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trivia.question.QuestionCategory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class PlayerManager {

    private final List<Player> players = new ArrayList<>();
    private final int maxPlayerCount;
    private final int placeCount;
    private int currentPlayerIndex = 0;

    public static PlayerManager init(int maxPlayerCount, int placeCount) {
        return new PlayerManager(maxPlayerCount, placeCount);
    }

    public boolean add(String playerName) {
        if (players.size() == maxPlayerCount || playerNameExists(playerName)) {
            return false;
        }

        players.add(new Player(playerName));
        logNewPlayerAdded(playerName);
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public String getCurrentPlayerName() {
        return players.get(currentPlayerIndex).getName();
    }

    public boolean isCurrentPlayerInPenaltyBox() {
        return players.get(currentPlayerIndex).isInPenaltyBox();
    }

    public boolean isCurrentPlayerNotWinner() {
        return players.get(currentPlayerIndex).isNotWinner();
    }

    public void moveCurrentPlayer(int roll) {
        players.get(currentPlayerIndex).move(roll, placeCount);
        logCurrentPlayerQuestionCategory();
    }

    public void moveCurrentPlayerToPenaltyBox() {
        players.get(currentPlayerIndex).moveToPenaltyBox();
        logCurrentPlayerMovedToPenaltyBox();
    }

    public void goToNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) {
            currentPlayerIndex = 0;
        }
    }

    public QuestionCategory getCurrentPlayerQuestionCategory() {
        int questionOrder = players.get(currentPlayerIndex).getPlace() % QuestionCategory.values().length;

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

    public void currentPlayerEarnsOneCoin() {
        players.get(currentPlayerIndex).earnOneCoin();
    }

    public void setCurrentPlayerIsGettingOutOfPenaltyBox() {
        players.get(currentPlayerIndex).setIsGettingOutOfPenaltyBox();
    }

    public void setCurrentPlayerIsNotGettingOutOfPenaltyBox() {
        players.get(currentPlayerIndex).setIsNotGettingOutOfPenaltyBox();
    }

    public boolean canCurrentPlayEarnCoins() {
        return players.get(currentPlayerIndex).canEarnCoins();
    }

    private boolean playerNameExists(String playerName) {
        return players.stream()
                .anyMatch(player -> player.getName().equalsIgnoreCase(playerName));
    }

    private void logNewPlayerAdded(String playerName) {
        log.info(playerName + " was added");
        log.info("They are player number " + players.size());
    }

    private void logCurrentPlayerMovedToPenaltyBox() {
        log.info(getCurrentPlayerName() + " was sent to the penalty box");
    }

    private void logCurrentPlayerQuestionCategory() {
        log.info("The category is " + getCurrentPlayerQuestionCategory().getValue());
    }
}
