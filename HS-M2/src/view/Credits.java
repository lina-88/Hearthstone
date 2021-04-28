package view;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import controller.controller;

public class Credits extends JFrame implements ActionListener {

public Credits(){
	this.setVisible(true);
	this.setAlwaysOnTop(true);
	Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	this.setBounds(5*screenSize.width/24,5*screenSize.height/18,25*screenSize.width/46,25*screenSize.height/54);
	this.setLayout(null);
	this.setTitle("Info");
	this.setIconImage(new ImageIcon("Images/Icon.png").getImage());
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	JLabel background = new JLabel();
	background.setIcon(controller.getScaledImage(new ImageIcon("Images/message.jpg"), this.getWidth(), this.getHeight()));
	JTextArea bla = new JTextArea();
	bla.setOpaque(false);
	bla.setText("This Game is made by: \nAbdelrahman Hegab, Lina Walid ,and Salma Sherif (Team 10) \n"
			+ "as a project for the CSEN 401 course in the met faculty.\nWe seriously hope you enjoy the game and that you like our GUI \n \n \n"
			+"The game is designed to work on any Resolution/Aspect Ratio. \nHowever, it's advised to use 16:9 Aspect Ratio screens (preferably 1080p)"
			+"\nOtherwise Some of the images and texts will be blurry");
	bla.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
	bla.setBounds(0, 0, this.getWidth(), 5*screenSize.height/16);
	bla.setEditable(false);
	
	JButton ok = new JButton("OK");
	ok.setBounds(37*screenSize.width/192, 35*screenSize.height/108, 5*screenSize.width/48, 5*screenSize.height/108);
	ok.setIcon(controller.getScaledImage(new ImageIcon("Images/Button original.png"), ok.getWidth(), ok.getHeight()));
	ok.setFont(new Font("Arial Black", Font.BOLD, screenSize.width/64));
	ok.setHorizontalTextPosition(JButton.CENTER);
	ok.addActionListener(this);
	this.setContentPane(background);
	this.add(bla);
	this.add(ok);
	this.setResizable(false);

	revalidate();
	repaint();
	
}

@Override
public void actionPerformed(ActionEvent e) {
//	this.setVisible(false);
	this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	
}
}

