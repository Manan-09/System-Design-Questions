import java.util.*;

// Enum representing the game modes
enum GameMode {
    EASY,
    MEDIUM,
    HARD
}

// Represents a player in the game
class Player {
    private String name;
    private int position;

    public Player(String name) {
        this.name = name;
        this.position = 0;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

// Represents a dice
class Dice {
    private Random random;

    public Dice() {
        random = new Random();
    }

    public int roll() {
        return random.nextInt(6) + 1;
    }
}

// Represents the game board
class Board {
    private int size;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;

    public Board(int size, GameMode mode) {
        this.size = size;
        snakes = new HashMap<>();
        ladders = new HashMap<>();
        initializeSnakesAndLadders(mode);
    }
    
    int getBoardSize() {
        return this.size;
    }

    private void initializeSnakesAndLadders(GameMode mode) {
        Random random = new Random();
        Set<Integer> positions = new HashSet<>();
        int numSnakes = 0, numLadders = 0;
        switch (mode) {
            case EASY:
                numSnakes = size / 10; // 1 snake for every 10 squares
                numLadders = size / 10; // 1 ladder for every 10 squares
                break;
            case MEDIUM:
                numSnakes = size / 7; // 1 snake for every 7 squares
                numLadders = size / 7; // 1 ladder for every 7 squares
                break;
            case HARD:
                numSnakes = size / 5; // 1 snake for every 5 squares
                numLadders = size / 5; // 1 ladder for every 5 squares
                break;
        }
        // Place snakes
        while (snakes.size() < numSnakes) {
            int start = random.nextInt(size - 1) + 2;
            int end = random.nextInt(start - 1) + 1;
            if (start != end && !snakes.containsKey(start) && !snakes.containsValue(start) &&
                    !ladders.containsKey(start) && !ladders.containsValue(start) &&
                    !positions.contains(start)) {
                snakes.put(start, end);
                positions.add(start);
            }
        }
        // Place ladders
        while (ladders.size() < numLadders) {
            int start = random.nextInt(size - 1) + 2;
            int end = random.nextInt(size - start + 1) + start;
            if (start != end && !snakes.containsKey(start) && !snakes.containsValue(start) &&
                    !ladders.containsKey(start) && !ladders.containsValue(start) &&
                    !positions.contains(start)) {
                ladders.put(start, end);
                positions.add(start);
            }
        }
    }

    public int adjustPosition(int position) {
        if (snakes.containsKey(position)) {
            System.out.println("Oops! You got bitten by a snake.");
            return snakes.get(position);
        }
        if (ladders.containsKey(position)) {
            System.out.println("Hurray! You climbed a ladder.");
            return ladders.get(position);
        }
        return position;
    }
    
    public void printSnakesAndLadders() {
        System.out.println("Snakes:");
        for (Map.Entry<Integer, Integer> entry : snakes.entrySet()) {
            System.out.println("Start: " + entry.getKey() + " -> End: " + entry.getValue());
        }
        System.out.println("Ladders:");
        for (Map.Entry<Integer, Integer> entry : ladders.entrySet()) {
            System.out.println("Start: " + entry.getKey() + " -> End: " + entry.getValue());
        }
    }
}

// Represents the Snake and Ladder game
class SnakeAndLadderGame {
    private Board board;
    private Player[] players;
    private Dice dice;

    public SnakeAndLadderGame(int boardSize, GameMode mode, String[] playerNames) {
        board = new Board(boardSize, mode);
        dice = new Dice();
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i]);
        }
    }
    
    public void printSnakesAndLadders() {
        this.board.printSnakesAndLadders();
    }

    public void play() {
        int currentPlayer = 0;
        while (true) {
            int diceRoll = dice.roll();
            Player player = players[currentPlayer];
            System.out.println(player.getName() + " rolls the dice and gets " + diceRoll);
            int newPosition = player.getPosition() + diceRoll;
            if (newPosition <= board.getBoardSize()) {
                newPosition = board.adjustPosition(newPosition);
                player.setPosition(newPosition);
            } else {
                System.out.println("Oops! " + player.getName() + " cannot move beyond the board.");
            }
            System.out.println(player.getName() + " is at position " + player.getPosition());
            if (player.getPosition() == board.getBoardSize()) {
                System.out.println(player.getName() + " wins!");
                break;
            }
            currentPlayer = (currentPlayer + 1) % players.length;
        }
    }

}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the size of the board: ");
        int boardSize = scanner.nextInt();
        System.out.println("Choose game mode:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        int modeInput = scanner.nextInt();
        GameMode mode;
        switch (modeInput) {
            case 1:
                mode = GameMode.EASY;
                break;
            case 2:
                mode = GameMode.MEDIUM;
                break;
            case 3:
                mode = GameMode.HARD;
                break;
            default:
                mode = GameMode.EASY;
                break;
        }
        
        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();
        String[] playerNames = new String[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name of player " + (i + 1) + ": ");
            playerNames[i] = scanner.next();
        }
        // Create a new game with specified board size, mode, and players
        SnakeAndLadderGame game = new SnakeAndLadderGame(boardSize, mode, playerNames);
        game.printSnakesAndLadders();
        // Start the game
        game.play();
    }
}
