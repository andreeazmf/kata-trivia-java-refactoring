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
    List<String> players = new ArrayList<>();
    int[] places = new int[MAX_PLAYER_COUNT];
    int[] purses = new int[MAX_PLAYER_COUNT];
    boolean[] inPenaltyBox = new boolean[MAX_PLAYER_COUNT];

    LinkedList<String> popQuestions = new LinkedList<>();
    LinkedList<String> scienceQuestions = new LinkedList<>();
    LinkedList<String> sportsQuestions = new LinkedList<>();
    LinkedList<String> rockQuestions = new LinkedList<>();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public GameBetter() {
        for (int i = 0; i < QUESTION_COUNT; i++) {
            popQuestions.add(createPopQuestion(i));
            scienceQuestions.add(createScienceQuestion(i));
            sportsQuestions.add(createSportsQuestion(i));
            rockQuestions.add(createRockQuestion(i));
        }
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

    public String createRockQuestion(int index) {
        return createQuestion(QuestionCategory.ROCK, index);
    }

    public String createQuestion(QuestionCategory questionCategory, int index) {
        return questionCategory.getValue() + " Question " + index;
    }

    public boolean isPlayable() {
        return (howManyPlayers() >= MIN_PLAYER_COUNT);
    }

    public boolean add(String playerName) {
        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

        logNewPlayer(playerName);

        return true;
    }

    private void logNewPlayer(String playerName) {
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    public int howManyPlayers() {
        return players.size();
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

    private void executeRoll(int roll) {
        moveCurrentPlayer(roll);
        askQuestion();
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

    private void logCurrentPlayerCoins() {
        System.out.println(players.get(currentPlayer)
                + " now has "
                + purses[currentPlayer]
                + " Gold Coins.");
    }

    private void logCorrectAnswer() {
        System.out.println("Answer was correct!!!!");
    }

    private void goToNextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) {
            currentPlayer = 0;
        }
    }

    public boolean wrongAnswer() {
        logWrongAnswer();
        logPlayerSentToPentaltyBox();

        inPenaltyBox[currentPlayer] = true;

        goToNextPlayer();

        return true;
    }

    private void logPlayerSentToPentaltyBox() {
        System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
    }

    private void logWrongAnswer() {
        System.out.println("Question was incorrectly answered");
    }

    private boolean isNotWinner() {
        return !isWinner();
    }

    private boolean isWinner() {
        return purses[currentPlayer] == WINNING_SCORE;
    }
}
