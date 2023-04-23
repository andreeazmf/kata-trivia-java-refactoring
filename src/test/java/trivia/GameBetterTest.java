package trivia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import trivia.player.Player;

import java.util.List;
import java.util.stream.Stream;

import static trivia.PlayerMother.players;

class GameBetterTest {

    private GameBetter gameBetter;

    @BeforeEach
    public void setUp() {
        gameBetter = new GameBetter();
    }

    @Test
    void shouldNotAddMoreThan6Players() {
        players(6).forEach(player -> gameBetter.add(player.getName()));
        Assertions.assertFalse(gameBetter.add(""));
    }

    @Test
    void shouldNotAddAfterStart() {
        players(4).forEach(player -> gameBetter.add(player.getName()));
        gameBetter.start();
        Assertions.assertFalse(gameBetter.add(""));
    }

    @ParameterizedTest
    @MethodSource("isPlayableProvider")
    void start(List<Player> players, boolean expected) {
        players.forEach(player -> gameBetter.add(player.getName()));
        Assertions.assertEquals(expected, gameBetter.start());
    }

    @ParameterizedTest
    @MethodSource("isPlayableProvider")
    void isPlayable(List<Player> players, boolean expected) {
        players.forEach(player -> gameBetter.add(player.getName()));
        Assertions.assertEquals(expected, gameBetter.isPlayable());
    }

    private static Stream<Arguments> isPlayableProvider() {
        return Stream.of(
                Arguments.of(players(1), false),
                Arguments.of(players(2), true),
                Arguments.of(players(6), true)
        );
    }
}
