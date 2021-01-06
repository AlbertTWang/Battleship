public class Carrier extends Boat {
    
    public Carrier(Battleship b, int player, int length, int health, String imgPath, String name) {
        super(b, player, length, health, imgPath, name);
    }
    
    @Override
    public void attack() { // Hits one random square from each column once
        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * 10);
            getShip().hitSquare(x, i);
        }
    }
}
