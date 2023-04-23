# Trivia Refactoring Kata

## Problems Found

### 1. Typo: "corre(n)t" instead of "corre(c)t"

This typo made it hard to extract a method for logging the correct answer.\
It was used in 2 places, but once with a typo and once without it.

```java
public class Game {
    // ...
    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                // ...
            }
            // ...
        } else {
            System.out.println("Answer was corrent!!!!");
            // ...
        }
        // ...
    }
    // ...
}
```

### 2. Misleading negative logic for winner

Looking at the `didPlayerWin()` method, it checks if the current player has 6 coins in his/her purse
– which seems like the winning condition; but then it _negates_ the result.

It actually checks if the player _did not_ win.

```java
public class Game {
    // ...
    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
    // ...
}
```

Looking at the `wasCorrectlyAnswered()` method, it seems that the return values are contradictory:

- if the player is getting out of the penalty box, it checks whether he/she
  _is the winner_
- if not, it returns _true_; which based on the previous branch would mean that he/she _is the
  winner_ – but a player still in the penalty box _cannot be a winner_

```java
public class Game {
    // ...
    public boolean wasCorrectlyAnswered() {
        // ...
        if (isGettingOutOfPenaltyBox) {
            // ...
            boolean winner = didPlayerWin();
            // ...
            return winner;
        } else {
            // ...
            return true;
        }
        // ...
    }
    // ...
}
```

Looking into the `PlayGame` class, we can see that the `wasCorreclyAnswered()`
call is expected to return whether the current player _was not a winner_.

```java
public class PlayGame {
    // ...
    public static void main(String[] args) {
        // ...
        notAWinner = aGame.wasCorrectlyAnswered();
        // ...
    }
    // ...
}
```

The gameplay is fine because in the flow of `main(...) -> wasCorrectlyAnswered() -> didPlayerWin()`:

- `didPlayerWin()` returns if the player did not win
- `wasCorrectlyAnswered()` returns the previous method, so again if whether the player did not win
- `main(...)` expects to see if the player did not win

### 3. Wrong array accessing

It happens for all primitive arrays: `int[] places`, `int[] purses`, `boolean[] inPenaltyBox`.

After a new player is added, the size of the ArrayList is used for indexing the other arrays.

As consequences:

- the 1st player is going to use index 1 - which means the 2nd element in the array;
- the 5th player is going to use index 5 - which means the 6th element in the array;
- the 6th player is going to use index 6 - which means the 7th element in the array, so it will
  throw an `ArrayIndexOutOfBoundsException`.

The arrays could fit data for 6 players, but they actually cannot because of accessing the memory
wrong.

(And actually, that initialization of primitives is redundant.)

```java
public class Game {
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

    // ...

    public boolean add(String playerName) {
        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;
        //...
    }

    // ...

    public int howManyPlayers() {
        return players.size();
    }

    // ...
}
```

During gameplay, we cannot encounter an index out of bounds exception, because it checks for maximum
4 players – so it could only reach index 5.

However, if the Game class is used anywhere else, there is no guarantee that there will be maximum 5
players added.

### 4. No number of players checking in the `Game` class

#### 4.1. More than 6 players

Regarding the previous problem, even if the arrays would be accessed properly, there is no condition
to the number of players added, and we could still get an
`ArrayIndexOutOfBounds`.

The `add()` method could be called 7 times, the `players` ArrayList would contain 7 elements - and
it would try to access the 7th element of the `places` array, which has enough memory only for 6
elements.

#### 4.2. Less than 2 players

There is the `isPlayable()` method, but it is not used anywhere - not even in the
`PlayGame` class.

Actually, `PlayGame` checks the number of players, but it lets the user use only 1 player.

### 5. Gameplay: player does not get out of penalty box

If a player is in the penalty box and rolls an odd number, he/she can get out of the penalty box.

The status of `isGettingOutOfPenaltyBox` is set to `true`, but the value in `inPenaltyBox` is not
updated - so the player is actually still in the penalty box.

In the test, player Sue has this behaviour:

```
...
Sue is the current player
They have rolled a 3
Sue is getting out of the penalty box
Sue's new location is 1
The category is Science
Science Question 3
Answer was correct!!!!
Sue now has 2 Gold Coins.
...
Sue is the current player
They have rolled a 1
Sue is getting out of the penalty box
...
```

### 6. OOP-related problems

- Encapsulation
    - all fields could be private (and some final);
    - some methods could be private
        - `public String createRockQuestion(int index)` – used only internally
        - `public int howManyPlayers()` – if we do not want to reveal such info outside
- Raw use of parameterized classes

... and so on :cherry_blossom:
