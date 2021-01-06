import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    
    private Battleship b;
    private JLabel status;
    
    private int numShips;
    private Point firstPoint;
    private int clickCount;
    private boolean[] shipAttackUsed;
    private Color randomColor;
    
    public static final int BOARD_WIDTH = 1100;
    public static final int BOARD_HEIGHT = 500;
    public static final Color GRAY = Color.GRAY;
    public static final Color RED = Color.RED;
    public static final Color DEFAULT = new Color(238, 238, 238);
    public static final Color[] COLOR_ARRAY = new Color[]
        {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN,
            Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW};
    
    public GameBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        numShips = 5;
        b = new Battleship(10, numShips);
        status = statusInit;
        clickCount = 1;
        shipAttackUsed = new boolean[5];
        randomColor = DEFAULT;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!b.getGameOver()) {
                    if (!b.getSettingComplete()) { // Setting Ships
                        Point p = e.getPoint();
                        if (clickCount == 1) {
                            clickCount++;
                            firstPoint = p;
                        } else {
                            if (!b.setShips(firstPoint.y / 50, firstPoint.x / 50,
                                    p.y / 50, p.x / 50)) {
                                JOptionPane.showMessageDialog(null,
                                        "Invalid Ship Placement. Try Again.","Message",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                updateSetStatus();
                            }
                            clickCount = 1;
                        }
                        while (!b.getSettingPlayer()) {
                            int x1 = (int) (Math.random() * 10);
                            int y1 = (int) (Math.random() * 10);
                            if (x1 - b.getSettingWhichShipPlayer2() >= 0) {
                                if (b.setShips(y1, x1, y1, x1 - b.getSettingWhichShipPlayer2())) {
                                    System.out.println("Computer Ship Placed");
                                }
                            }
                        }
                    } else { // Playing Game
                        if (b.getCurrentPlayer()) {
                            Point p = e.getPoint();
                            if (p.x > 600 && b.playTurn(p.y / 50, (p.x - 600) / 50)) {
                                int x = (int) (Math.random() * 10);
                                int y = (int) (Math.random() * 10);
                                
                                b.playTurn(y, x);
                                updatePlayStatus();
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Invalid Hit. Try Again.","Message",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "The game ended. Click Reset to Play Again!","Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    
    public void reset() {
        b.reset(10, numShips);
        shipAttackUsed = new boolean[5];
        status.setText("Set Ironclad");
        randomColor = DEFAULT;
        repaint();
        
        requestFocusInWindow();
        JOptionPane.showMessageDialog(null,
                "HOW TO PLAY:\n"
                + "There are two phases in Battleship: Setting and Attacking.\n"
                + "To start, set your ships by clicking twice within your board.\n"
                + "You will be setting 5 ships: Ironclad, Submarine, Destroyer, Cruiser, Carrier.\n"
                + "They have respective lengths of 1, 2, 3, 4, 5.\n"
                + "To set a ship, your two clicks must match "
                + "the length of the ship you're setting.\n"
                + "","Instructions",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void undo() {
        b.undo();
        status.setText("Turn " + b.getTurns());
        repaint();
    }
    
    public void attack(int whichShip) {
        if (!b.getSettingComplete()) {
            JOptionPane.showMessageDialog(null,
                    "Setting not complete!","Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (shipAttackUsed[whichShip]) {
            JOptionPane.showMessageDialog(null,
                    "Attack used already!","Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (b.getPlayer1Ships()[whichShip].isSunk()) {
            JOptionPane.showMessageDialog(null,
                    "Ship is sunk! Can't use attack!","Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        b.getPlayer1Ships()[whichShip].attack();
        b.playTurn();
        status.setText("Turn " + b.getTurns());
        shipAttackUsed[whichShip] = true;
        repaint();
    }
    
    private void updateSetStatus() {
        if (b.getSettingWhichShipPlayer1() == numShips) {
            updatePlayStatus();
            JOptionPane.showMessageDialog(null,
                    "All ships set! Click on enemy board to attack!\n"
                    + "You win once you sink all enemy ships.\n"
                    + "You are allowed and will need to attack a square twice to win.\n"
                    + "You can undo at anytime.\n"
                    + "You have access to each of your ship's one time use attack.\n"
                    + "You CANNOT undo a one time use attack. The damage is done.\n"
                    + "Good luck!"
                    + "","Attack Phase Begins",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            status.setText("Set " +
                            b.getPlayer1Ships()[(b.getSettingWhichShipPlayer1())].getName());
        }
        
    }
    
    private void updatePlayStatus() {
        status.setText("Turn " + b.getTurns());
        int winner = b.checkWinner();
        if (winner == 1) {
            JOptionPane.showMessageDialog(null,
                    "You Won!","Message",
                    JOptionPane.INFORMATION_MESSAGE);
            status.setText("You Win!");
        } else if (winner == 2) {
            JOptionPane.showMessageDialog(null,
                    "You Lost!","Message",
                    JOptionPane.INFORMATION_MESSAGE);
            status.setText("You Lost!");
        } else if (winner == 3) {
            status.setText("Neither Player Won.");
            JOptionPane.showMessageDialog(null,
                    "\"There is no instance of a nation benefitting from prolonged warfare.\""
                    + " -Sun Tzu\n"
                    + "Neither Player wins.","Message",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void randomColorBoard1() {
        int x = (int) (Math.random() * 10);
        randomColor = COLOR_ARRAY[x];
        repaint();
    }
    
    public void showInfo() {
        JOptionPane.showMessageDialog(null,
                "Battleship Game created by Albert Wang \n"
                + "CIS120 Final Project \n"
                + "Created with Java Swing \n","Credits",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawLine(50, 0, 50, 500);
        g.drawLine(100, 0, 100, 500);
        g.drawLine(150, 0, 150, 500);
        g.drawLine(200, 0, 200, 500);
        g.drawLine(250, 0, 250, 500);
        g.drawLine(300, 0, 300, 500);
        g.drawLine(350, 0, 350, 500);
        g.drawLine(400, 0, 400, 500);
        g.drawLine(450, 0, 450, 500);
        g.drawLine(500, 0, 500, 500);
        
        g.drawLine(0, 50, 500, 50);
        g.drawLine(0, 100, 500, 100);
        g.drawLine(0, 150, 500, 150);
        g.drawLine(0, 200, 500, 200);
        g.drawLine(0, 250, 500, 250);
        g.drawLine(0, 300, 500, 300);
        g.drawLine(0, 350, 500, 350);
        g.drawLine(0, 400, 500, 400);
        g.drawLine(0, 450, 500, 450);
        
        
        g.drawLine(600, 0, 600, 500);
        g.drawLine(650, 0, 650, 500);
        g.drawLine(700, 0, 700, 500);
        g.drawLine(750, 0, 750, 500);
        g.drawLine(800, 0, 800, 500);
        g.drawLine(850, 0, 850, 500);
        g.drawLine(900, 0, 900, 500);
        g.drawLine(950, 0, 950, 500);
        g.drawLine(1000, 0, 1000, 500);
        g.drawLine(1050, 0, 1050, 500);
        
        g.drawLine(600, 50, 1100, 50);
        g.drawLine(600, 100, 1100, 100);
        g.drawLine(600, 150, 1100, 150);
        g.drawLine(600, 200, 1100, 200);
        g.drawLine(600, 250, 1100, 250);
        g.drawLine(600, 300, 1100, 300);
        g.drawLine(600, 350, 1100, 350);
        g.drawLine(600, 400, 1100, 400);
        g.drawLine(600, 450, 1100, 450);
        
        for (int i = 0; i < numShips; i++) {
            b.getPlayer1Ships()[i].draw(g);
            if (b.getPlayer2Ships()[i].isSunk()) {
                b.getPlayer2Ships()[i].draw(g);
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int hit1 = b.getBoard1Hit(i, j);
                int hit2 = b.getBoard2Hit(i, j);
                
                int ship1 = b.getBoard1Ship(i, j);
                int ship2 = b.getBoard2Ship(i, j);
                
                if (hit1 != 0 && ship1 != 0) {
                    g.setColor(RED);
                    g.drawRect(50 * j, 50 * i, 50, 50);
                    g.drawLine(50 * j, 50 * i, 50 + 50 * j, 50 + 50 * i);
                    g.drawLine(50 + 50 * j, 50 * i, 50 * j, 50 + 50 * i);
                }
                if (hit1 != 0 && ship1 == 0) {
                    g.setColor(GRAY);
                    g.fillOval(20 + 50 * j, 20 + 50 * i, 10, 10);
                }
                
                if (hit2 != 0 && ship2 != 0) {
                    g.setColor(RED);
                    g.drawRect(600 + 50 * j, 50 * i, 50, 50);
                    g.drawLine(600 + 50 * j, 50 * i, 650 + 50 * j, 50 + 50 * i);
                    g.drawLine(650 + 50 * j, 50 * i, 600 + 50 * j, 50 + 50 * i);
                }
                
                if (hit2 != 0 && ship2 == 0) {
                    g.setColor(GRAY);
                    g.fillOval(620 + 50 * j, 20 + 50 * i, 10, 10);
                }
            }
        }
        g.setColor(randomColor);
        g.fillRect(501, 0, 98, 500);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}