public class Destroyer extends Boat {
    
    public Destroyer(Battleship b, int player, int length,
            int health, String imgPath, String name) {
        super(b, player, length, health, imgPath, name);
    }
    
    @Override
    public void attack() { // Hits an entire row once each
        int y = (int) (Math.random() * 10);
        for (int i = 0; i < 10; i++) {
            getShip().hitSquare(i, y);
        }
    }
}
