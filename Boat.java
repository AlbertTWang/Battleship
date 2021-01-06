import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public abstract class Boat {
    
    private String imagePath;
    private Battleship b;
    private int player;
    private int length;
    private int health;
    private boolean placed;
    private BufferedImage img;
    private List<int[]> squares;
    private boolean messageSunk;
    private String name;
    
    public Boat(Battleship b, int player, int length, int health, String imgPath, String name) {
        this.b = b;
        this.player = player;
        this.length = length;
        this.health = health;
        this.name = name;
        placed = false;
        messageSunk = false;
        squares = new LinkedList<>();
        imagePath = imgPath;
        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public abstract void attack();
    
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }
    
    public void setSquares(int[] point) {
        squares.add(point);
    }
    
    public int getLength() {
        return length;
    }
    
    public int getHealth() {
        return health;
    }
    
    public List<int[]> getSquares() {
        return squares;
    }
    
    public Battleship getShip() {
        return b;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public boolean isSunk() { // Checks if Boat is sunk
        if (!placed) {
            return false;
        }
        if (player == 1) {
            for (int[] point : squares) {
                if (b.getBoard1Hit(point[0], point[1]) < health) {
                    return false;
                }
            }
            if (!messageSunk) {
                JOptionPane.showMessageDialog(null,
                        "Your " + name + " was sunk!","Message",
                        JOptionPane.INFORMATION_MESSAGE);
                messageSunk = true;
            }
            return true;
        } else {
            for (int[] point : squares) {
                if (b.getBoard2Hit(point[0], point[1]) < health) {
                    return false;
                }
            }
            if (!messageSunk) {
                JOptionPane.showMessageDialog(null,
                        "You sunk their " + name + "!","Message",
                        JOptionPane.INFORMATION_MESSAGE);
                messageSunk = true;
            }
            return true;
        }
    }
    
    public void resetSunk() {
        messageSunk = false;
    }
    
    public void draw(Graphics g) {
        if (placed) {
            for (int[] point : squares) {
                if (player == 1) {
                    g.drawImage(img, point[1] * 50, point[0] * 50, 50, 50, null);
                } else {
                    g.drawImage(img, 600 + point[1] * 50, point[0] * 50, 50, 50, null);
                }
            }
        }
    }
}
