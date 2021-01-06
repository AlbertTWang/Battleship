import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Battleship");
        frame.setLocation(150, 150);
        frame.setResizable(false);
        
        final JPanel status_panel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setHgap(0);
        status_panel.setLayout(layout);
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);
        
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new FlowLayout());
        
        final JButton descripter1 = new JButton("Your Board");
        descripter1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.randomColorBoard1();
            }
        });
        control_panel.add(descripter1);
        
        control_panel.add(Box.createHorizontalStrut(150));
        
        final JButton undo = new JButton("Undo Move");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);
        
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);
        control_panel.add(Box.createHorizontalStrut(150));
        
        final JButton descripter2 = new JButton("Enemy Board");
        descripter2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.showInfo();
            }
        });
        control_panel.add(descripter2);
        
        final JButton ironclad = new JButton("Ironclad");
        ironclad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.attack(0);
            }
        });
        status_panel.add(ironclad);
        
        final JButton submarine = new JButton("Submarine");
        submarine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.attack(1);
            }
        });
        status_panel.add(submarine);
        
        final JButton destroyer = new JButton("Destroyer");
        destroyer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.attack(2);
            }
        });
        status_panel.add(destroyer);
        
        final JButton cruiser = new JButton("Cruiser");
        cruiser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.attack(3);
            }
        });
        status_panel.add(cruiser);
        
        final JButton carrier = new JButton("Carrier");
        carrier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.attack(4);
            }
        });
        status_panel.add(carrier);
        status_panel.add(Box.createHorizontalStrut(15));
        status_panel.add(status);
        status_panel.add(Box.createHorizontalStrut(415));
        
        frame.add(control_panel, BorderLayout.NORTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        board.reset();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}