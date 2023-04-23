package trivia.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import trivia.question.QuestionCategory;

import static trivia.constants.GameConstants.PLACE_COUNT;
import static trivia.object.PlayerMother.player;

class PlayerTest {

    private final Player player = player(1);

    @Test
    void shouldBeInPenaltyBoxAfterTwoWrongAnswersFromSameCategory() {
        player.gaveWrongAnswer();
        player.gaveWrongAnswer();

        Assertions.assertTrue(player.isInPenaltyBox());
    }

    @Test
    void shouldNotBeInPenaltyBoxAfterOneWrongAnswerFromSingleCategory() {
        for (int i = 0; i < QuestionCategory.values().length; i++) {
            player.gaveWrongAnswer();
            player.move(1, PLACE_COUNT);
        }

        Assertions.assertFalse(player.isInPenaltyBox());
    }

    @Test
    void gaveWrongAnswerWhenHasAWinningStreak() {
        player.obtainBenefits();
        player.gaveWrongAnswer();

        Assertions.assertEquals(0, player.getWinningStreak());
        Assertions.assertFalse(player.isInPenaltyBox());
    }

    @Test
    void testObtainBenefits() {
        int expectedCoins = 5;

        player.obtainBenefits();
        player.obtainBenefits();
        player.obtainBenefits();
        player.obtainBenefits();

        Assertions.assertEquals(4, player.getWinningStreak());
        Assertions.assertEquals(expectedCoins, player.getCoins());
    }

    @Test
    void testIsWinner() {
        int expectedCoins = 13;

        player.obtainBenefits();
        player.obtainBenefits();
        player.obtainBenefits();

        player.obtainBenefits();
        player.obtainBenefits();
        player.obtainBenefits();
        player.obtainBenefits();
        player.obtainBenefits();

        Assertions.assertEquals(8, player.getWinningStreak());
        Assertions.assertEquals(expectedCoins, player.getCoins());
        Assertions.assertTrue(player.isWinner());
    }
}
