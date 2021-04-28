package view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.controller;


public class WelcomeScreen extends JFrame {
	private Dimension screenSize;
	private int screenHeight;
	private int screenWidth;
	controller bla;

	public WelcomeScreen(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenHeight = screenSize.height;
		screenWidth = screenSize.width;
	//this.setBounds(0, 0, 1920, 1080);	
	this.setPreferredSize(screenSize);
	this.setVisible(true);	
	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	this.setIconImage(new ImageIcon("Images/Icon.png").getImage());
	this.setTitle("Hearthstone made by Abdelrahman Hegab, Lina Walid,and Salma Sherif (Team 10)");
	
	ImageIcon original = new ImageIcon("Images/Hearthstone.jpg");
	JLabel background = new JLabel(controller.getScaledImage(original,screenWidth,screenHeight));
	this.setContentPane(background);
	this.setLayout(null); 
	
    setExtendedState(JFrame.MAXIMIZED_BOTH);
   for ( Window w : Window.getWindows() ) {
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow( w );
    }
	revalidate();
	repaint();
	}
	
	
public int getHeighty() {
		return screenHeight;
	}


	public int getWidthy() {
		return screenWidth;
	}


public Dimension getScreenSize() {
		return screenSize;
	}

}