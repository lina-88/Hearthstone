package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import controller.controller;

public class Names extends JFrame {
	private JButton Change;
	private JLabel tall;
	private JTextArea name1;
	private JTextArea name2;
	
	public Names(){
		
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(5*screenSize.width/24,5*screenSize.height/18,25*screenSize.width/46,25*screenSize.height/54);
		this.setLayout(null);
		this.setTitle("Players' Names");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(new ImageIcon("Images/Icon.png").getImage());
		
		JLabel background = new JLabel();
		background.setIcon(controller.getScaledImage(new ImageIcon("Images/message.jpg"), this.getWidth(), this.getHeight()));
		
		name1 = new JTextArea();
		name1.setText("");
		name1.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
		name1.setBounds(37*screenSize.width/140,getHeight()/5 , this.getWidth()/6, 5*screenSize.height/120);
		JLabel p1 = new JLabel("Player 1's Name:");
		p1.setBounds(37*screenSize.width/360,getHeight()/5 , this.getWidth()/3, 5*screenSize.height/120);
		p1.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/60));
		
		name2 = new JTextArea();
		name2.setText("");
		name2.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
		name2.setBounds(37*screenSize.width/140,getHeight()/2 , this.getWidth()/6, 5*screenSize.height/120);
		JLabel p2 = new JLabel("Player 2's Name:");
		p2.setBounds(37*screenSize.width/360,getHeight()/2 , this.getWidth()/3, 5*screenSize.height/120);
		p2.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/60));
		
		tall = new JLabel();
		tall.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
		tall.setBounds(37*screenSize.width/400,0 , this.getWidth(), 5*screenSize.height/120);
		//tall.setForeground(Color.RED);
		
		Change = new JButton("Change");
		Change.setBounds(37*screenSize.width/192, 35*screenSize.height/108, 5*screenSize.width/48, 5*screenSize.height/108);
		Change.setIcon(controller.getScaledImage(new ImageIcon("Images/Button original.png"), Change.getWidth(), Change.getHeight()));
		Change.setFont(new Font("Arial Black", Font.BOLD, screenSize.width/64));
		Change.setHorizontalTextPosition(JButton.CENTER);
		this.setContentPane(background);
		this.add(name1);
		this.add(name2);
		this.add(Change);
		this.add(p1);
		this.add(p2);
		this.add(tall);
		this.setResizable(false);

		revalidate();
		repaint();
		
	}
	public JButton getChange() {
		return Change;
	}
	public JTextArea getName1() {
		return name1;
	}
	public JTextArea getName2() {
		return name2;
	}
	public JLabel getTall() {
		return tall;
	}
}

