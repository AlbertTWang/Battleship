public class Submarine extends Boat {
    
    public Submarine(Battleship b, int player, int length,
            int health, String imgPath, String name) {
        super(b, player, length, health, imgPath, name);
    }
    
    @Override
    public void attack() { // Hits 3x3 square once
        int x = 1 + (int) (Math.random() * 8);
        int y = 1 + (int) (Math.random() * 8);
        getShip().hitSquare(x, y);
        getShip().hitSquare(x + 1, y);
        getShip().hitSquare(x - 1, y);

        getShip().hitSquare(x, y + 1);
        getShip().hitSquare(x + 1, y + 1);
        getShip().hitSquare(x - 1, y + 1);
        
        getShip().hitSquare(x, y - 1);
        getShip().hitSquare(x + 1, y - 1);
        getShip().hitSquare(x - 1, y - 1);
    }
}
