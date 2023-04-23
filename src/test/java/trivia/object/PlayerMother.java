package trivia.object;

import trivia.player.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerMother {

    public static List<Player> players(int count) {
        return IntStream.range(0, count)
                .mapToObj(PlayerMother::player)
                .collect(Collectors.toList());
    }

    public static List<String> playerNames(int count) {
        return IntStream.range(0, count)
                .mapToObj(PlayerMother::playerName)
                .collect(Collectors.toList());
    }

    public static String playerName(int index) {
        return "Player " + index;
    }

    public static Player player(int index) {
        return new Player(playerName(index));
    }
}
