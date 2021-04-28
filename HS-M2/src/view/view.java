package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.controller;

public class view extends JFrame {
	private JPanel main;
	private	JPanel oppcards;
	private	JPanel oppinfo;
	private JPanel oppfield;
	private JPanel herofield;
	private JPanel heroinfo;
	private JPanel herocards;
	private JPanel decks;
	private JPanel cardinfo;
	
	public view(){
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0,0,screenSize.width,screenSize.height);
		this.setVisible(true);	
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setIconImage(new ImageIcon("Images/Icon.png").getImage());
		this.setTitle("Hearthstone made by Abdelrahman Hegab, Lina Walid,and Salma Sherif (Team 10)");
		JLabel background = new JLabel(controller.getScaledImage(new ImageIcon("Images/Board.png"), screenSize.width, screenSize.height));
		this.setContentPane(background);
		
		  this.setLayout(new BorderLayout());
		
	    decks=new JPanel();
	    main=new JPanel();
	    oppcards=new JPanel();
	    oppinfo=new JPanel();
	    herofield=new JPanel();
	    herocards=new JPanel();
	    heroinfo=new JPanel();
	    oppfield=new JPanel();
	    cardinfo = new JPanel();
	    
	    
	    //CardInfo
	    
	    cardinfo.setPreferredSize(new Dimension(2*screenSize.width/15,this.getHeight()));
	    this.add(cardinfo,BorderLayout.WEST);
	    cardinfo.setOpaque(false);
	    
	    //Decks
	    decks.setPreferredSize(new Dimension(2*screenSize.width/15,this.getHeight()));
	    decks.setLayout(null);
	    decks.setOpaque(false);
	    this.add(decks,BorderLayout.EAST);
	    //Main
	    main.setPreferredSize(new Dimension (this.getWidth()-(2*screenSize.width/15),this.getHeight()));
	    this.add(main,BorderLayout.CENTER);
	    main.setLayout(new GridLayout(6,0));
	    main.setOpaque(false);
	    
	  //OppCards
	    oppcards.setPreferredSize(new Dimension(main.getWidth(),screenSize.height/6));
	    main.add(oppcards);
	    oppcards.setOpaque(false);
	    
	   //OPP INFO
	    oppinfo.setPreferredSize(new Dimension(main.getWidth(),screenSize.height/6));
	    oppinfo.setLayout(null);
	    main.add(oppinfo);
	    oppinfo.setOpaque(false);
	  
	   //OPP field
	    oppfield.setPreferredSize(new Dimension(main.getWidth(),screenSize.height/6));
	    main.add(oppfield);
	    oppfield.setOpaque(false);
	    
	   //HeroField
	    herofield.setPreferredSize(new Dimension(main.getWidth(),screenSize.height/6));
	    main.add(herofield);
	    herofield.setOpaque(false);
	    
	    //HeroInfo
	    heroinfo.setPreferredSize(new Dimension(main.getWidth(),screenSize.height/6));
	    heroinfo.setLayout(null);
	    main.add(heroinfo);
	    heroinfo.setOpaque(false);

	    
	   //HeroCards
	    herocards.setPreferredSize(new Dimension(main.getWidth(),screenSize.height/6));
	    main.add(herocards);
	    herocards.setOpaque(false);
	 
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	   for ( Window w : Window.getWindows() ) {
	        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow( w );
	    } 
	    
	  		this.revalidate();
	  	    this.repaint();
	    
		
		
	}
	public JPanel getMain() {
		return main;
	}
	public JPanel getOppcards() {
		return oppcards;
	}
	public JPanel getOppinfo() {
		return oppinfo;
	}
	public JPanel getOppfield() {
		return oppfield;
	}
	public JPanel getHerofield() {
		return herofield;
	}
	public JPanel getHeroinfo() {
		return heroinfo;
	}
	public JPanel getHerocards() {
		return herocards;
	}
	public JPanel getDecks() {
		return decks;
	}
	public JPanel getCardinfo() {
		return cardinfo;
	}

}
