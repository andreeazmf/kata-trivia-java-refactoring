package trivia;

import trivia.player.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerMother {
    
    static List<Player> players(int count) {
        return IntStream.range(0, count)
                .mapToObj(PlayerMother::player)
                .collect(Collectors.toList());
    }

    private static Player player(int index) {
        return new Player("Player " + index);
    }
}
