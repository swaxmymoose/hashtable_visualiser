/*  07/02/2020
    VisualiserApp.java
    COSC326
*/

import javax.swing.*;

public class VisualiserApp {
    public static void main (String args[]) {
        JFrame main = new JFrame("Hash Table Visualiser Main Menu");
        main.getContentPane().add(new MainMenuPanel());
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setResizable(false);
        main.pack();
        main.setLocationRelativeTo(null);
        main.setVisible(true);
    }
}