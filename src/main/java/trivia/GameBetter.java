package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static trivia.constants.GameConstants.MIN_PLAYER_COUNT;
import static trivia.constants.GameConstants.PLACE_COUNT;
import static trivia.constants.GameConstants.QUESTION_COUNT;
import static trivia.constants.GameConstants.WINNING_SCORE;

// REFACTOR ME
public class GameBetter implements IGame {

    private final List<Player> players = new ArrayList<>();

    private final LinkedList<String> popQuestions = new LinkedList<>();
    private final LinkedList<String> scienceQuestions = new LinkedList<>();
    private final LinkedList<String> sportsQuestions = new LinkedList<>();
    private final LinkedList<String> rockQuestions = new LinkedList<>();

    private int currentPlayerIndex = 0;
    private boolean isGettingOutOfPenaltyBox;

    public GameBetter() {
        for (int i = 0; i < QUESTION_COUNT; i++) {
            popQuestions.add(createPopQuestion(i));
            scienceQuestions.add(createScienceQuestion(i));
            sportsQuestions.add(createSportsQuestion(i));
            rockQuestions.add(createRockQuestion(i));
        }
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

    private String createPopQuestion(int index) {
        return createQuestion(QuestionCategory.POP, index);
    }

    private String createScienceQuestion(int index) {
        return createQuestion(QuestionCategory.SCIENCE, index);
    }

    private String createSportsQuestion(int index) {
        return createQuestion(QuestionCategory.SPORTS, index);
    }

    private String createRockQuestion(int index) {
        return createQuestion(QuestionCategory.ROCK, index);
    }

    private String createQuestion(QuestionCategory questionCategory, int index) {
        return questionCategory.getValue() + " Question " + index;
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
        if (QuestionCategory.POP.getValue().equals(currentCategory())) {
            System.out.println(popQuestions.removeFirst());
        }
        if (QuestionCategory.SCIENCE.getValue().equals(currentCategory())) {
            System.out.println(scienceQuestions.removeFirst());
        }
        if (QuestionCategory.SPORTS.getValue().equals(currentCategory())) {
            System.out.println(sportsQuestions.removeFirst());
        }
        if (QuestionCategory.ROCK.getValue().equals(currentCategory())) {
            System.out.println(rockQuestions.removeFirst());
        }
    }

    private String currentCategory() {
        int questionOrder = currentPlayer().getPlace() % QuestionCategory.values().length;

        switch (questionOrder) {
            case 0:
                return QuestionCategory.POP.getValue();
            case 1:
                return QuestionCategory.SCIENCE.getValue();
            case 2:
                return QuestionCategory.SPORTS.getValue();
            default:
                return QuestionCategory.ROCK.getValue();
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
        System.out.println("The category is " + currentCategory());
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
