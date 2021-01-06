import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BattleshipTest {

    @Test
    public void testSetShips() {
        Battleship b = new Battleship(3, 2);
        // Checks for out of bounds and overlap, same column/row
        assertFalse(b.setShips(0, 0, 0, 5));
        assertFalse(b.setShips(0, 0, 5, 0));
        assertFalse(b.setShips(0, 5, 0, 0));
        assertFalse(b.setShips(5, 0, 0, 0));
        assertFalse(b.setShips(5, 5, -1, -1));
        assertFalse(b.setShips(0, 0, 1, 1));
        assertFalse(b.setShips(0, 0, 2, 0));
        assertFalse(b.setShips(0, 0, 1, 0));
        assertTrue(b.setShips(0, 0, 0, 0));
        assertFalse(b.setShips(0, 0, 0, 0));
        assertFalse(b.setShips(0, 0, 1, 1));
        assertTrue(b.setShips(1, 1, 2, 1));
        
        assertFalse(b.setShips(2, 0, 2, 2));
        assertTrue(b.setShips(0, 0, 0, 0));
        assertTrue(b.setShips(2, 1, 2, 2));
        assertFalse(b.setShips(0, 2, 2, 2));
        
        b.reset(3, 2);
        assertTrue(b.setShips(0, 0, 0, 0));
        assertFalse(b.setShips(0, 0, 1, 1));
    }
    
    @Test
    public void testPlayTurn() {
        Battleship b = new Battleship(3, 2);
        // Checks out of bounds and edge cases.
        assertFalse(b.playTurn(0,0));
        assertFalse(b.playTurn(5,5));
        
        b.setShips(0, 0, 0, 0);
        b.setShips(1, 1, 1, 2);
        b.setShips(2, 2, 2, 2);
        b.setShips(1, 1, 2, 1);
        
        assertFalse(b.playTurn(5, 5));
        assertFalse(b.playTurn(5, 0));
        assertFalse(b.playTurn(0, 5));
        
        assertTrue(b.playTurn(0, 0));
        assertTrue(b.playTurn(0, 0));
        
        assertFalse(b.playTurn(5, 5));
        
        assertTrue(b.playTurn(2, 2));
        assertTrue(b.playTurn(1, 1));
        assertTrue(b.playTurn(2, 1));
        assertTrue(b.playTurn(1, 2));
        assertTrue(b.playTurn(0, 2));
        assertTrue(b.playTurn(2, 0)); //Checks can play on same square multiple times.
        assertTrue(b.playTurn(2, 0));
        assertTrue(b.playTurn(2, 0));
        assertTrue(b.playTurn(1, 1));
        assertTrue(b.playTurn(2, 0));
        assertTrue(b.playTurn(1, 1));
        assertTrue(b.playTurn(1, 1));
        assertTrue(b.playTurn(2, 1)); // Wins the game for Player 1
        
        assertEquals(b.checkWinner(), 1);
        assertFalse(b.playTurn(1, 1)); // All remaining playTurns should be false
        assertFalse(b.playTurn(0, 1));
        assertFalse(b.playTurn(2, 1));
        assertFalse(b.playTurn(2, 2));
        
        b.reset(3, 2);
        assertFalse(b.playTurn(0, 0));
    }
    
    @Test
    public void testCheckWinner() {
        Battleship b = new Battleship(3, 2);
      // Checks for winner
        assertEquals(b.checkWinner(), 0);
      
        b.setShips(0, 0, 0, 0);
        b.setShips(1, 1, 1, 2);
        b.setShips(2, 2, 2, 2);
        b.setShips(1, 1, 2, 1);
      
        b.playTurn(2, 2);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(0, 0);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(1, 1);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(1, 1);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(2, 1);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(1, 2);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(1, 1);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(1, 1);
        assertEquals(b.checkWinner(), 0);
        b.playTurn(2, 1);
        b.printGameState();
        assertEquals(b.checkWinner(), 1);
        b.reset(3, 2);
        assertEquals(b.checkWinner(), 0);
    }
    
    @Test
    public void testCheckWinnerEdgeCase() {
        // Checks for 200 turn edge case
        Battleship b = new Battleship(3, 2);
        
        b.setShips(0, 0, 0, 0);
        b.setShips(1, 1, 1, 2);
        b.setShips(2, 2, 2, 2);
        b.setShips(1, 1, 2, 1);
        for (int i = 0; i < 200; i++) {
            b.playTurn(0, 0);
            b.playTurn(2, 2);
        }
        assertEquals(b.checkWinner(), 3);
        b.reset(3, 2);
        assertEquals(b.checkWinner(), 0);
    }
    
    @Test
    public void testGameOver() {
        // Checks for game over and various edge cases.
        Battleship b = new Battleship(3, 2);
        
        assertFalse(b.getGameOver());
        b.setShips(0, 0, 0, 0);
        b.setShips(1, 1, 1, 2);
        b.setShips(2, 2, 2, 2);
        b.setShips(1, 1, 2, 1);
        
        assertFalse(b.getGameOver());
        
        b.playTurn(2,2);
        assertFalse(b.getGameOver());
        b.playTurn(0,0);
        assertFalse(b.getGameOver());
        b.playTurn(1,1);
        assertFalse(b.getGameOver());
        b.playTurn(1,1);
        assertFalse(b.getGameOver());
        b.playTurn(1,1);
        assertFalse(b.getGameOver());
        b.playTurn(1,1);
        assertFalse(b.getGameOver());
        b.playTurn(2,1);
        assertFalse(b.getGameOver());
        b.playTurn(1,2);
        assertFalse(b.getGameOver());
        b.playTurn(2,1);
        assertTrue(b.getGameOver());
        b.playTurn(1,2);
        assertTrue(b.getGameOver());
    
        b.reset(3, 2);
        assertFalse(b.getGameOver());
    }
    
    @Test
    public void testGetTurns() {
        Battleship b = new Battleship(3, 2);
        assertEquals(1, b.getTurns());
        b.setShips(0, 0, 0, 0);
        b.setShips(1, 1, 2, 1);
        b.setShips(2, 2, 2, 2);
        b.setShips(1, 1, 2, 1);
        
        assertEquals(1, b.getTurns());
        
        b.playTurn(0, 0);
        assertEquals(2, b.getTurns());
        
        b.playTurn(1, 0);
        assertEquals(2, b.getTurns());
        
        b.playTurn(1, 0);
        assertEquals(3, b.getTurns());
        
        b.playTurn(1, 0);
        assertEquals(3, b.getTurns());
        
        b.playTurn(1, 0);
        assertEquals(4, b.getTurns());
        
        b.playTurn(1, 0);
        assertEquals(4, b.getTurns());
        
        b.reset(3, 2);
        assertEquals(1, b.getTurns());
    }
    
    @Test
    public void testGetSettingComplete() {
        //Checks for settingComplete
        Battleship b = new Battleship(3, 2);
        assertFalse(b.getSettingComplete());
        b.setShips(0, 0, 0, 0);
        assertFalse(b.getSettingComplete());
        b.setShips(1, 1, 2, 1);
        assertFalse(b.getSettingComplete());
        b.setShips(2, 2, 2, 2);
        assertFalse(b.getSettingComplete());
        b.setShips(1, 1, 2, 1);
        assertTrue(b.getSettingComplete());
        
        b.reset(5, 3);
        assertFalse(b.getSettingComplete());
        b.setShips(0, 0, 0, 0);
        assertFalse(b.getSettingComplete());
        b.setShips(1, 1, 2, 1);
        assertFalse(b.getSettingComplete());
        b.setShips(2, 2, 4, 2);
        assertFalse(b.getSettingComplete());
        b.setShips(4, 4, 4, 4);
        assertFalse(b.getSettingComplete());
        b.setShips(3, 3, 2, 3);
        assertFalse(b.getSettingComplete());
        b.setShips(2, 2, 0, 2);
        assertTrue(b.getSettingComplete());
    }
    
//    @Test
//    public void testConstructor() {
//        Battleship b = new Battleship(3, 2);
//        
//        Battleship b1 = new Battleship(10, 5);  // Check to see if console prints "Invalid Params"
//        
//        Battleship b2 = new Battleship(-1, 2);
//        
//        Battleship b3 = new Battleship(0, 0);
//        
//        Battleship b4 = new Battleship(3, 6);
//        
//        Battleship b5 = new Battleship(2, 5);
//        
//        //Should print 4 times for b2, b3, b4, b5.
//    }
    
    @Test
    public void testUndo() {
        Battleship b = new Battleship(3, 2);
        b.undo(); // Error message should pop up
        b.setShips(0, 0, 0, 0);
        b.setShips(1, 1, 2, 1);
        b.setShips(2, 2, 2, 2);
        b.setShips(1, 1, 1, 2);
        
        b.undo(); // Error message should pop up
        b.playTurn(2, 2);
        assertEquals(b.getBoard2Hit(2, 2), 1);
        b.undo(); // Error message should pop up
        b.playTurn(0, 0);
        assertEquals(b.getBoard1Hit(0, 0), 1);
        b.undo();
        assertEquals(b.getBoard2Hit(2, 2), 0);
        assertEquals(b.getBoard1Hit(0, 0), 0);
        b.playTurn(2, 2);
        assertEquals(b.getBoard2Hit(2, 2), 1);
        
        b.reset(5, 3);
        b.undo(); // Error message should pop up
    }
    
    @Test
    public void testWhichShipAndPlayer() {
        Battleship b = new Battleship(3, 2);
        assertEquals(b.getSettingWhichShipPlayer1(), 0);
        assertTrue(b.getSettingPlayer());
        b.setShips(0, 0, 0, 0);
        assertEquals(b.getSettingWhichShipPlayer1(), 1);
        assertTrue(b.getSettingPlayer());
        b.setShips(1, 1, 2, 1);
        assertEquals(b.getSettingWhichShipPlayer1(), 2);
        assertEquals(b.getSettingWhichShipPlayer2(), 0);
        assertFalse(b.getSettingPlayer());
        b.setShips(2, 2, 2, 2);
        assertEquals(b.getSettingWhichShipPlayer1(), 2);
        assertEquals(b.getSettingWhichShipPlayer2(), 1);
        assertFalse(b.getSettingPlayer());
        b.setShips(1, 1, 1, 2);
        assertEquals(b.getSettingWhichShipPlayer1(), 2);
        assertEquals(b.getSettingWhichShipPlayer2(), 2);
        assertTrue(b.getSettingPlayer());
        
        b.reset(5, 3);
        assertEquals(b.getSettingWhichShipPlayer1(), 0);
        assertEquals(b.getSettingWhichShipPlayer2(), 0);
        assertTrue(b.getSettingPlayer());
    }
    
    @Test
    public void testGetSize() {
        // Test for size
        Battleship b = new Battleship(3, 2);
        assertEquals(b.getSize(), 3);
        
        Battleship b1 = new Battleship(5, 2);
        assertEquals(b1.getSize(), 5);
        
        b.reset(10, 5);
        assertEquals(b.getSize(), 10);
        
        b1.reset(8, 4);
        assertEquals(b1.getSize(), 8);
        
        b.reset(20, 5);
        assertEquals(b.getSize(), 10);
        
        b1.reset(5, 6);
        assertEquals(b1.getSize(), 8);
    }

    @Test
    public void testGetNumShips() {
        // Test for numShips()
        Battleship b = new Battleship(3, 2);
        assertEquals(b.getNumShips(), 2);
        
        b.reset(5, 2);
        assertEquals(b.getNumShips(), 2);
        
        b.reset(5, 6);
        assertEquals(b.getNumShips(), 2);
        
        b.reset(10, 3);
        assertEquals(b.getNumShips(), 3);
    }
    
}
