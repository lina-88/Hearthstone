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

public class GameOver extends JFrame implements ActionListener {
	
	JButton New;
	
	public GameOver(String s){
		this.setVisible(true);
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(5*screenSize.width/24,5*screenSize.height/18,25*screenSize.width/46,25*screenSize.height/54);
		this.setLayout(null);
		this.setAlwaysOnTop(true);
		this.setTitle("Game Over");
		this.setIconImage(new ImageIcon("Images/Icon.png").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel background = new JLabel();
		background.setIcon(controller.getScaledImage(new ImageIcon("Images/message.jpg"), this.getWidth(), this.getHeight()));
		
		JTextArea bla = new JTextArea();
		bla.setOpaque(false);
		bla.setText("Well Played,\nThe Winner is " + s);
		bla.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/35));
		bla.setBounds(this.getWidth()/10, this.getHeight()/5, this.getWidth(), 3*screenSize.height/16);
		bla.setEditable(false);
		
		JButton ok = new JButton("Exit");
		ok.setBounds(37*screenSize.width/142, 35*screenSize.height/108, 5*screenSize.width/48, 5*screenSize.height/108);
		ok.setIcon(controller.getScaledImage(new ImageIcon("Images/Button original.png"), ok.getWidth(), ok.getHeight()));
		ok.setFont(new Font("Arial Black", Font.BOLD, screenSize.width/64));
		ok.setHorizontalTextPosition(JButton.CENTER);
		ok.addActionListener(this);
		
		New = new JButton("New Game");
		New.setBounds(37*screenSize.width/280, 35*screenSize.height/108, 5*screenSize.width/48, 5*screenSize.height/108);
		New.setIcon(controller.getScaledImage(new ImageIcon("Images/Button original.png"), ok.getWidth(), ok.getHeight()));
		New.setFont(new Font("Arial Black", Font.BOLD, screenSize.width/80));
		New.setHorizontalTextPosition(JButton.CENTER);
		New.addActionListener(this);
		
		this.setContentPane(background);
		this.setResizable(false);
		this.add(bla);
		this.add(ok);
		this.add(New);
		
		revalidate();
		repaint();
	}

	public JButton getNew() {
		return New;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	//	JButton b = (JButton)e.getSource();
			//this.setVisible(false);
			this.dispose();
			
		
	}
	

}
