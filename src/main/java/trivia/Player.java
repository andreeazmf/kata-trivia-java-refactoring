package trivia;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class Player {

    private final String name;

    private int place;

    private int coins;

    @Setter
    private boolean isInPenaltyBox;

    public void earnOneCoin() {
        coins++;
    }

    public void move(int distance, int placeCount) {
        int nextPlace = place + distance;
        if (nextPlace > placeCount - 1) {
            nextPlace -= placeCount;
        }
        place = nextPlace;
    }
}
