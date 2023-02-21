package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Creates frontend GUI for a patient collection
 *
 * @author Matt Ellis
 */
public class MainFrame {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Patient Collection Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(815, 538));
        frame.setResizable(false);

        frame.getContentPane().add(new MainPanel());

        frame.pack();
        frame.setVisible(true);

    }

}
