package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static trivia.constants.GameConstants.MAX_PLAYER_COUNT;
import static trivia.constants.GameConstants.MIN_PLAYER_COUNT;
import static trivia.constants.GameConstants.PLACE_COUNT;
import static trivia.constants.GameConstants.QUESTION_COUNT;
import static trivia.constants.GameConstants.WINNING_SCORE;

// REFACTOR ME
public class GameBetter implements IGame {

    private final List<String> players = new ArrayList<>();
    private final int[] places = new int[MAX_PLAYER_COUNT];
    private final int[] purses = new int[MAX_PLAYER_COUNT];
    private final boolean[] inPenaltyBox = new boolean[MAX_PLAYER_COUNT];

    private final LinkedList<String> popQuestions = new LinkedList<>();
    private final LinkedList<String> scienceQuestions = new LinkedList<>();
    private final LinkedList<String> sportsQuestions = new LinkedList<>();
    private final LinkedList<String> rockQuestions = new LinkedList<>();

    private int currentPlayer = 0;
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
        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

        logNewPlayer(playerName);

        return true;
    }

    public void roll(int roll) {
        logCurrentPlayer();
        logRoll(roll);

        if (inPenaltyBox[currentPlayer]) {
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
        if (inPenaltyBox[currentPlayer] && !isGettingOutOfPenaltyBox) {
            goToNextPlayer();
            return true;
        }

        logCorrectAnswer();

        purses[currentPlayer]++;
        logCurrentPlayerCoins();

        boolean isNotWinner = isNotWinner();

        goToNextPlayer();

        return isNotWinner;
    }

    public boolean wrongAnswer() {
        logWrongAnswer();
        logPlayerSentToPenaltyBox();

        inPenaltyBox[currentPlayer] = true;

        goToNextPlayer();

        return true;
    }

    public boolean isPlayable() {
        return (howManyPlayers() >= MIN_PLAYER_COUNT);
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
        places[currentPlayer] = places[currentPlayer] + roll;
        if (places[currentPlayer] > PLACE_COUNT - 1)
            places[currentPlayer] = places[currentPlayer] - PLACE_COUNT;

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
        int questionOrder = places[currentPlayer] % QuestionCategory.values().length;

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
        currentPlayer++;
        if (currentPlayer == players.size()) {
            currentPlayer = 0;
        }
    }

    private boolean isNotWinner() {
        return !isWinner();
    }

    private boolean isWinner() {
        return purses[currentPlayer] == WINNING_SCORE;
    }

    private void logNewPlayer(String playerName) {
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    private void logCurrentPlayer() {
        System.out.println(players.get(currentPlayer) + " is the current player");
    }

    private void logRoll(int roll) {
        System.out.println("They have rolled a " + roll);
    }

    private void logGettingOutOfPenaltyBox() {
        System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
    }

    private void logNotGettingOutOfPenaltyBox() {
        System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
    }

    private void logNewLocation() {
        System.out.println(players.get(currentPlayer)
                + "'s new location is "
                + places[currentPlayer]);
    }

    private void logCurrentCategory() {
        System.out.println("The category is " + currentCategory());
    }

    private void logCurrentPlayerCoins() {
        System.out.println(players.get(currentPlayer)
                + " now has "
                + purses[currentPlayer]
                + " Gold Coins.");
    }

    private void logCorrectAnswer() {
        System.out.println("Answer was correct!!!!");
    }

    private void logPlayerSentToPenaltyBox() {
        System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
    }

    private void logWrongAnswer() {
        System.out.println("Question was incorrectly answered");
    }
}
