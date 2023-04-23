package trivia.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static trivia.PlayerMother.playerName;
import static trivia.PlayerMother.playerNames;
import static trivia.constants.GameConstants.MAX_PLAYER_COUNT;
import static trivia.constants.GameConstants.PLACE_COUNT;

class PlayerManagerTest {

    private final PlayerManager playerManager = PlayerManager.init(MAX_PLAYER_COUNT, PLACE_COUNT);

    @Test
    void shouldNotAddPlayerIfNameExists() {
        playerManager.add(playerName(0));

        Assertions.assertFalse(playerManager.add(playerName(0)));
        Assertions.assertEquals(1, playerManager.howManyPlayers());
    }

    @Test
    void shouldNotAddMoreThan6Players() {
        playerNames(6).forEach(playerManager::add);

        Assertions.assertFalse(playerManager.add(""));
        Assertions.assertEquals(6, playerManager.howManyPlayers());
    }
}
