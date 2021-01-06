public class Cruiser extends Boat {
    
    public Cruiser(Battleship b, int player, int length, int health, String imgPath, String name) {
        super(b, player, length, health, imgPath, name);
    }
    
    @Override
    public void attack() { // Hits 2x2 square twice each.
        int x = 1 + (int) (Math.random() * 8);
        int y = 1 + (int) (Math.random() * 8);
        for (int i = 0; i < 2; i++) {
            getShip().hitSquare(x, y);
            getShip().hitSquare(x + 1, y + 1);
            getShip().hitSquare(x + 1, y);
            getShip().hitSquare(x, y + 1);
        }
    }
}
