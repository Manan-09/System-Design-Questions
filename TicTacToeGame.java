import java.util.InputMismatchException;
import java.util.Scanner;

// Enum representing the possible states of a cell in the Tic Tac Toe board
enum CellState {
    EMPTY, X, O
}

// Class representing a move made by a player
class Move {
    private int row;
    private int col;
    private CellState player;

    public Move(int row, int col, CellState player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellState getPlayer() {
        return player;
    }
}

// Class representing the game board
class Board {
    private final int size;
    private CellState[][] cells;
    
    public int getSize() {
        return size;
    }
    
    public CellState getCell(int row, int col) {
        return cells[row][col];
    }

    public Board(int size) {
        this.size = size;
        cells = new CellState[size][size];
        // Initialize the board with EMPTY cells
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = CellState.EMPTY;
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size && cells[row][col] == CellState.EMPTY;
    }

    public void makeMove(Move move) {
        cells[move.getRow()][move.getCol()] = move.getPlayer();
    }

    // Method to print the current state of the board
    public void printBoard() {
        System.out.print(" ");
        for (int j = 0; j < size; j++) {
            System.out.print(" " + j);
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.print(i);
            for (int j = 0; j < size; j++) {
                switch (cells[i][j]) {
                    case X:
                        System.out.print(" X");
                        break;
                    case O:
                        System.out.print(" O");
                        break;
                    case EMPTY:
                        System.out.print(" -");
                        break;
                }
            }
            System.out.println();
        }
    }
    
}

// Class representing a player
class Player {
    private String name;
    private CellState symbol;

    public Player(String name, CellState symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public CellState getSymbol() {
        return symbol;
    }
}

class TicTacToeGame {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    

    public TicTacToeGame(int size, String player1Name, String player2Name) {
        board = new Board(size); // Assuming a standard 3x3 board
        player1 = new Player(player1Name, CellState.X);
        player2 = new Player(player2Name, CellState.O);
        currentPlayer = player1; // Player 1 starts first
    }
    
    // Getters and setters for board
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    // Getters and setters for player1
    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    // Getters and setters for player2
    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    // Getters and setters for currentPlayer
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    // Method to make a move
    public boolean makeMove(int row, int col) {
        if (!board.isValidMove(row, col)) {
            System.out.println("Invalid move! Please try again.");
            return false;
        }
        Move move = new Move(row, col, currentPlayer.getSymbol());
        board.makeMove(move);
        if (isWin(move)) {
            System.out.println(currentPlayer.getName() + " wins!");
            return true;
        } else if (isDraw()) {
            System.out.println("It's a draw!");
            return true;
        }
        switchPlayers();
        return false;
    }

    // Method to check for a win
    private boolean isWin(Move lastMove) {
        int row = lastMove.getRow();
        int col = lastMove.getCol();
        CellState playerSymbol = lastMove.getPlayer();
    
        // Check row
        boolean winInRow = true;
        for (int c = 0; c < board.getSize(); c++) {
            if (board.getCell(row, c) != playerSymbol) {
                winInRow = false;
                break;
            }
        }
        if (winInRow) return true;
    
        // Check column
        boolean winInCol = true;
        for (int r = 0; r < board.getSize(); r++) {
            if (board.getCell(r, col) != playerSymbol) {
                winInCol = false;
                break;
            }
        }
        if (winInCol) return true;
    
        // Check main diagonal
        if (row == col) {
            boolean winInMainDiagonal = true;
            for (int i = 0; i < board.getSize(); i++) {
                if (board.getCell(i, i) != playerSymbol) {
                    winInMainDiagonal = false;
                    break;
                }
            }
            if (winInMainDiagonal) return true;
        }
    
        // Check anti-diagonal
        if (row + col == board.getSize() - 1) {
            boolean winInAntiDiagonal = true;
            for (int i = 0; i < board.getSize(); i++) {
                if (board.getCell(i, board.getSize() - 1 - i) != playerSymbol) {
                    winInAntiDiagonal = false;
                    break;
                }
            }
            if (winInAntiDiagonal) return true;
        }
    
        return false;
    }

    // Method to check for a draw
    private boolean isDraw() {
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                if (board.getCell(r, c) == CellState.EMPTY) {
                    return false; // If there's an empty cell, the game is not a draw
                }
            }
        }
        return true;
    }

    // Method to switch players
    private void switchPlayers() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }
    
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tic Tac Toe!");
        int boardSize = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter board size: ");
                boardSize = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
        System.out.print("Enter player 1 name: ");
        String player1Name = scanner.nextLine();
        System.out.print("Enter player 2 name: ");
        String player2Name = scanner.nextLine();

        TicTacToeGame game = new TicTacToeGame(boardSize, player1Name, player2Name);
        boolean isGameFinished = false;
        while (!isGameFinished) {
            System.out.println(game.getCurrentPlayer().getName() + "'s turn:");
            int row = 0;
            int col = 0;
            validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Enter row number: ");
                    row = scanner.nextInt();
                    System.out.print("Enter column number: ");
                    col = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter integers.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }
            if (!game.getBoard().isValidMove(row, col)) {
                System.out.println("Invalid move!");
            } else {
                isGameFinished = game.makeMove(row, col);
                game.getBoard().printBoard();
            }
        }

        scanner.close();
    }
}
