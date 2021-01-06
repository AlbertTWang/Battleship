import java.util.Stack;

import javax.swing.JOptionPane;

public class Battleship {
    private int[][] board1Hits;
    private int[][] board2Hits;
    private int[][] board1Ships;
    private int[][] board2Ships;
    private int numTurns;
    private boolean player1;
    private boolean gameOver;
    private int settingWhichShipPlayer1;
    private int settingWhichShipPlayer2;
    private boolean settingPlayer;
    private int size;
    private int numShips;
    private boolean settingComplete;
    private Boat[] player1Ships;
    private Boat[] player2Ships;
    private Stack<int[]> player1Moves;
    private Stack<int[]> player2Moves;
    
    public static final String IRONCLAD_IMG = "files/ironclad.jpg";
    public static final String SUBMARINE_IMG = "files/submarine.png";
    public static final String DESTROYER_IMG = "files/destroyer.jpg";
    public static final String CRUISER_IMG = "files/cruiser.jpeg";
    public static final String CARRIER_IMG = "files/carrier.jpg";
    
    public Battleship(int size, int numShips) {
        this.size = size;
        this.numShips = numShips;
        reset(size, numShips);
    }
    
    public void reset(int newSize, int newNumShips) {
        if (newSize <= 0 || newSize > 10 || newNumShips <= 0 || newNumShips > 5
                || (newNumShips * (newNumShips + 1) / 2) > newSize * newSize) {
            System.out.println("Invalid parameters");
            return;
        }
        size = newSize;
        board1Hits = new int[size][size];
        board2Hits = new int[size][size];
        board1Ships = new int[size][size];
        board2Ships = new int[size][size];
        numShips = newNumShips;
        numTurns = 1;
        player1 = true;
        gameOver = false;
        settingWhichShipPlayer1 = 0;
        settingWhichShipPlayer2 = 0;
        settingPlayer = true;
        settingComplete = false;
        player1Ships = new Boat[]
            {new Ironclad(this, 1, 1, 1, IRONCLAD_IMG, "Ironclad"),
                new Submarine(this, 1, 2, 2, SUBMARINE_IMG, "Submarine"),
                new Destroyer(this, 1, 3, 1, DESTROYER_IMG, "Destroyer"),
                new Cruiser(this, 1, 4, 1, CRUISER_IMG, "Cruiser"),
                new Carrier(this, 1, 5, 1, CARRIER_IMG, "Carrier")};
        player2Ships = new Boat[]
            {new Ironclad(this, 2, 1, 1, IRONCLAD_IMG, "Ironclad"),
                new Submarine(this, 2, 2, 2, SUBMARINE_IMG, "Submarine"),
                new Destroyer(this, 2, 3, 1, DESTROYER_IMG, "Destroyer"),
                new Cruiser(this, 2, 4, 1, CRUISER_IMG, "Cruiser"),
                new Carrier(this, 2, 5, 1, CARRIER_IMG, "Carrier")};
        player1Moves = new Stack<>();
        player2Moves = new Stack<>();
    }
    
    public void undo() {
        if (player1Moves.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No Moves to Undo!","Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (player1Moves.size() != player2Moves.size()) {
            JOptionPane.showMessageDialog(null,
                    "No Moves to Undo!","Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int[] point1 = player1Moves.pop();
        int[] point2 = player2Moves.pop();
        board1Hits[point2[0]][point2[1]]--;
        board2Hits[point1[0]][point1[1]]--;
        numTurns--;
        for (Boat b : player1Ships) {
            for (int[] square : b.getSquares()) {
                if (square[0] == point2[0] && square[1] == point2[1]) {
                    b.resetSunk();
                }
            }
        }
        for (Boat b : player2Ships) {
            for (int[] square : b.getSquares()) {
                if (square[0] == point1[0] && square[1] == point1[1]) {
                    b.resetSunk();
                }
            }
        }
        
    }
    
    public boolean setShips(int r1, int c1, int r2, int c2) {
        if (r1 < 0 || r1 >= size || c1 < 0 || c1 >= size ||
                r2 < 0 || r2 >= size || c2 < 0 || c2 >= size || settingComplete) {
            System.out.println("Invalid Ship Setting: Out of Bounds");
            return false;
        }
        if (settingPlayer) {
            if (!((r1 == r2 && Math.abs(c2 - c1) == settingWhichShipPlayer1) ||
                    (Math.abs(r2 - r1) == settingWhichShipPlayer1 && c1 == c2))) {
                System.out.println("Invalid Ship Setting: Not same line");
                return false;
            } else {
                if (Math.abs(r2 - r1) != 0) {
                    int max = Math.max(r1, r2);
                    int min = Math.min(r1, r2);
                    for (int i = min; i <= max; i++) {
                        if (board1Ships[i][c1] != 0) { // Overlaps w/ previous ship
                            return false;
                        }
                    }
                    for (int i = min; i <= max; i++) {
                        player1Ships[settingWhichShipPlayer1].setSquares(new int[] {i, c1});
                        board1Ships[i][c1] = settingWhichShipPlayer1 + 1;
                        System.out.println("Ship set at (" + i + ", " + c1 + ")");
                    }
                } else {
                    int max = Math.max(c1, c2);
                    int min = Math.min(c1, c2);
                    for (int i = min; i <= max; i++) {
                        if (board1Ships[r1][i] != 0) {
                            return false;
                        }
                    }
                    for (int i = min; i <= max; i++) {
                        player1Ships[settingWhichShipPlayer1].setSquares(new int[] {r1, i});
                        board1Ships[r1][i] = settingWhichShipPlayer1 + 1;
                        System.out.println("Ship set at (" + r1 + ", " + i + ")");
                    }
                }
                player1Ships[settingWhichShipPlayer1].setPlaced(true);
            }
            settingWhichShipPlayer1++;
            if (settingWhichShipPlayer1 == numShips) {
                settingPlayer = !settingPlayer;
            }
        } else {
            if (!((r1 == r2 && Math.abs(c2 - c1) == settingWhichShipPlayer2) ||
                    (Math.abs(r2 - r1) == settingWhichShipPlayer2 && c1 == c2))) {
                System.out.println("Invalid Ship Setting: Not same line");
                return false;
            } else {
                if (Math.abs(r2 - r1) != 0) {
                    int max = Math.max(r1, r2);
                    int min = Math.min(r1, r2);
                    for (int i = min; i <= max; i++) {
                        if (board2Ships[i][c1] != 0) {
                            return false;
                        }
                    }
                    for (int i = min; i <= max; i++) {
                        player2Ships[settingWhichShipPlayer2].setSquares(new int[] {i, c1});
                        board2Ships[i][c1] = settingWhichShipPlayer2 + 1;
                        System.out.println("CPU Ship set at (" + i + ", " + c1 + ")");
                    }
                } else {
                    int max = Math.max(c1, c2);
                    int min = Math.min(c1, c2);
                    for (int i = min; i <= max; i++) {
                        if (board2Ships[r1][i] != 0) {
                            return false;
                        }
                    }
                    for (int i = min; i <= max; i++) {
                        player2Ships[settingWhichShipPlayer2].setSquares(new int[] {r1, i});
                        board2Ships[r1][i] = settingWhichShipPlayer2 + 1;
                        System.out.println("CPU Ship set at (" + r1 + ", " + i + ")");
                    }
                }
                player2Ships[settingWhichShipPlayer2].setPlaced(true);
            }
            settingWhichShipPlayer2++;
            if (settingWhichShipPlayer2 == numShips) {
                settingPlayer = !settingPlayer;
                settingComplete = true;
            }
        }
        return true;
    }
    
    public boolean playTurn(int r, int c) {
        if (r < 0 || r >= size || c < 0 || c >= size || !settingComplete || gameOver) {
            return false;
        }
        if (player1) {
            if (board2Ships[r][c] != 0) {
                if (player2Ships[board2Ships[r][c] - 1].isSunk()) {
                    return false;
                }
            }
            board2Hits[r][c]++;
            if (board2Ships[r][c] != 0) {
                JOptionPane.showMessageDialog(null,
                        "Hit!","Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            player1Moves.push(new int[] {r, c});
            numTurns++;
        } else {
            board1Hits[r][c]++;
            if (board1Ships[r][c] != 0) {
                JOptionPane.showMessageDialog(null,
                        "Your Ship was Hit!","Message",
                        JOptionPane.WARNING_MESSAGE);
            }
            player2Moves.push(new int[] {r, c});
        }
        
        if (checkWinner() == 0) {
            player1 = !player1;
        }
        return true;
    }
    
    public void playTurn() {
        int x = (int) (Math.random() * 10);
        int y = (int) (Math.random() * 10);
        board1Hits[x][y]++;
        if (board1Ships[x][y] != 0) {
            JOptionPane.showMessageDialog(null,
                    "Your Ship was Hit!","Message",
                    JOptionPane.WARNING_MESSAGE);
        }
        numTurns++;
    }
    
    public void hitSquare(int x, int y) {
        board2Hits[x][y]++;
        if (board2Ships[x][y] != 0) {
            JOptionPane.showMessageDialog(null,
                    "Hit!","Message",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public int checkWinner() {
        boolean player1Dub = true;
        boolean player2Dub = true;
        int count = 1;
        for (Boat b : player1Ships) {
            if (count > numShips) {
                break;
            }
            if (!b.isSunk()) {
                player2Dub = false;
            }
            count++;
        }
        count = 1;
        for (Boat b : player2Ships) {
            if (count > numShips) {
                break;
            }
            if (!b.isSunk()) {
                player1Dub = false;
            }
            count++;
        }
        if (player1Dub) {
            gameOver = true;
            return 1;
        }
        if (player2Dub) {
            gameOver = true;
            return 2;
        }
        if (numTurns > 200) {
            gameOver = true;
            return 3;
        }
        return 0;
    }
    
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        System.out.println("Player 1 ships");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board1Ships[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Player 1 hits");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board2Hits[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Player 2 ships");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board2Ships[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Player 2 hits");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board1Hits[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public int getTurns() {
        return numTurns;
    }
    
    public boolean getCurrentPlayer() {
        return player1;
    }
    
    public boolean getSettingPlayer() {
        return settingPlayer;
    }
    
    public int getSettingWhichShipPlayer1() {
        return settingWhichShipPlayer1;
    }
      
    public int getSettingWhichShipPlayer2() {
        return settingWhichShipPlayer2;
    }
    
    public boolean getSettingComplete() {
        return settingComplete;
    }
    
    public boolean getGameOver() {
        return gameOver;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getNumShips() {
        return numShips;
    }
    
    public int getBoard1Hit(int r, int c) {
        return board1Hits[r][c];
    }
    
    public int getBoard2Hit(int r, int c) {
        return board2Hits[r][c];
    }
    
    public int getBoard1Ship(int r, int c) {
        return board1Ships[r][c];
    }
    
    public int getBoard2Ship(int r, int c) {
        return board2Ships[r][c];
    }
    
    public Boat[] getPlayer1Ships() {
        return player1Ships.clone();
    }
    
    public Boat[] getPlayer2Ships() {
        return player2Ships.clone();
    }
    
    public static void main(String[] args) {
        Battleship b = new Battleship(3, 2);
        
        b.setShips(0, 0, 0, 0);
        b.printGameState();
        
        b.setShips(1, 1, 2, 1);
        b.printGameState();
        
        b.setShips(0, 0, 0, 0);
        b.printGameState();
        
        b.setShips(2, 1, 2, 2);
        b.printGameState();
        
        b.playTurn(0, 0);
        b.printGameState();
        
        b.playTurn(0, 0);
        b.printGameState();
        
        b.playTurn(2, 1);
        b.printGameState();
        
        b.playTurn(1, 1);
        b.printGameState();
        
        b.playTurn(2, 2);
        b.printGameState();
        
        System.out.println();
        System.out.println();
        System.out.println("Winner is: " + b.checkWinner());
    }
}
