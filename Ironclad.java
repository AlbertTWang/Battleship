public class Ironclad extends Boat {
    
    public Ironclad(Battleship b, int player, int length, int health, String imgPath, String name) {
        super(b, player, length, health, imgPath, name);
    }
    
    @Override
    public void attack() { // Hits 1x1 square twice
        int x = (int) (Math.random() * 10);
        int y = (int) (Math.random() * 10);
        getShip().hitSquare(x, y);
        getShip().hitSquare(x, y);
    }
}
