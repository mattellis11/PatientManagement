/**
 * MainFrame.java
 * @author Matt Ellis
 * Creates JFrame of Patient Collection gui
 */

package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

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
