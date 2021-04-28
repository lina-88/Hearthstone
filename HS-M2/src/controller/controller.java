package controller;

/*Hello And Welcome,
 * This Is the CSEN 401 project for 2020, done by Team 10, Abdelrahman Hegab, Lina Walid, Salma Sherif.
 * In 90% of the cases I'll be using the Null Layout although it's painful and really hard to read or write or to deal with ( ain't no coding without some pain ha ? )
 * However, I like the freedom of using x and y coordinates to Place my elements wherever I want.
 * This Project Works Best With 1920x1080 screens as it's designed on a display with such dimensions however it should work with any screen
 * Every Single Set Bounds Method is Actually used By Trial And error.. I First try maybe 500,500,500,500 or something and then alternate according
 * to the need and to the looks. when I find the perfect match I then change the numbers to numbers that are relative to the screen dimensions
 * so for example since my screen width is 1920 and I want to add an element with 500 pixels width it will be added like this : 25*screenSize.width/96
 *
 *This GUI Took Way Too Much time because of using Null Layout. But I Think the end result Is Worth it.
 *and Finally Let's Begin to see the code shall we ?
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import model.cards.Card;
//import model.cards.Rarity;
import model.cards.minions.Minion;
import model.cards.spells.*;
import model.heroes.*;
import view.*;
import engine.Game;
import engine.GameListener;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;

public class controller implements ActionListener,GameListener, MouseListener {
private Game model;
private view view;
private GameOver gameover;
private Hero h1; //Bottom Hero (Will be Referenced as "Hero" as we go on)
private Hero h2; //Top Hero	(Will be Referenced as "Opponent" as we go on)
private JLabel p1text; 
private JLabel p2text;
private JLabel Hhealth;
private JLabel Hmana;
private JLabel Ohealth;
private JLabel Omana;
private JButton ODeck;
private JButton HDeck;
private ArrayList<JButton> HeroButtons;		//(Relating Hero's Hand Cards with Hand Buttons)
private ArrayList<JButton> OppButtons;		//same opponent
private ArrayList<JButton> HFieldButtons;	//(Relating Hero's Field Cards with Field Buttons)
private ArrayList<JButton> OFieldButtons;	// you get it
private JLabel current;				//specifies if it is h1 or h2's turn to play
private JButton test;	
private warning war;
private Minion attacker;
private Minion target;
private boolean attackcommand;		//turns true if u click on a minion to trigger attacking next target
private WelcomeScreen screen;
private boolean Hmagepower;		//turns true if u use Mage Power in h1
private JPanel photo;
private JLabel status;
private boolean Omagepower;		//turns true if u use Mage Power in h2
private Clip clip;
private Dimension screenSize;	//will be used to make the game adapt to different dimensions
private JLabel Basic;			//this Jlabel Holds the Basic (Flippd) card imageicon
private ImageIcon BasicButton;  // yup this one
private boolean HPriestPower;
private boolean OPriestPower;
private int Hfatigue = 0;
private int Ofatigue = 0;
private boolean OMinionTSpell;
private boolean HMinionTSpell;
private MinionTargetSpell MinionTSpell;
private JButton MinionTSpellButton;
private boolean OHeroTSpell;
private boolean HHeroTSpell;
private HeroTargetSpell HeroTSpell;
private JButton HeroTSpellButton;
private Minion MightDieMinion;
private boolean HLeechboolean;
private boolean OLeechboolean;
private LeechingSpell LeechSpell;
private JButton LeechButton;
private Names names;
private String p1 = "Player 1";
private String p2 = "Player 2";
private Credits info;

public controller(){
	screen = new WelcomeScreen();
	p1text = new JLabel(); // this label changes from Player 1 selected (Hero class) or (player 1 please select a specific hero class)
	p2text = new JLabel(); // the same thing for p2
	
	//Removing old Game Windows In case of consturctor is called twice ( New Game after a finished one)
	   for ( Window w : Window.getWindows() ) {
		   if(!(w == screen)){
			   w.removeAll();
	        w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));}
	    }
	
	
	
	//						Constructing the Welcome/Hero choosing Screen with null layout 					\\
	
	
	/*In a nutshell you have 10 buttons with the same Image where 
	 * they have different command actions that changes p1 and p2 texts, 
	 * assign values to h1 and h2 Hero attributes,
	 * and pass h1 and h2 to another constructor that uses them to initialize the game.
	 * every font and bounds will be relative to the screen size so that the game looks the same on different displays
	 * Please Check the Action command of each button if You want to how it's handled in the action perform method below.
	*/
	
	p1text.setBounds(65*screen.getWidthy()/192, 5*screen.getHeighty()/18, 5*screen.getWidthy()/12, 5*screen.getHeighty()/54);
	p1text.setFont(new Font("Arial Black", Font.BOLD, screen.getWidthy()/64));
	p1text.setForeground(Color.WHITE);
	
	p2text.setBounds(65*screen.getWidthy()/192, (5*screen.getHeighty()/18)+(13*screen.getHeighty()/108), 5*screen.getWidthy()/12, 5*screen.getHeighty()/54);
	p2text.setFont(new Font("Arial Black", Font.BOLD, screen.getWidthy()/64));
	p2text.setForeground(Color.WHITE);
	
	JButton hunter = new JButton("Hunter");
	hunter.setFont(new Font("Arial Black", Font.BOLD, screen.getWidthy()/64));
	hunter.addActionListener(this);
	hunter.setBounds(65*screen.getWidthy()/192, 46*screen.getHeighty()/135, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	ImageIcon select = getScaledImage( new ImageIcon("Images/Button welcome.png"), hunter.getWidth(), hunter.getHeight());
	hunter.setIcon(select);
	hunter.setHorizontalTextPosition(JButton.CENTER);

	
	JButton mage = new JButton("Mage");
	mage.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	mage.addActionListener(this);
	mage.setBounds(65*screen.getWidthy()/192+7*screen.getWidthy()/64, 46*screen.getHeighty()/135, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	mage.setIcon(select);
	mage.setHorizontalTextPosition(JButton.CENTER);
	
	JButton paladin = new JButton("Paladin");
	paladin.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	paladin.addActionListener(this);
	paladin.setBounds(65*screen.getWidthy()/192+14*screen.getWidthy()/64, 46*screen.getHeighty()/135, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	paladin.setIcon(select);
	paladin.setHorizontalTextPosition(JButton.CENTER);
	
	JButton priest = new JButton("Priest");
	priest.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	priest.addActionListener(this);
	priest.setBounds(65*screen.getWidthy()/192+21*screen.getWidthy()/64, 46*screen.getHeighty()/135, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	priest.setIcon(select);
	priest.setHorizontalTextPosition(JButton.CENTER);
	
	JButton warlock = new JButton("Warlock");
	warlock.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	warlock.addActionListener(this);
	warlock.setBounds(65*screen.getWidthy()/192+28*screen.getWidthy()/64, 46*screen.getHeighty()/135, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	warlock.setIcon(select);
	warlock.setHorizontalTextPosition(JButton.CENTER);
	
	
	JButton hunter2 = new JButton("Hunter");
	hunter2.setFont(new Font("Arial Black", Font.BOLD, screen.getWidthy()/64));
	hunter2.addActionListener(this);
	hunter2.setBounds(65*screen.getWidthy()/192, 83*screen.getHeighty()/180,5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	hunter2.setIcon(select);
	hunter2.setHorizontalTextPosition(JButton.CENTER);
	hunter2.setActionCommand("Hunter2");
	
	JButton mage2 = new JButton("Mage");
	mage2.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	mage2.addActionListener(this);
	mage2.setBounds(65*screen.getWidthy()/192+7*screen.getWidthy()/64, 83*screen.getHeighty()/180, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	mage2.setIcon(select);
	mage2.setHorizontalTextPosition(JButton.CENTER);
	mage2.setActionCommand("Mage2");
	
	JButton paladin2 = new JButton("Paladin");
	paladin2.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	paladin2.addActionListener(this);
	paladin2.setBounds(65*screen.getWidthy()/192+14*screen.getWidthy()/64, 83*screen.getHeighty()/180, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	paladin2.setIcon(select);
	paladin2.setHorizontalTextPosition(JButton.CENTER);
	paladin2.setActionCommand("Paladin2");
	
	JButton priest2 = new JButton("Priest");
	priest2.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	priest2.addActionListener(this);
	priest2.setBounds(65*screen.getWidthy()/192+21*screen.getWidthy()/64, 83*screen.getHeighty()/180, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	priest2.setIcon(select);
	priest2.setHorizontalTextPosition(JButton.CENTER);
	priest2.setActionCommand("Priest2");
	
	JButton warlock2 = new JButton("Warlock");
	warlock2.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	warlock2.addActionListener(this);
	warlock2.setBounds(65*screen.getWidthy()/192+28*screen.getWidthy()/64, 83*screen.getHeighty()/180, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	warlock2.setIcon(select);
	warlock2.setHorizontalTextPosition(JButton.CENTER);
	warlock2.setActionCommand("Warlock2");
	
	JButton start = new JButton("Start Game");
	start.addActionListener(this);
	start.setFont(new Font("Arial Black", Font.BOLD, screen.getWidthy()/39));
	start.setBounds(23*screen.getWidthy()/64, 25*screen.getHeighty()/36, 9*screen.getWidthy()/32, 11*screen.getHeighty()/72);
	start.setIcon(getScaledImage(new ImageIcon("Images/Button original.png"),start.getWidth(),start.getHeight()));
	start.setHorizontalTextPosition(JButton.CENTER);
	
	JButton credits = new JButton("Info");
	credits.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/64));
	credits.addActionListener(this);
	credits.setBounds(43*screen.getWidthy()/48, 0, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	credits.setIcon(select);
	credits.setHorizontalTextPosition(JButton.CENTER);
	
	JButton psound = new JButton("Stop Music");
	psound.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/80));
	psound.addActionListener(this);
	psound.setBounds(43*screen.getWidthy()/48,  5*screen.getHeighty()/108, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	psound.setIcon(select);
	psound.setHorizontalTextPosition(JButton.CENTER);
	
	JButton change = new JButton("Adjust Names");
	change.setFont(new Font("Arial Black", Font.PLAIN, screen.getWidthy()/90));
	change.addActionListener(this);
	change.setBounds(43*screen.getWidthy()/48,  10*screen.getHeighty()/108, 5*screen.getWidthy()/48, 5*screen.getHeighty()/108);
	change.setIcon(select);
	change.setHorizontalTextPosition(JButton.CENTER);
	
	screen.add(hunter);
	screen.add(mage);
	screen.add(paladin);
	screen.add(priest);
	screen.add(warlock);
	screen.add(hunter2);
	screen.add(mage2);
	screen.add(paladin2);
	screen.add(priest2);
	screen.add(warlock2);
	screen.add(start);
	screen.add(p1text);
	screen.add(p2text);
	screen.add(credits);
	screen.add(psound);
	screen.add(change);
	
	
	playsound("sounds/Hearthstone Soundtrack - Main Title.wav");
	clip.loop(Clip.LOOP_CONTINUOUSLY);
	screen.revalidate();
	screen.repaint();
	
	//As you Can see throughout all of this constructor I Used the Screen get method to get screen dimension
	//instead of getting them again here as I thought that I only needed to do that
	//turns out since This constructor calls the other one with passed heroes, the new constructor 
	//doesn't communicate with the (screen) attribute very well, so I had to call it again.
}


//X,and Y will be passed from The previous constructor, 
//Please check how the buttons work in the action performed method to understand how this work.
public controller(Hero x,Hero y,String p1, String p2) throws FullHandException, CloneNotSupportedException{
	screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //will be used to get relative sizes of the objects later
	UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/500)));
	//changing the ok button font in the warning messages that trigger when getting the exceptions
	
	//Initializing the attributes
	this.p1=p1;
	this.p2=p2;
	current = new JLabel("");
	HeroButtons = new ArrayList<JButton>();
	OppButtons = new ArrayList<JButton>();
	HFieldButtons= new ArrayList<JButton>();
	OFieldButtons= new ArrayList<JButton>();
	model = new Game(x,y);
	model.setListener(this);
	view = new view();
	
	
		//Removing old View incase of restarting the game after finishing it
	   for ( Window w : Window.getWindows() ) {
		   if(!(w == view)){
			   w.removeAll();
	        w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
	    }}
	
	Basic = new JLabel();
	Basic.setBounds(0,0,47*screenSize.width/350,107*screenSize.height/310);
	Basic.setIcon(getScaledImage(new ImageIcon("Images/CardDeck.JPG"), Basic.getWidth(), Basic.getHeight()));
	BasicButton = getScaledImage(new ImageIcon("Images/CardDeck.JPG"), 13*screenSize.width/192,screenSize.height/6); 

h1 = x; h2 =y; //the game should randomly assign who starts first to either x or y.

//Current Hero Info
//Getting the cards where the players start with, then add them to his hand and adds photo to them 
//(check CardPhoto method to know how this works)
//the CardData method was just for the testing stage without adding any Photos, so you don't really need to look at it.

for(int i=0; i<h1.getHand().size();i++){
	JButton HCard = new JButton("");
	HCard.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
	HCard.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
	
	if(h1.getHand().get(i) instanceof Minion)
		HCard.setActionCommand("HeroHandMinion");

	else{
		HCard.setActionCommand("HeroHandSpell");
		spellaction(HCard,h1.getHand().get(i));}
	//HCard.setText(CardData(h1.getHand().get(i)));
	
	if(model.getCurrentHero() == h1)
	CardPhoto(HCard,h1.getHand().get(i));
	else
		HCard.setIcon(BasicButton);
	
	
	HCard.addActionListener(this);
	HCard.addMouseListener(this);
	HeroButtons.add(HCard);
	view.getHerocards().add(HCard);
}
//Number Representing Hero's current mana
Hmana = new JLabel(""+h1.getCurrentManaCrystals()+"/"+h1.getTotalManaCrystals());
Hmana.setBounds(49*screenSize.width/194,screenSize.height/28,5*screenSize.width/48,5*screenSize.height/54);
Hmana.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/400));
Hmana.setForeground(Color.WHITE);

//same for health
Hhealth = new JLabel("" +h1.getCurrentHP());
Hhealth.setBounds(58*screenSize.width/192,screenSize.height/28,5*screenSize.width/48,5*screenSize.height/54);
Hhealth.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/384));
Hhealth.setForeground(Color.WHITE);


view.getHeroinfo().add(Hhealth);
view.getHeroinfo().add(Hmana);

JButton Hero = new JButton("");
Hero.addActionListener(this);
Hero.setActionCommand("Hero");
Hero.setBounds(125*screenSize.width/384,0,77*screenSize.width/960,43*screenSize.height/270);

JButton HPower = new JButton("");
HPower.setBounds(269*screenSize.width/640, 53*screenSize.height/1080, 3*screenSize.width/64, 19*screenSize.height/180);

//Assigning different command actions and image icons according to the picks of the hero. 
//Images are scaled to match the size using the getscaledImage method below
if(h1 instanceof Mage){
	HPower.setActionCommand("HMagePower");
	Hero.setIcon(getScaledImage(new ImageIcon("Images/Jaina.png"), Hero.getWidth(), Hero.getHeight()));
	HPower.setIcon(getScaledImage(new ImageIcon("Images/MagePower.png"), HPower.getWidth(), HPower.getHeight()));}
else if(h1 instanceof Hunter){
	HPower.setActionCommand("HHunterPower");
	Hero.setIcon(getScaledImage(new ImageIcon("Images/Hunter.png"), Hero.getWidth(), Hero.getHeight()));
	HPower.setIcon(getScaledImage(new ImageIcon("Images/HunterPower.png"), HPower.getWidth(), HPower.getHeight()));}
else if(h1 instanceof Paladin){
	HPower.setActionCommand("HPaladinPower");
	Hero.setIcon(getScaledImage(new ImageIcon("Images/Paladin.png"), Hero.getWidth(), Hero.getHeight()));
	HPower.setIcon(getScaledImage(new ImageIcon("Images/PaladinPower.png"), HPower.getWidth(), HPower.getHeight()));}
else if(h1 instanceof Warlock){
	HPower.setActionCommand("HWarlockPower");
	Hero.setIcon(getScaledImage(new ImageIcon("Images/Warlock.png"), Hero.getWidth(), Hero.getHeight()));
	HPower.setIcon(getScaledImage(new ImageIcon("Images/WarlockPower.png"), HPower.getWidth(), HPower.getHeight()));}
else if(h1 instanceof Priest){
	HPower.setActionCommand("HPriestPower");
	Hero.setIcon(getScaledImage(new ImageIcon("Images/Priest.png"), Hero.getWidth(), Hero.getHeight()));
	HPower.setIcon(getScaledImage(new ImageIcon("Images/PriestPower.png"), HPower.getWidth(), HPower.getHeight()));}

Hero.setBorderPainted(false);
Hero.setContentAreaFilled(false);
view.getHeroinfo().add(Hero);
HPower.addMouseListener(this);
HPower.addActionListener(this);
view.getHeroinfo().add(HPower);

//Opponent Info
//doing exactly the same thing as explained above but for the opponent
for(int j =0; j<h2.getHand().size();j++){
	JButton OCard = new JButton("");
	OCard.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
	OCard.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
	if(h2.getHand().get(j) instanceof Minion)
		OCard.setActionCommand("OppHandMinion");

	else{
		OCard.setActionCommand("OppHandSpell");
		spellaction(OCard,h2.getHand().get(j));}
	
	if(model.getCurrentHero() == h2)
	CardPhoto(OCard,h2.getHand().get(j));
	
	else
		OCard.setIcon(BasicButton);
	
	OCard.addMouseListener(this);
	OCard.addActionListener(this);
	OppButtons.add(OCard);
	view.getOppcards().add(OCard);
}
Omana = new JLabel(""+h2.getCurrentManaCrystals()+"/"+h2.getTotalManaCrystals());
Omana.setBounds(49*screenSize.width/194,screenSize.height/11,5*screenSize.width/48,5*screenSize.height/54);
Omana.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/400));
//Omana.setForeground(Color.WHITE);
Ohealth = new JLabel(""+h2.getCurrentHP());
Ohealth.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/384));
//Ohealth.setForeground(Color.WHITE);
Ohealth.setBounds(58*screenSize.width/192,screenSize.height/11,5*screenSize.width/64,5*screenSize.height/54);

view.getOppinfo().add(Ohealth);
view.getOppinfo().add(Omana);
JButton Opponent = new JButton();
Opponent.addActionListener(this);
Opponent.setActionCommand("Opp");
Opponent.setBounds(125*screenSize.width/384,0,77*screenSize.width/960,43*screenSize.height/270);

	
JButton OPower = new JButton("");
OPower.setBounds(269*screenSize.width/640, 2*screenSize.height/45, 3*screenSize.width/64, 19*screenSize.height/180);
if(h2 instanceof Mage){
	OPower.setActionCommand("OMagePower");
	Opponent.setIcon(getScaledImage(new ImageIcon("Images/Jaina.png"), Hero.getWidth(), Hero.getHeight()));
	OPower.setIcon(getScaledImage(new ImageIcon("Images/MagePower.png"), OPower.getWidth(), OPower.getHeight()));}
else if(h2 instanceof Hunter){
	OPower.setActionCommand("OHunterPower");
	Opponent.setIcon(getScaledImage(new ImageIcon("Images/Hunter.png"), Hero.getWidth(), Hero.getHeight()));
	OPower.setIcon(getScaledImage(new ImageIcon("Images/HunterPower.png"), OPower.getWidth(), OPower.getHeight()));}
else if(h2 instanceof Paladin){
	OPower.setActionCommand("OPaladinPower");
	Opponent.setIcon(getScaledImage(new ImageIcon("Images/Paladin.png"), Hero.getWidth(), Hero.getHeight()));
	OPower.setIcon(getScaledImage(new ImageIcon("Images/PaladinPower.png"), OPower.getWidth(), OPower.getHeight()));}
else if(h2 instanceof Warlock){
	OPower.setActionCommand("OWarlockPower");
	Opponent.setIcon(getScaledImage(new ImageIcon("Images/Warlock.png"), Hero.getWidth(), Hero.getHeight()));
	OPower.setIcon(getScaledImage(new ImageIcon("Images/WarlockPower.png"), OPower.getWidth(), OPower.getHeight()));}
else if(h2 instanceof Priest){
	OPower.setActionCommand("OPriestPower");
	Opponent.setIcon(getScaledImage(new ImageIcon("Images/Priest.png"), Hero.getWidth(), Hero.getHeight()));
	OPower.setIcon(getScaledImage(new ImageIcon("Images/PriestPower.png"), OPower.getWidth(), OPower.getHeight()));}

Opponent.setBorderPainted(false);
Opponent.setContentAreaFilled(false);
view.getOppinfo().add(Opponent);
OPower.addActionListener(this);
OPower.addMouseListener(this);
view.getOppinfo().add(OPower);


//card info
/* this panel consists of :
 * A current JLabel that displays which hero's turn it is
 * a photo panel that displays enlarged photo when someone hovers over a button
 * (Check CardPhoto method that returns a new JLabel with the imageicon to know how the photo panel works)
 * a status JLabel to indicate if one of the players is making an action like (Attacking) or (Using Hero Power) etc.
 */

current.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/70));
current.setForeground(Color.WHITE);
view.getCardinfo().add(current);
if(model.getCurrentHero() ==h1)
	current.setText(p1+"'s Turn");
else
	current.setText(p2+"'s Turn");
photo = new JPanel();
photo.setOpaque(false);
photo.setPreferredSize(new Dimension(47*screenSize.width/320,107*screenSize.height/310));
view.getCardinfo().add(photo);
status = new JLabel("");
status.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
status.setForeground(Color.WHITE);
view.getCardinfo().add(status);

//Decks
/*This Panel Contains an end turn button
 * also contains 2 buttons that represent respective Hero Deck and the number of cards remaining in them.
 * Now decided to add Stop Music button in it as well. yay.
 */

JButton psound = new JButton("Stop Music");
psound.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
psound.addActionListener(this);
psound.setBounds(0,0, 37*screenSize.width/320, 5*screenSize.height/108);
psound.setIcon(getScaledImage(new ImageIcon("Images/Button original.png"), psound.getWidth(), psound.getHeight()));
psound.setHorizontalTextPosition(JButton.CENTER);
//psound.setActionCommand("Stop Music2");

JLabel player2 = new JLabel(p2);
JLabel player1 = new JLabel(p1);
player2.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/350));
player1.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/350));
if(p2.length()>=10||p1.length()>=10)
	current.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/500));	

player2.setBounds(0,90*screenSize.height/1080, 37*screenSize.width/200, 5*screenSize.height/108);
player2.setForeground(Color.WHITE);
player1.setBounds(0,820*screenSize.height/1080, 37*screenSize.width/200, 5*screenSize.height/108);
player1.setForeground(Color.WHITE);

ODeck = new JButton(h2.getDeck().size()+"");
ODeck.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/32));
ODeck.addActionListener(this);
ODeck.setBounds(0,7*screenSize.height/54,37*screenSize.width/320,screenSize.height/4);
ODeck.setIcon(getScaledImage(new ImageIcon("Images/CardDeck.jpg"), ODeck.getWidth(), ODeck.getHeight()));
ODeck.setHorizontalTextPosition(JButton.CENTER);
ODeck.setForeground(Color.WHITE);
ODeck.setBorderPainted(false);

JButton end = new JButton();
end.setActionCommand("End Turn");
end.setBounds(0,85*screenSize.height/216,37*screenSize.width/320,23*screenSize.height/216);
end.setIcon(getScaledImage(new ImageIcon("Images/EndButton.png"), end.getWidth(), end.getHeight()));
end.addActionListener(this);

HDeck = new JButton(""+h1.getDeck().size());
HDeck.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/32));
HDeck.addActionListener(this);
HDeck.setBounds(0,37*screenSize.height/72,37*screenSize.width/320,screenSize.height/4);
HDeck.setIcon(getScaledImage(new ImageIcon("Images/CardDeck.jpg"), HDeck.getWidth(), HDeck.getHeight()));
HDeck.setHorizontalTextPosition(JButton.CENTER);
HDeck.setForeground(Color.WHITE);
HDeck.setBorderPainted(false);

view.getDecks().add(ODeck);
view.getDecks().add(end);
view.getDecks().add(HDeck);
view.getDecks().add(psound);
view.getDecks().add(player2);
view.getDecks().add(player1);
view.pack();

view.revalidate();
view.repaint();


playsound("sounds/Loop.wav");
clip.loop(Clip.LOOP_CONTINUOUSLY);
		
}
public static void main(String[]args){
	new controller();
}

@Override
public void actionPerformed(ActionEvent e) {
	
	/*How Every button Behaves ,
	 *  This is the longest Method of all. and It's really hard to trace
	 *  if you really need to know how each button works,  
	 * 	I advise you to check what is this button's action command and then search for it using (Ctrl+f)
	*/
	
	
	JButton b = (JButton)e.getSource();
	if(b.getActionCommand().equals("Adjust Names")){
		names = new Names();
		names.getName1().setText(p1);
		names.getName2().setText(p2);
		names.getChange().addActionListener(this);
	}
	
	else if(b.getActionCommand().equals("Change")){
		if(names.getName1().getText().length()>=12||names.getName1().getText().length()<3
				||names.getName2().getText().length()>=12||names.getName2().getText().length()<3)
			names.getTall().setText("(Names Must Be Between 3 and 11 characters)");
		
	else{
		p1 = names.getName1().getText();
		p2 = names.getName2().getText();
		
		if(h1 instanceof Hunter)
			p1text.setText(p1+" Selected Hunter");
		else if(h1 instanceof Mage)
			p1text.setText(p1+" Selected Mage");
		else if(h1 instanceof Paladin)
			p1text.setText(p1+" Selected Paladin");
		else if(h1 instanceof Priest)
			p1text.setText(p1+" Selected Priest");
		else if(h1 instanceof Warlock)
			p1text.setText(p1+" Selected Warlock");
		
		if(h2 instanceof Hunter)
			p2text.setText(p2+" Selected Hunter");
		else if(h2 instanceof Mage)
			p2text.setText(p2+" Selected Mage");
		else if(h2 instanceof Paladin)
			p2text.setText(p2+" Selected Paladin");
		else if(h2 instanceof Priest)
			p2text.setText(p2+" Selected Priest");
		else if(h2 instanceof Warlock)
			p2text.setText(p2+" Selected Warlock");
		
		
		else if(h1 == null && h2 == null){
			p1text.setText(p1 +", Please Select a Hero");
			p2text.setText(p2 +", Please Select a Hero");
		}
		else if(h1 == null)
			p1text.setText(p1+", Please Select a Hero");
		else if(h2 == null)
			p2text.setText(p2+", Please Select a Hero");
		
		names.setVisible(false);
		names.dispose();
		
	}}
	
	else if(b.getActionCommand().equals("Start Game")){
		
		//if both heroes selected hence their texts won't be either "" or "player ,, please select a hero"
		// then we call a new constructor with the passed heroes, 
		//else we set the texts to please select a hero ...
		
		if(h1 != null && h2 !=null){
			try {
				new controller(h1,h2,p1,p2);
				clip.stop();
				screen.dispose();
				if(names != null)
					names.dispose();
				
				if(info !=null)
					info.dispose();
				if(names !=null)
					names.dispose();
				
			} catch (FullHandException | CloneNotSupportedException e1) {
		//It Will never catch these exceptions..
			}

			
		}
		else if(h1 == null && h2 == null){
			p1text.setText(p1 +", Please Select a Hero");
			p2text.setText(p2 +", Please Select a Hero");
		}
		else if(h1 == null)
			p1text.setText(p1+", Please Select a Hero");
		else if(h2 == null)
			p2text.setText(p2+", Please Select a Hero");
}
	
	//The same code for every buttons depending on the number after the class. assign the chosen hero to either h1 or h2
	
	else if(b.getActionCommand().equals("Hunter")){
		try {
			h1 = new Hunter();
			p1text.setText(p1+" Selected Hunter");
		} catch (IOException | CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
}
	else if(b.getActionCommand().equals("Hunter2")){
		try {
			h2 = new Hunter();
			p2text.setText(p2+" Selected Hunter");
		} catch (IOException | CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	else if(b.getActionCommand().equals("Mage")){
		try {
			p1text.setText(p1+" Selected Mage");
			h1 = new Mage();
		} catch (IOException | CloneNotSupportedException e1) {

		}
	}
	else if(b.getActionCommand().equals("Mage2")){
		try {
			p2text.setText(p2+" Selected Mage");
			h2 = new Mage();
		} catch (IOException | CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	else if(b.getActionCommand().equals("Paladin")){
		try {
			p1text.setText(p1+" Selected Paladin");
			h1 = new Paladin();
		} catch (IOException | CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	else if(b.getActionCommand().equals("Paladin2")){
		try {
			p2text.setText(p2+" Selected Paladin");
			h2 = new Paladin();
		} catch (IOException | CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
	else if(b.getActionCommand().equals("Priest")){
		try {
			p1text.setText(p1+" Selected Priest");
			h1 = new Priest();
		} catch (IOException | CloneNotSupportedException e1) {
		}
	}
	else if(b.getActionCommand().equals("Priest2")){
		try {
			p2text.setText(p2+" Selected Priest");
			h2 = new Priest();
		} catch (IOException | CloneNotSupportedException e1) {
		}
	}
		
	else if(b.getActionCommand().equals("Warlock")){
		try {
			p1text.setText(p1+" Selected Warlock");
			h1 = new Warlock();
		} catch (IOException | CloneNotSupportedException e1) {
		}
	}
	else if(b.getActionCommand().equals("Warlock2")){
		try {
			p2text.setText(p2+" Selected Warlock");
			h2 = new Warlock();
		} catch (IOException | CloneNotSupportedException e1) {
			
		}
	}

	else if(b.getActionCommand().equals("End Turn")){
		
		//The Game reseter, Resets heroes hp's and mana , also removes Sleeping Texts from the buttons
		//flips the cards for different turns.

			try {
				model.endTurn();
			} catch (FullHandException e1) {
				/*Making a different Warning that displays the image of the icon that will be burned
				 *instead of using jlabel I decided to go with JTextArea as it's better treated with the borderlayout manager
				 *Check Warning class to get a better idea of how this works altho this code isn't the smartest
				 *but it was based on a jframe class so this is just a relative update and it's working just fine 
				 */
				JTextArea warn = new JTextArea("Hand Is Full,\nThis Card will be Burned");
				warn.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/500));
				warn.setOpaque(false);
				warn.setEditable(false);
				JButton whatever = new JButton(); //this button will never be used 
				//it's just that I didn't add a case to CardPhoto method that returns a Jlabel without having a button as an input 
				whatever.setBounds(0,0,10,10);
				JLabel cardin = CardPhoto(whatever,e1.getBurned());
				cardin.setBounds(screenSize.width/16,screenSize.height/9,47*screenSize.width/350,107*screenSize.height/310);
				war = new warning("");
				war.getPanel().add(warn,BorderLayout.NORTH);
				war.getPanel().add(cardin);
				JOptionPane.showMessageDialog(view, war.getPanel());
				
			} catch (CloneNotSupportedException e1) {
				
				
			}
		if(current.getText().equals(p1+"'s Turn"))
			current.setText(p2+"'s Turn");
		
		else
			current.setText(p1+"'s Turn");
		
		
		updatedata();
				ODeck.setText(""+h2.getDeck().size());
				HDeck.setText(""+h1.getDeck().size());
		resetbooleans();
		target = null;
		
		
		//showing only current hero's turn hand photos in case of turn switching..
		//The logic is based on the fact that every hand card or field card has the same
		//elements and indexes as (HeroButtons/OppButtons),(HFieldButtons/OFieldButtons) respectively
		
		if(model.getCurrentHero() ==h1){
			for(int i = 0; i <HeroButtons.size();i++){
				CardPhoto(HeroButtons.get(i),h1.getHand().get(i));
			}
			for(int j = 0; j<OppButtons.size();j++){
				OppButtons.get(j).setIcon(BasicButton);
				OppButtons.get(j).removeAll();
			}
		}
		
		else if(model.getCurrentHero() ==h2){
			for(int i = 0; i <OppButtons.size();i++){
				CardPhoto(OppButtons.get(i),h2.getHand().get(i));
			}
			for(int j = 0; j<HeroButtons.size();j++){
				HeroButtons.get(j).setIcon(BasicButton);
				HeroButtons.get(j).removeAll();
			}
		}
		for(int s = 0; s<HFieldButtons.size();s++){	//removing sleeping for Hero field buttons
			for(Component comp : HFieldButtons.get(s).getComponents()){
			if(((JLabel)comp).getText().equals("(Sleeping)"))
				HFieldButtons.get(s).remove(comp);
		}}
		for(int h = 0; h<OFieldButtons.size();h++){ // same for Opponent
			for(Component comp : OFieldButtons.get(h).getComponents()){
				if(((JLabel)comp).getText().equals("(Sleeping)"))
				OFieldButtons.get(h).remove(comp);
				
				
				
				
				
		}}
		
		view.revalidate();
		view.repaint();
		
	}
	else if(b.getActionCommand().equals("HeroHandMinion")){
		//adds the button to the field, and to the Arraylist of the respective field.
		//also assign the sleeping status JLabel according to the minion
		resetbooleans();
		int index = HeroButtons.indexOf(b);
		JLabel Sleep = new JLabel("");
		Sleep.setBounds(screenSize.width/192, screenSize.height/108, 11*screenSize.width/192,screenSize.height/36);
		Sleep.setForeground(Color.WHITE);
		Sleep.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
		resetbooleans();
		status.setText("");
		if(((Minion)h1.getHand().get(index)).isSleeping())
		Sleep.setText("(Sleeping)");
	
			try {
				h1.playMinion((Minion) h1.getHand().get(index));
			} catch (NotYourTurnException | NotEnoughManaException
					| FullFieldException e1) {
				updatedata(); newWarning(e1.getMessage());
			}
		if(Sleep.getText().equals("(Sleeping)"))
		b.add(Sleep);
		HFieldButtons.add(b);
		view.getHerofield().add(b);
		HeroButtons.remove(index);
		view.getHerocards().remove(b);
		updatedata();
				
				b.setActionCommand("HeroFieldMinion");
				
				
		
		view.revalidate();
		view.repaint();
		
	}
	else if(b.getActionCommand().equals("OppHandMinion")){
		resetbooleans();
		
		int index = OppButtons.indexOf(b);
		JLabel Sleep = new JLabel("");
		Sleep.setBounds(screenSize.width/192, screenSize.height/108, 11*screenSize.width/192,screenSize.height/36);
		Sleep.setForeground(Color.WHITE);
		Sleep.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
		resetbooleans();
		status.setText("");
		if(((Minion)h2.getHand().get(index)).isSleeping())
		Sleep.setText("(Sleeping)");
			
		try {
			h2.playMinion((Minion) h2.getHand().get(index));
		} catch (NotYourTurnException e1) {
			updatedata(); newWarning(e1.getMessage());
			return;
		} catch (NotEnoughManaException e1) {
		
			updatedata(); newWarning(e1.getMessage());
			return;
		} catch (FullFieldException e1) {
			
			updatedata(); newWarning(e1.getMessage());
			return;
		}
		
		if(Sleep.getText().equals("(Sleeping)"))
		b.add(Sleep);
		
		OppButtons.remove(index);
		OFieldButtons.add(b);
		view.getOppfield().add(b);
		view.getOppcards().remove(b);
		updatedata();
		b.setActionCommand("OppFieldMinion");
	
		
		view.revalidate();
		view.repaint();
		
	}
	else if(b.getActionCommand().equals("HeroFieldMinion")){
		//this checks if the minion you clicked on if he's the first minion to be clicked on 
		//then we know we want to attack with it, we save the attacker minion and set the action command to true
		//else if the attacker command is true attack with the Attacker attribute (which has been saved in step one) the target minion (THIS).
		//same logic for Hero Powers, different booleans.
		
		JLabel Sleep = new JLabel("");
		Sleep.setBounds(screenSize.width/192, screenSize.height/108, 11*screenSize.width/192,screenSize.height/36);
		Sleep.setForeground(Color.WHITE);
		Sleep.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
		Sleep.setText("(Sleeping)");
		//(This will only be used in case of Polymorph)
		
		int index = HFieldButtons.indexOf(b);
		int Adata =0;
		
		if(HLeechboolean == true){
			try {
				h1.castSpell(LeechSpell, h1.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			updatedata();
			resetbooleans();
			view.getHerocards().remove(LeechButton);
			HeroButtons.remove(LeechButton);
			
			
		}
		
		else if(OLeechboolean == true){
			try {
				h2.castSpell(LeechSpell, h1.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			updatedata();
			resetbooleans();
			view.getOppcards().remove(LeechButton);
			OppButtons.remove(LeechButton);
			
			
		}
		
		
		
		else if(HMinionTSpell == true){
			try {
				MightDieMinion = h1.getField().get(index);
				h1.castSpell(MinionTSpell, h1.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException
					| InvalidTargetException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			if(MightDieMinion.getName().equals("Sheep"))
				b.add(Sleep);
			
			if(MightDieMinion.getCurrentHP()>0){
			CardPhoto(b,h1.getField().get(index));}
			
			updatedata();
			resetbooleans();
			view.getHerocards().remove(MinionTSpellButton);
			HeroButtons.remove(MinionTSpellButton);
			
			
		}
		
		else if(OMinionTSpell == true){
			
			try {
				MightDieMinion = h1.getField().get(index);
				h2.castSpell(MinionTSpell, h1.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException
					| InvalidTargetException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			if(MightDieMinion.getName().equals("Sheep"))
				b.add(Sleep);
			
			if(MightDieMinion.getCurrentHP()>0){
				CardPhoto(b,h1.getField().get(index));}
			
			updatedata();
			resetbooleans();
			view.getOppcards().remove(MinionTSpellButton);
			OppButtons.remove(MinionTSpellButton);
			
		}
		
		
		else if(HPriestPower == true){
			target = h1.getField().get(index);
			try {
				((Priest)h1).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				target = null;
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				status.setText("");
				return;
			}
			CardPhoto(b,target);
			resetbooleans();
			updatedata();
		}
	else if(OPriestPower == true){
			target = h1.getField().get(index);
			try {
				((Priest)h2).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				target = null;
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				status.setText("");
				return;
			}
			CardPhoto(b,target);
			resetbooleans();
			updatedata();
		}
		else if(Hmagepower == true){
			target = h1.getField().get(index);
			try {
				((Mage)h1).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				
				target = null;
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				status.setText("");
				return;
			}
			CardPhoto(b,target);
				
				resetbooleans();
				updatedata();
			}
			
		
		
		else if (Omagepower == true){
			target = h1.getField().get(index);
			try {
				((Mage)h2).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				
				target = null;
				status.setText("");
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				
				return;
			}
			status.setText("");
			CardPhoto(b,target);
				
				resetbooleans();
				updatedata();
			
		}
		
		else if(attackcommand ==false){
				attackcommand = true;
			attacker = h1.getField().get(index);
			test =b;
			status.setText("(Attacking)");}
				
			else if(attackcommand==true){
					
					target = h1.getField().get(index);
					if(target == attacker){
						target = null;
						return;
					}
					if(h2.getField().contains(attacker)){
						try {
							h2.attackWithMinion(attacker, target);
						} catch (CannotAttackException | NotYourTurnException
								| TauntBypassException | InvalidTargetException
								| NotSummonedException e1) {
							test = null;
							status.setText("");
							attacker = null;
							target = null;
							attackcommand = false;
							updatedata(); newWarning(e1.getMessage());
							return;
						}}
					else if(h1.getField().contains(attacker)){
						try {
							h1.attackWithMinion(attacker, target);
						} catch (CannotAttackException | NotYourTurnException
								| TauntBypassException | InvalidTargetException
								| NotSummonedException e1) {
							test = null;
							status.setText("");
							attacker = null;
							target = null;
							attackcommand = false;
							updatedata(); newWarning(e1.getMessage());
							return;
						}
					}

			
					CardPhoto(b,target);
					
					if(h1.getField().contains(attacker)){
					Adata = h1.getField().indexOf(attacker);
					CardPhoto(HFieldButtons.get(Adata),attacker);
					}
					else if(h2.getField().contains(attacker)){
					Adata = h2.getField().indexOf(attacker);
		
					CardPhoto(OFieldButtons.get(Adata),attacker);
					}	
					CardPhoto(test,attacker);
					
					test = null;
					attackcommand = false;
					status.setText("");
					
			}
			view.revalidate();
			view.repaint();
		
	}
	
	else if(b.getActionCommand().equals("OppFieldMinion")){
		//Check description above in the HeroFieldMinion.
		
		JLabel Sleep = new JLabel("");
		Sleep.setBounds(screenSize.width/192, screenSize.height/108, 11*screenSize.width/192,screenSize.height/36);
		Sleep.setForeground(Color.WHITE);
		Sleep.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
		Sleep.setText("(Sleeping)");
		//(This will only be used in case of Polymorph)
		
		int index = OFieldButtons.indexOf(b);
		int Adata =0;
		
		if(HLeechboolean == true){
			try {
				h1.castSpell(LeechSpell, h2.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			updatedata();
			resetbooleans();
			view.getHerocards().remove(LeechButton);
			HeroButtons.remove(LeechButton);
			
			
		}
		
		else if(OLeechboolean == true){
			try {
				h2.castSpell(LeechSpell, h2.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			updatedata();
			resetbooleans();
			view.getOppcards().remove(LeechButton);
			OppButtons.remove(LeechButton);
			
			
		}
		
		else if(HMinionTSpell == true){
			try {
				MightDieMinion = h2.getField().get(index);
				h1.castSpell(MinionTSpell, h2.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException
					| InvalidTargetException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			if(MightDieMinion.getName().equals("Sheep"))
				b.add(Sleep);
			
			if(MightDieMinion.getCurrentHP()>0){
			CardPhoto(b,h2.getField().get(index));}
			
			updatedata();
			resetbooleans();
			view.getHerocards().remove(MinionTSpellButton);
			HeroButtons.remove(MinionTSpellButton);
			
			
		}
		
		else if(OMinionTSpell == true){
			
			try {
				MightDieMinion = h2.getField().get(index);
				h2.castSpell(MinionTSpell, h2.getField().get(index));
			} catch (NotYourTurnException | NotEnoughManaException
					| InvalidTargetException e1) {
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			if(MightDieMinion.getName().equals("Sheep"))
				b.add(Sleep);
			
			if(MightDieMinion.getCurrentHP()>0){
			CardPhoto(b,h2.getField().get(index));}
			
			updatedata();
			resetbooleans();
			view.getOppcards().remove(MinionTSpellButton);
			OppButtons.remove(MinionTSpellButton);
			
		}
		
		else if(HPriestPower == true){
			target = h2.getField().get(index);
			try {
				((Priest)h1).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				target = null;
				HPriestPower = false;
				updatedata(); newWarning(e1.getMessage());
				status.setText("");
				return;
			}
			CardPhoto(b,target);
			attackcommand = false;
			OPriestPower = false;
			HPriestPower =false;
			updatedata();
		}
	else if(OPriestPower == true){
			target = h2.getField().get(index);
			try {
				((Priest)h2).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				target = null;
				HPriestPower = false;
				updatedata(); newWarning(e1.getMessage());
				status.setText("");
				return;
			}
			CardPhoto(b,target);
			attackcommand = false;
			OPriestPower = false;
			HPriestPower =false;
			updatedata();
		}
		else if(Hmagepower == true){
			target = h2.getField().get(index);
			try {
				((Mage)h1).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				
				target = null;
				status.setText("");
				resetbooleans();
				updatedata(); newWarning(e1.getMessage());
				
				return;
			}
			updatedata();
			CardPhoto(b,target);
				
			resetbooleans();
				
			
		}
		
		else if (Omagepower == true){
			target = h2.getField().get(index);
			try {
				((Mage)h2).useHeroPower(target);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
				
				target = null;
				status.setText("");
				Omagepower = false;
				updatedata(); newWarning(e1.getMessage());
				
				return;
			}
			updatedata();
			CardPhoto(b,target);
			status.setText("");
				
			resetbooleans();
			
		}
		else if(attackcommand ==false){
				attackcommand = true;
			attacker = h2.getField().get(index);
			test =b;
			status.setText("(Attacking)");}
				
			else if(attackcommand==true){
					target = h2.getField().get(index);
					if(target == attacker){
						target = null;
						return;
					}
					
				
					if(h1.getField().contains(attacker)){
						try {
							h1.attackWithMinion(attacker, target);
						} catch (CannotAttackException | NotYourTurnException
								| TauntBypassException | InvalidTargetException
								| NotSummonedException e1) {
							test = null;
							status.setText("");
							attacker = null;
							target = null;
							attackcommand = false;
							updatedata(); newWarning(e1.getMessage());
							return;
						}}
					else if(h2.getField().contains(attacker)){
						try {
							h2.attackWithMinion(attacker, target);
						} catch (CannotAttackException | NotYourTurnException
								| TauntBypassException | InvalidTargetException
								| NotSummonedException e1) {
							test = null;
							status.setText("");
							attacker = null;
							target = null;
							attackcommand = false;
							updatedata(); newWarning(e1.getMessage());
							return;
						}
					}
					
					CardPhoto(b,target);
					
					if(h1.getField().contains(attacker)){
					Adata = h1.getField().indexOf(attacker);
					CardPhoto(HFieldButtons.get(Adata),attacker);
					}
					else if(h2.getField().contains(attacker)){
					Adata = h2.getField().indexOf(attacker);
					CardPhoto(OFieldButtons.get(Adata),attacker);
					}
					CardPhoto(test,attacker);
					
					test = null;
					attacker = null;
					target = null;
					attackcommand = false;
					status.setText("");
					
					
			}
			view.revalidate();
			view.repaint();
	}
	else if(b.getActionCommand().equals("Opp")){
		//This Buttons represents the Opp Hero (H2), Therefore we always know that it will be attacked or 
		//have hero power be used on it.
		
		if(HHeroTSpell == true){
			try {
				h1.castSpell(HeroTSpell, h2);
			} catch (NotYourTurnException | NotEnoughManaException e1) {
					resetbooleans();
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			updatedata();
			resetbooleans();
			view.getHerocards().remove(HeroTSpellButton);
			HeroButtons.remove(HeroTSpellButton);
			view.revalidate();
			view.repaint();
			
			
		}
	
		else if(OHeroTSpell == true){
			try {
				h2.castSpell(HeroTSpell, h2);
			} catch (NotYourTurnException | NotEnoughManaException e1) {
					resetbooleans();
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			updatedata();
			resetbooleans();
			view.getOppcards().remove(HeroTSpellButton);
			OppButtons.remove(HeroTSpellButton);
			view.revalidate();
			view.repaint();
			
		}
		
		else if(HPriestPower ==true){
			try {
				((Priest)h1).useHeroPower(h2);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					resetbooleans();
					status.setText("");
					updatedata(); newWarning(e1.getMessage());
					return;
			}updatedata();
			resetbooleans();}
		
		
		if(OPriestPower ==true){
			try {
				((Priest)h2).useHeroPower(h2);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					resetbooleans();
					status.setText("");
					updatedata(); newWarning(e1.getMessage());
					return;
			}updatedata();
			resetbooleans();}
		
		
	else if(Omagepower ==true){
			try {
				((Mage)h2).useHeroPower(h2);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					resetbooleans();
					status.setText("");
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			updatedata();
			resetbooleans();}
		
			else if(Hmagepower ==true){
			try {
				((Mage)h1).useHeroPower(h2);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					resetbooleans();
					status.setText("");
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			updatedata();
			resetbooleans();
		}
			else if(attackcommand == false){
				
			// yet to be done
				return;
		}
		else if(attackcommand == true){
			if(h1.getField().contains(attacker)){
			try {
				h1.attackWithMinion(attacker, h2);
			} catch (CannotAttackException | NotYourTurnException
					| TauntBypassException | NotSummonedException
					| InvalidTargetException e1) {
				test = null;
				status.setText("");
				attacker = null;
				attackcommand = false;
				updatedata(); newWarning(e1.getMessage());
				return;
			
			}}
			else if(h2.getField().contains(attacker)){
				try {
					h2.attackWithMinion(attacker, h2);
				} catch (CannotAttackException | NotYourTurnException
						| TauntBypassException | NotSummonedException
						| InvalidTargetException e1) {
					test = null;
					status.setText("");
					attacker = null;
					attackcommand = false;
					updatedata(); newWarning(e1.getMessage());
					return;
				
				}
				
			}

		}
		test = null;
		attacker = null;
		updatedata();
		resetbooleans();
		view.revalidate();
		view.repaint();
		
	}
	else if(b.getActionCommand().equals("Hero")){
		//Same as above, also For no reason Hero is added after opp against the usual.. lol 
		if(HHeroTSpell == true){
			try {
				h1.castSpell(HeroTSpell, h1);
			} catch (NotYourTurnException | NotEnoughManaException e1) {
					resetbooleans();
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			updatedata();
			resetbooleans();
			view.getHerocards().remove(HeroTSpellButton);
			HeroButtons.remove(HeroTSpellButton);
			view.revalidate();
			view.repaint();
			
			
		}
	
		else if(OHeroTSpell == true){
			try {
				h2.castSpell(HeroTSpell, h1);
			} catch (NotYourTurnException | NotEnoughManaException e1) {
					resetbooleans();
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			updatedata();
			resetbooleans();
			view.getOppcards().remove(HeroTSpellButton);
			OppButtons.remove(HeroTSpellButton);
			view.revalidate();
			view.repaint();
			
		}
		
	else if(HPriestPower ==true){
			try {
				((Priest)h1).useHeroPower(h1);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					HPriestPower = false;
					status.setText("");
					updatedata(); newWarning(e1.getMessage());
					return;
			}updatedata();
			OPriestPower = false;
			HPriestPower = false;}
		if(OPriestPower ==true){
			try {
				((Priest)h2).useHeroPower(h1);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					OPriestPower = false;
					status.setText("");
					updatedata(); newWarning(e1.getMessage());
					return;
			}updatedata();
			OPriestPower = false;
			HPriestPower = false;}
		else if(Omagepower ==true){
			try {
				((Mage)h2).useHeroPower(h1);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					Omagepower = false;
					updatedata(); newWarning(e1.getMessage());
					status.setText("");
					return;
			}updatedata();
}
		
			else if(Hmagepower ==true){
			try {
				((Mage)h1).useHeroPower(h1);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
					| NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e1) {
					resetbooleans();
					status.setText("");
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			updatedata();
		}
			else if(attackcommand == false){
			// yet to be done
				return;
		}
		else if(attackcommand == true){
			if(h2.getField().contains(attacker)){
			try {
				h2.attackWithMinion(attacker, h1);
			} catch (CannotAttackException | NotYourTurnException
					| TauntBypassException | NotSummonedException
					| InvalidTargetException e1) {
				test = null;
				status.setText("");
				attacker = null;
				attackcommand = false;
				updatedata(); newWarning(e1.getMessage());
				return;
			
			}}
			else if(h1.getField().contains(attacker)){
				try {
					h1.attackWithMinion(attacker, h1);
				} catch (CannotAttackException | NotYourTurnException
						| TauntBypassException | NotSummonedException
						| InvalidTargetException e1) {
					test = null;
					status.setText("");
					attacker = null;
					attackcommand = false;
					updatedata(); newWarning(e1.getMessage());
					return;
				
				}
				
			}

		}
		test = null;
		attacker = null;
		updatedata();
		resetbooleans();
		view.revalidate();
		view.repaint();
		
	}
	
	else if(b.getActionCommand().equals("HHunterPower")){
		attackcommand = false;
		try {
			h1.useHeroPower();
		} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
				| NotYourTurnException | FullHandException | FullFieldException
				| CloneNotSupportedException e1) {
			updatedata();
			newWarning(e1.getMessage());
			return;
		}
		updatedata();	
		view.revalidate();
		view.repaint();
		
	}
	else if(b.getActionCommand().equals("OHunterPower")){
		attackcommand = false;
		try {
			h2.useHeroPower();
		} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
				| NotYourTurnException | FullHandException | FullFieldException
				| CloneNotSupportedException e1) {
			updatedata();
			newWarning(e1.getMessage());
			return;
		}
		updatedata();
		
		view.revalidate();
		view.repaint();
		
	}
	else if(b.getActionCommand().equals("HMagePower")){
		resetbooleans();
			Hmagepower =true;
			status.setText("(Using Mage Power)");
	}
	
	else if(b.getActionCommand().equals("OMagePower")){
		resetbooleans();
			Omagepower =true;
			status.setText("(Using Mage Power)");
	}
	else if(b.getActionCommand().equals("HPriestPower")){
		resetbooleans();
		HPriestPower = true;
		status.setText("(Using Priest Power)");
	}
	else if(b.getActionCommand().equals("OPriestPower")){
		resetbooleans();
		OPriestPower = true;
		status.setText("(Using Priest Power)");
		
	}
	else if(b.getActionCommand().equals("HPaladinPower")){
		try {
			((Paladin)h1).useHeroPower();
		} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
				| NotYourTurnException | FullHandException | FullFieldException
				| CloneNotSupportedException e1) {
				updatedata();
				newWarning(e1.getMessage());
				return;
		}
		JLabel Sleep = new JLabel("(Sleeping)");
		Sleep.setBounds(screenSize.width/192, screenSize.height/108, 11*screenSize.width/192,screenSize.height/36);
		Sleep.setForeground(Color.WHITE);
		Sleep.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
		JButton pminion = new JButton("");
		pminion.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
		pminion.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
		pminion.addActionListener(this);
		pminion.addMouseListener(this);
		pminion.setActionCommand("HeroFieldMinion");
//		Minion silverHand = new Minion("Silver Hand Recruit", 1, Rarity.BASIC, 1, 1, false, false, false);
		CardPhoto(pminion,h1.getField().get(h1.getField().size()-1));
		pminion.add(Sleep);
		HFieldButtons.add(pminion);
		view.getHerofield().add(pminion);
		updatedata();
		
	}
	else if(b.getActionCommand().equals("OPaladinPower")){
		try {
			((Paladin)h2).useHeroPower();
		} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
				| NotYourTurnException | FullHandException | FullFieldException
				| CloneNotSupportedException e1) {
				updatedata();
				newWarning(e1.getMessage());
				return;
		}
		JLabel Sleep = new JLabel("(Sleeping)");
		Sleep.setBounds(screenSize.width/192, screenSize.height/108, 11*screenSize.width/192,screenSize.height/36);
		Sleep.setForeground(Color.WHITE);
		Sleep.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
		JButton pminion = new JButton("");
		pminion.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
		pminion.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
		pminion.addActionListener(this);
		pminion.addMouseListener(this);
		pminion.setActionCommand("OppFieldMinion");
	//	Minion silverHand = new Minion("Silver Hand Recruit", 1, Rarity.BASIC, 1, 1, false, false, false);
		CardPhoto(pminion,h2.getField().get(h2.getField().size()-1));
		pminion.add(Sleep);
		OFieldButtons.add(pminion);
		view.getOppfield().add(pminion);
		updatedata();
	}
	else if(b.getActionCommand().equals("HWarlockPower")){

		try {
			((Warlock)h1).useHeroPower();
		} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
				| NotYourTurnException | FullFieldException
				| CloneNotSupportedException e1) {
			updatedata(); newWarning(e1.getMessage());
			
				return;
		} catch (FullHandException e1) {
			updatedata();
			JTextArea warn = new JTextArea("Hand Is Full,\nThis Card will be Burned");
			warn.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/500));
			warn.setOpaque(false);
			warn.setEditable(false);
			JButton whatever = new JButton(); //this button will never be used 
			//it's just that I didn't add a case to CardPhoto method that returns a Jlabel without having a button as an input 
			whatever.setBounds(0,0,10,10);
			JLabel cardin = CardPhoto(whatever,e1.getBurned());
			cardin.setBounds(screenSize.width/16,screenSize.height/9,47*screenSize.width/350,107*screenSize.height/310);
			war = new warning("");
			war.getPanel().add(warn,BorderLayout.NORTH);
			war.getPanel().add(cardin);
			JOptionPane.showMessageDialog(view, war.getPanel());
		}
		updatedata();
	}
	
	else if(b.getActionCommand().equals("OWarlockPower")){
		
		try {
			((Warlock)h2).useHeroPower();
		} catch (NotEnoughManaException | HeroPowerAlreadyUsedException
				| NotYourTurnException | FullFieldException
				| CloneNotSupportedException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
		} catch (FullHandException e1) {
			updatedata();
			JTextArea warn = new JTextArea("Hand Is Full,\nThis Card will be Burned");
			warn.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/500));
			warn.setOpaque(false);
			warn.setEditable(false);
			JButton whatever = new JButton(); //this button will never be used 
			//it's just that I didn't add a case to CardPhoto method that returns a Jlabel without having a button as an input 
			whatever.setBounds(0,0,10,10);
			JLabel cardin = CardPhoto(whatever,e1.getBurned());
			cardin.setBounds(screenSize.width/16,screenSize.height/9,47*screenSize.width/350,107*screenSize.height/310);
			war = new warning("");
			war.getPanel().add(warn,BorderLayout.NORTH);
			war.getPanel().add(cardin);
			JOptionPane.showMessageDialog(view, war.getPanel());
		}
		updatedata();
	}
	else if(b.getActionCommand().equals("HeroHandSpell")){
		int index = HeroButtons.indexOf(b);
		
		if(b.getName().equals("Twisting Nether")){
			try {
				h1.castSpell((AOESpell)h1.getHand().get(index), h2.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
					updatedata(); newWarning(e1.getMessage());
					return;
			}
			HFieldButtons = new ArrayList<JButton>();
			OFieldButtons = new ArrayList<JButton>();
			view.getHerofield().removeAll();
			view.getOppfield().removeAll();
			
		}
		
		else if(b.getName().equals("Curse of Weakness")){
			try {
				h1.castSpell((AOESpell)h1.getHand().get(index), h2.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			
			for(int i =0;i<h2.getField().size();i++){
				CardPhoto(OFieldButtons.get(i),h2.getField().get(i));
			}
			
		}
		else if(b.getName().equals("Level Up!")){
			try {
				h1.castSpell((FieldSpell)h1.getHand().get(index));
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h1.getField().size();i++){
				CardPhoto(HFieldButtons.get(i),h1.getField().get(i));
			}
		}
		else if(b.getName().equals("Flamestrike")){
			try {
				h1.castSpell((AOESpell)h1.getHand().get(index), h2.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h2.getField().size();i++){
				CardPhoto(OFieldButtons.get(i),h2.getField().get(i));
			}
			
			
		}
		else if(b.getName().equals("Holy Nova")){
			try {
				h1.castSpell((AOESpell)h1.getHand().get(index), h2.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h2.getField().size();i++){
				CardPhoto(OFieldButtons.get(i),h2.getField().get(i));
			}
			for(int i =0;i<h1.getField().size();i++){
				CardPhoto(HFieldButtons.get(i),h1.getField().get(i));
			}
			
		}
		
		else if(b.getName().equals("Multi-Shot")){
			try {
				h1.castSpell((AOESpell)h1.getHand().get(index), h2.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h2.getField().size();i++){
				CardPhoto(OFieldButtons.get(i),h2.getField().get(i));
			}
			
			
		}
		
		else if(h1.getHand().get(index) instanceof LeechingSpell){
			resetbooleans();
			HLeechboolean = true;
			LeechSpell = (LeechingSpell)h1.getHand().get(index);
			LeechButton =b;
			status.setText("Siphon Soul");
			return;
			
		}
		
		
		else{ if(h1.getHand().get(index) instanceof MinionTargetSpell){
			resetbooleans();
			HMinionTSpell = true;
			MinionTSpell = (MinionTargetSpell)h1.getHand().get(index);
			MinionTSpellButton = b;
			status.setText(("Target Spell"));
			
			
		}
		
		if(h1.getHand().get(index) instanceof HeroTargetSpell){
			resetbooleans();
			HHeroTSpell = true;
			HMinionTSpell = true;
			HeroTSpell = (HeroTargetSpell)h1.getHand().get(index);
			HeroTSpellButton = b;
			status.setText(("Target Spell"));
			
		}return;}
		
		
		
		
		updatedata();
		HeroButtons.remove(b);
		view.getHerocards().remove(b);
		view.revalidate();
		view.repaint();
		
		
	}
	else if(b.getActionCommand().equals("OppHandSpell")){
		int index = OppButtons.indexOf(b);
		
		if(b.getName().equals("Twisting Nether")){
			try {
				h2.castSpell((AOESpell)h2.getHand().get(index), h1.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
					updatedata(); newWarning(e1.getMessage());
					return;
			    }
			HFieldButtons = new ArrayList<JButton>();
			OFieldButtons = new ArrayList<JButton>();
			view.getHerofield().removeAll();
			view.getOppfield().removeAll();
		}
		else if(b.getName().equals("Curse of Weakness")){
			try {
				h2.castSpell((AOESpell)h2.getHand().get(index), h1.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
	
			for(int i =0;i<h1.getField().size();i++){
				CardPhoto(HFieldButtons.get(i),h1.getField().get(i));
			}
	
		}
		else if(b.getName().equals("Level Up!")){
			try {
				h2.castSpell((FieldSpell)h2.getHand().get(index));
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h2.getField().size();i++){
				CardPhoto(OFieldButtons.get(i),h2.getField().get(i));
			}
		}
		else if(b.getName().equals("Flamestrike")){
			try {
				h2.castSpell((AOESpell)h2.getHand().get(index), h1.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h1.getField().size();i++){
				CardPhoto(HFieldButtons.get(i),h1.getField().get(i));
			}
			
			
		}
		
		else if(b.getName().equals("Holy Nova")){
			try {
				h2.castSpell((AOESpell)h2.getHand().get(index), h1.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h2.getField().size();i++){
				CardPhoto(OFieldButtons.get(i),h2.getField().get(i));
			}
			for(int i =0;i<h1.getField().size();i++){
				CardPhoto(HFieldButtons.get(i),h1.getField().get(i));
			}
			
		}
		
		else if(b.getName().equals("Multi-Shot")){
			try {
				h2.castSpell((AOESpell)h2.getHand().get(index), h1.getField());
			} catch (NotYourTurnException | NotEnoughManaException e1) {
				updatedata(); newWarning(e1.getMessage());
				return;
			}
			for(int i =0;i<h1.getField().size();i++){
				CardPhoto(HFieldButtons.get(i),h1.getField().get(i));
			}
			
			
		}
		
		
		else if(h2.getHand().get(index) instanceof LeechingSpell){
			resetbooleans();
			OLeechboolean = true;
			LeechSpell = (LeechingSpell)h2.getHand().get(index);
			LeechButton =b;
			status.setText("Siphon Soul");
			return;
			
		}
		
		else{ if(h2.getHand().get(index) instanceof MinionTargetSpell){
			resetbooleans();
			OMinionTSpell = true;
			MinionTSpell = (MinionTargetSpell)h2.getHand().get(index);
			MinionTSpellButton = b;
			status.setText(("MinionTarget Spell"));
		}
		if(h2.getHand().get(index) instanceof HeroTargetSpell){
			resetbooleans();
			OHeroTSpell = true;
			OMinionTSpell = true;
			HeroTSpell = (HeroTargetSpell)h2.getHand().get(index);
			HeroTSpellButton = b;
			status.setText(("Target Spell"));
			
		}return;}
		
		updatedata();
		OppButtons.remove(b);
		view.getOppcards().remove(b);
		view.revalidate();
		view.repaint();
		
	}
	
	
	else if(b.getActionCommand().equals("Stop Music")){
		if(clip.isRunning()){
			b.setText("Start Music");
			b.setActionCommand("Stop Music");
			clip.stop();
		}
		else{
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			b.setText("Stop Music");
		}
	}
	
	else if(b.getActionCommand().equals("Info")){
		info =new Credits();
		
	}
	
	else if (b.getActionCommand().equals("New Game")){
		this.gameover.dispose();
		this.view.dispose();
		new controller();	
	}
	}



@Override
public void onGameOver() {
	String winner = "";
	
	if(h1.getCurrentHP() ==0)
		winner = p2;
	else
		winner = p1;
	
	if(info !=null)
		info.dispose();
	
	if(names !=null)
		names.dispose();
	
	gameover = new GameOver(winner);
	gameover.getNew().addActionListener(this);
	//view.setVisible(false);
	clip.stop();
	playsound("sounds/Victory.wav");
	this.view.dispose();
		
}

//This Method was only implemented for testing purposed without adding any photos to the game
//You don't really need to check it out..

/*public static String CardData(Card c){
	String rarity ="";
	String taunt ="";
	String divine ="";
	String charge ="";
	
	if(c.getRarity()==Rarity.COMMON)
		rarity ="<br/> Common";
	else if(c.getRarity()==Rarity.LEGENDARY)
		rarity ="<br/> Legendary";
	else if(c.getRarity()==Rarity.BASIC)
		rarity ="<br/> Basic";
	else if(c.getRarity()==Rarity.EPIC)
		rarity ="<br/> Epic";
	else if(c.getRarity()==Rarity.RARE)
		rarity ="<br/> Rare";
	if(c instanceof Minion){
		if(((Minion)c).isDivine())
			divine = "<br/> Divine Shield";
		if(!((Minion)c).isSleeping())
			charge = "<br/> Charge";
		if(((Minion)c).isTaunt())
			taunt = "<br/> Taunt";
		
		return "<html>" +"("+ c.getName()+")" +"<br/>" +"Mana Cost: "
		+c.getManaCost()+ rarity + taunt
		+ divine+charge+"<br/>"+"Attack: " +((Minion)c).getAttack()+"<br/>"+"CurrentHP: "+ ((Minion)c).getCurrentHP()+"</html>";
	}
	else
		return "<html>"+c.getName()+"<br/>"+"Mana Cost: " +c.getManaCost() +rarity+"</html>";
	
} */
public JLabel CardPhoto(JButton b, Card c){
	//This method does 2 thing: Adding photos + mana + health attributes to the button (b) taken from card C
	// returning a JLabel with the same Image,health,mana (Will be referenced as Pmana and Phealth) however with larger size...
	//to be used as the enlarged photo image when someone hover over the button.
	//Check mouseEntered method below to understand how this is used)
	
	//(I decided to use null layout in the label and the button so that I can place the inside-Jlabels wherever I Want)
	//it makes the code much harder to read but it's working flawlessly.
	//every button has Mana, Health JLabels inside of them that corresponds to the input card's attributes
	//the same thing is added to the JLabel Pphoto as this JLabel will be Used later to magnify photos when buttons are being hovered on
	//the buttons also have (Sleeping) JLabel that's added when clicked on the button if it's a hand minion.
	//this is why when I'm Clearing the JLabels I avoid clearing the Sleeping JLabels.
	
	// Button adding Mana + current HP + Attack
	JLabel mana = new JLabel("");
	JLabel health = new JLabel("");
	JLabel attack = new JLabel("");
	JLabel taunt = new JLabel("(Taunt)");
	JLabel divine = new JLabel("(Divine)");
	
	taunt.setBounds(screenSize.width/80, screenSize.height/10, 11*screenSize.width/160,screenSize.height/36);
	taunt.setForeground(Color.WHITE);
	taunt.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
	
	divine.setBounds(screenSize.width/80, screenSize.height/15, 11*screenSize.width/160,screenSize.height/36);
	divine.setForeground(Color.WHITE);
	divine.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/100));
	
	mana.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
	mana.setForeground(Color.WHITE);
	mana.setBounds(0, 0, 5*screenSize.width/250,5*screenSize.height/200);
	
	health.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
	health.setForeground(Color.WHITE);
	health.setBounds(screenSize.width/17,31*screenSize.height/220,5*screenSize.width/250,5*screenSize.height/200);
	
	attack.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/80));
	attack.setForeground(Color.WHITE);
	attack.setBounds(screenSize.width/384,31*screenSize.height/220,5*screenSize.width/250,5*screenSize.height/200);
	
	for(Component comp : b.getComponents()){
			if(!(((JLabel)comp).getText().equals(("(Sleeping)"))))
			b.remove(comp);
	}
	//removing Jlabels if the button contains any in case of updating after an attack
	
	b.setLayout(null);
							
	b.add(health);
	b.add(mana);
	b.add(attack);
	 // updating mana component (JLabel)
	//((JLabel)(b.getComponent(1))).setText(""+c.getManaCost()); ( discoverd that I don't need to use component since I delete all anyways)
	mana.setText(""+c.getManaCost());
	
	JLabel Phealth = new JLabel(""); // for magnified photo
	JLabel Pattack = new JLabel("");
	
	if(c instanceof Minion){ // Modifying attack only if the Card is a minion
		if(((Minion)c).getCurrentHP()>=10) //(spacing for double digits-hp minions is different) ( Trial&error approach)
		health.setBounds(screenSize.width/20,31*screenSize.height/220,5*screenSize.width/250,5*screenSize.height/200);
	
		//((JLabel)(b.getComponent(0))).setText("" + ((Minion)c).getCurrentHP()); (same discovery)
		health.setText("" + ((Minion)c).getCurrentHP());
		Phealth.setText("" + ((Minion)c).getCurrentHP());
		attack.setText(""+((Minion)c).getAttack());
		Pattack.setText(""+((Minion)c).getAttack());
		if(((Minion)c).getAttack()>=10)
			attack.setBounds(0,31*screenSize.height/220,5*screenSize.width/250,5*screenSize.height/200);
		
	if(((Minion)c).isDivine())
	b.add(divine);
	if(((Minion)c).isTaunt())
		b.add(taunt);
	}

	
	//adding mana + current hp + attack to the output JLabel
	JLabel Pphoto = new JLabel();
	Pphoto.setPreferredSize(new Dimension(47*screenSize.width/350,107*screenSize.height/310));
	Pphoto.setBounds(0,0,47*screenSize.width/350,107*screenSize.height/310);
	JLabel Pmana = new JLabel(""+c.getManaCost()); // for magnified photo

	Phealth.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/40));
	Phealth.setBounds(11*screenSize.width/96,37*screenSize.height/125,5*screenSize.width/192,5*screenSize.height/108);
	Pattack.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/40));
	Pattack.setBounds(screenSize.width/192,37*screenSize.height/125,5*screenSize.width/192,5*screenSize.height/108);
	Pattack.setForeground(Color.WHITE);
	
	
	if(c instanceof Minion){
		if(((Minion)c).getCurrentHP()>=10){ //(spacing for double digits-hp minions is different) ( Trial&error approach)
			Phealth.setBounds(7*screenSize.width/64,37*screenSize.height/125,5*screenSize.width/182,5*screenSize.height/98);
			Phealth.setFont(new Font("Arial Black", Font.PLAIN, screenSize.width/50));}
	
		if(((Minion)c).getAttack()>=10){
			Pattack.setBounds(0,37*screenSize.height/132,5*screenSize.width/160,5*screenSize.height/60);
			Pattack.setFont(new Font("Arial Black", Font.BOLD, screenSize.width/50));}
	}
	
	Phealth.setForeground(Color.WHITE);
	Pmana.setFont(new Font("Arial Black", Font.BOLD, screenSize.width/40));
	Pmana.setBounds(screenSize.width/384,0,5*screenSize.width/192,screenSize.height/12);
	if(c.getManaCost()>=10){
		Pmana.setFont(new Font("Arial Black", Font.BOLD, screenSize.width/50));
		Pmana.setBounds(0,0,5*screenSize.width/160,5*screenSize.height/60);
		}
	Pmana.setForeground(Color.WHITE);
	Pphoto.removeAll();
	Pphoto.setLayout(null);
	Pphoto.add(Phealth); 
	Pphoto.add(Pmana);
	Pphoto.add(Pattack);
	
	
	//Adding Specific Photo for each Button + generating a bigger sized JLabel with the same ImageIcon \\
	
	if(c.getName().equals("Wolfrider")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Wolfrider.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Wolfrider.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Goldshire Footman")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Goldshire Footman.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Goldshire Footman.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Stonetusk Boar")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Stonetusk Boar.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Stonetusk Boar.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Bloodfen Raptor")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Bloodfen Raptor.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Bloodfen Raptor.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Frostwolf Grunt")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Frostwolf Grunt.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Frostwolf Grunt.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Chilwind Yeti")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Chilwind Yeti.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Chilwind Yeti.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Boulderfist Ogre")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Boulderfist Ogre.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Boulderfist Ogre.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Core Hound")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Core Hound.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Core Hound.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Argent Commander")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Argent Commander.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Argent Commander.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Sunwalker")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Sunwalker.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Sunwalker.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Chromaggus")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Chromaggus.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Chromaggus.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("The LichKing")){
		b.setIcon(getScaledImage(new ImageIcon("Images/The LichKing.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/The LichKing.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Icehowl")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Icehowl.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Icehowl.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Colossus of the Moon")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Colossus of the Moon.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Colossus of the Moon.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Curse of Weakness")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Curse of Weakness.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Curse of Weakness.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Divine Spirit")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Divine Spirit.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Divine Spirit.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Flamestrike")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Flamestrike.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Flamestrike.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Holy Nova")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Holy Nova.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Holy Nova.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Kill Command")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Kill Command.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Kill Command.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Level Up!")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Level Up!.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Level Up!.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Multi-Shot")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Multi-Shot.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Multi-Shot.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Polymorph")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Polymorph.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Polymorph.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Pyroblast")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Pyroblast.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Pyroblast.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Seal of Champions")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Seal of Champions.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Seal of Champions.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Shadow Word: Death")){ // Image not the same name cause of ":"
		b.setIcon(getScaledImage(new ImageIcon("Images/Shadow Word Death.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Shadow Word Death.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Siphon Soul")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Siphon Soul.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Siphon Soul.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("Twisting Nether")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Twisting Nether.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Twisting Nether.png"), Pphoto.getWidth(), Pphoto.getHeight()));
	}
	else if (c.getName().equals("King Krush")){
		b.setIcon(getScaledImage(new ImageIcon("Images/King Krush.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/King Krush.png"), Pphoto.getWidth(), Pphoto.getHeight()));
		
	}
	else if (c.getName().equals("Kalycgos")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Kalycgos.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Kalycgos.png"), Pphoto.getWidth(), Pphoto.getHeight()));
		
	}
	else if (c.getName().equals("Wilfred Fizzlebang")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Wilfred Fizzlebang.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Wilfred Fizzlebang.png"), Pphoto.getWidth(), Pphoto.getHeight()));
		
	}
	else if (c.getName().equals("Prophet Velen")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Prophet Velen.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Prophet Velen.png"), Pphoto.getWidth(), Pphoto.getHeight()));
		
	}
	else if (c.getName().equals("Tirion Fordring")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Tirion Fordring.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Tirion Fordring.png"), Pphoto.getWidth(), Pphoto.getHeight()));
		
	}
	else if (c.getName().equals("Silver Hand Recruit")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Silver Hand Recruit.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Silver Hand Recruit.png"), Pphoto.getWidth(), Pphoto.getHeight()));
		
	}
	
	else if (c.getName().equals("Sheep")){
		b.setIcon(getScaledImage(new ImageIcon("Images/Sheep.png"), b.getWidth(), b.getHeight()));
		Pphoto.setIcon(getScaledImage(new ImageIcon("Images/Sheep.png"), Pphoto.getWidth(), Pphoto.getHeight()));
		
	}
	
//	else
	//	return new JLabel(CardData(c));
	
	return Pphoto;
}

@Override
public void onEndTurn(Card c) {
	
	if(c == null){
		updatedata();
		
		if(model.getCurrentHero()==h1){
			Hfatigue++;
		newWarning("Your Deck Is Empty, You will take " + Hfatigue + " fatigue damage");}
		
		else{
			Ofatigue++;
			newWarning("Your Deck Is Empty, You will take " + Ofatigue + " fatigue damage");}
	}
else{
	JButton card = new JButton("");
	card.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
	card.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
	CardPhoto(card,c);
	card.addActionListener(this);
	card.addMouseListener(this);
	
	JButton copy = new JButton("  ");
	copy.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
	copy.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
	CardPhoto(copy,c);
	copy.setHorizontalTextPosition(JButton.CENTER);
	copy.addActionListener(this);
	copy.addMouseListener(this);
	


	
	if(model.getCurrentHero()==h1){
		if(c instanceof Minion){
			card.setActionCommand("HeroHandMinion");
			copy.setActionCommand("HeroHandMinion");}

		else{
			card.setActionCommand("HeroHandSpell");
			copy.setActionCommand("HeroHandSpell");
			spellaction(card,c);
			spellaction(copy,c);}
		
	HeroButtons.add(card);
	view.getHerocards().add(card);
	for(Minion m:h1.getField()){
		if(m.getName().equals("Chromaggus")&&HeroButtons.size()<10){
			HeroButtons.add(copy);
			view.getHerocards().add(copy);
		}
			
	}
	}
	
	
	
	
	else if(model.getCurrentHero()==h2){
		if(c instanceof Minion){
			card.setActionCommand("OppHandMinion");
			copy.setActionCommand("OppHandMinion");}

		else{
			card.setActionCommand("OppHandSpell");
			copy.setActionCommand("OppHandSpell");
			spellaction(card,c);
			spellaction(copy,c);}
		
	OppButtons.add(card);
	view.getOppcards().add(card);}
	for(Minion m:h2.getField()){
		if(m.getName().equals("Chromaggus")&&OppButtons.size()<10){
			OppButtons.add(copy);
			view.getOppcards().add(copy);
		}
			
	}
	

	}
	view.revalidate();
	view.repaint();
}

public void playsound(String path){
	try {
		AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
		clip = AudioSystem.getClip();
		clip.open(audio);
		clip.start();
	} catch (UnsupportedAudioFileException | IOException e) {
	} catch (LineUnavailableException e) {

	}
}
public static ImageIcon getScaledImage(ImageIcon srcImg, int w, int h){
    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = resizedImg.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(srcImg.getImage(), 0, 0, w, h, null);
    g2.dispose();

    return new ImageIcon(resizedImg);
}
public void updatedata(){
	Omana.setText(""+h2.getCurrentManaCrystals()+"/"+h2.getTotalManaCrystals()); 
	Ohealth.setText(""+h2.getCurrentHP());
	if(h2.getCurrentHP()<10)
		Ohealth.setBounds(58*screenSize.width/189,screenSize.height/11,5*screenSize.width/64,5*screenSize.height/54);
	if(h1.getCurrentHP()<10)
		Hhealth.setBounds(58*screenSize.width/189,screenSize.height/28,5*screenSize.width/48,5*screenSize.height/54);
	
	Hmana.setText(""+h1.getCurrentManaCrystals()+"/"+h1.getTotalManaCrystals()); 
	Hhealth.setText(""+h1.getCurrentHP());
	status.setText("");
}

@Override
public void mouseClicked(MouseEvent arg0) {
	
	
}

@Override
public void mouseEntered(MouseEvent e) {
	//Adds the JLabel Created using the CardPhoto method (implemented Above) 
	//to the Photo panel that's in the Card Info Panel in the view of the game.

	//checks if it's a field minion or a hand card that's to the current hero playing then displays it.
	//else it displays the basic card icon.
	
	JButton b = (JButton)e.getSource();
	JLabel NinaPower = new JLabel(); // (TO whoever reads this go listen to Hozier's Nina cried power song it's amazing lol)
	NinaPower.setPreferredSize(new Dimension(47*screenSize.width/350,107*screenSize.height/310));
	NinaPower.setBounds(0,0,47*screenSize.width/350,107*screenSize.height/310);
	//Since The Hero Power Buttons aren't added to the CardPhoto method cases I implemented them here separately
	
	int index = 0;
	
	if(b.getActionCommand().equals("HeroFieldMinion")){
		index = HFieldButtons.indexOf(b);
		JLabel mag = CardPhoto(b,h1.getField().get(index));
		photo.removeAll();
		photo.add(mag);
		
	}
	else if(b.getActionCommand().equals("OppFieldMinion")){
		index = OFieldButtons.indexOf(b);
		JLabel mag = CardPhoto(b,h2.getField().get(index));
		photo.removeAll();
		photo.add(mag);
		
	}
	else if(b.getActionCommand().equals("OppHandMinion")||b.getActionCommand().equals("OppHandSpell")){
		if(model.getCurrentHero()==h2){
			index = OppButtons.indexOf(b);
			JLabel mag = CardPhoto(b,h2.getHand().get(index));
			photo.removeAll();
			photo.add(mag);
		}
		else{
			photo.removeAll();
			photo.add(Basic);
		}
	}
	else if(b.getActionCommand().equals("HeroHandMinion")||b.getActionCommand().equals("HeroHandSpell")){
		if(model.getCurrentHero()==h1){
			index = HeroButtons.indexOf(b);
			JLabel mag = CardPhoto(b,h1.getHand().get(index));
			photo.removeAll();
			photo.add(mag);
		}
		else{
			photo.removeAll();
			photo.add(Basic);
		}
		
	}
	else if(b.getActionCommand().equals("HMagePower")||b.getActionCommand().equals("OMagePower")){
		NinaPower.setIcon(getScaledImage(new ImageIcon("Images/MagePower.png"), NinaPower.getWidth(), NinaPower.getHeight()));
		photo.removeAll();
		photo.add(NinaPower);
	}
	else if(b.getActionCommand().equals("HHunterPower")||b.getActionCommand().equals("OHunterPower")){
		NinaPower.setIcon(getScaledImage(new ImageIcon("Images/HunterPower.png"), NinaPower.getWidth(), NinaPower.getHeight()));
		photo.removeAll();
		photo.add(NinaPower);
	}
	else if(b.getActionCommand().equals("HPriestPower")||b.getActionCommand().equals("OPriestPower")){
		NinaPower.setIcon(getScaledImage(new ImageIcon("Images/PriestPower.png"), NinaPower.getWidth(), NinaPower.getHeight()));
		photo.removeAll();
		photo.add(NinaPower);
	}
	else if(b.getActionCommand().equals("HPaladinPower")||b.getActionCommand().equals("OPaladinPower")){
		NinaPower.setIcon(getScaledImage(new ImageIcon("Images/PaladinPower.png"), NinaPower.getWidth(), NinaPower.getHeight()));
		photo.removeAll();
		photo.add(NinaPower);
	}
	else if(b.getActionCommand().equals("HWarlockPower")||b.getActionCommand().equals("OWarlockPower")){
		NinaPower.setIcon(getScaledImage(new ImageIcon("Images/WarlockPower.png"), NinaPower.getWidth(), NinaPower.getHeight()));
		photo.removeAll();
		photo.add(NinaPower);
	}
	
	photo.revalidate();
	photo.repaint();

}

@Override
public void mouseExited(MouseEvent e) {
	photo.removeAll();
	photo.revalidate();
	photo.repaint();
	
}

@Override
public void mousePressed(MouseEvent arg0) {
	
}

@Override
public void mouseReleased(MouseEvent arg0) {
	
}
public void newWarning(String s){
	war =new warning(s);
	JOptionPane.showMessageDialog(view, war.getPanel());
}


@Override
public void onWarlockCard2(Card c) {
	
	if(c == null){
		updatedata();
		
		if(model.getCurrentHero()==h1){
			Hfatigue++;
		newWarning("Your Deck Is Empty, You will take " + Hfatigue + " fatigue damage");}
		
		else{
			Ofatigue++;
			newWarning("Your Deck Is Empty, You will take " + Ofatigue + " fatigue damage");}
	}
else{
	JButton card = new JButton("");
	card.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
	card.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
	CardPhoto(card,c);
	card.addActionListener(this);
	card.addMouseListener(this);
	
	JButton copy = new JButton("  ");
	copy.setPreferredSize(new Dimension(13*screenSize.width/192,screenSize.height/6));
	copy.setBounds(0,0,13*screenSize.width/192,screenSize.height/6);
	CardPhoto(copy,c);
	copy.setHorizontalTextPosition(JButton.CENTER);
	copy.addActionListener(this);
	copy.addMouseListener(this);
	


	
	if(model.getCurrentHero()==h1){
		if(c instanceof Minion){
			card.setActionCommand("HeroHandMinion");
			copy.setActionCommand("HeroHandMinion");}

		else{
			card.setActionCommand("HeroHandSpell");
			copy.setActionCommand("HeroHandSpell");
			spellaction(card,c);
			spellaction(copy,c);}
		
	HeroButtons.add(card);
	view.getHerocards().add(card);
	for(Minion m:h1.getField()){
		if(m.getName().equals("Chromaggus")&&HeroButtons.size()<10){
			HeroButtons.add(copy);
			view.getHerocards().add(copy);
		}
			
	}
	}
	
	
	
	
	else if(model.getCurrentHero()==h2){
		if(c instanceof Minion){
			card.setActionCommand("OppHandMinion");
			copy.setActionCommand("OppHandMinion");}

		else{
			card.setActionCommand("OppHandSpell");
			copy.setActionCommand("OppHandSpell");
			spellaction(card,c);
			spellaction(copy,c);}
		
	OppButtons.add(card);
	view.getOppcards().add(card);}
	for(Minion m:h2.getField()){
		if(m.getName().equals("Chromaggus")&&OppButtons.size()<10){
			OppButtons.add(copy);
			view.getOppcards().add(copy);
		}
			
	}
	

	}
	view.revalidate();
	view.repaint();
}


public void spellaction(JButton card, Card c){
		if(c.getName().equals("Twisting Nether"))
		card.setName("Twisting Nether");
		else if (c.getName().equals("Curse of Weakness"))
			card.setName("Curse of Weakness");
		else if (c.getName().equals("Divine Spirit"))
			card.setName("Divine Spirit");
		else if (c.getName().equals("Flamestrike"))
			card.setName("Flamestrike");
		else if (c.getName().equals("Holy Nova"))
			card.setName("Holy Nova");
		else if (c.getName().equals("Kill Command"))
			card.setName("Kill Command");
		else if (c.getName().equals("Level Up!"))
			card.setName("Level Up!");
		else if (c.getName().equals("Multi-Shot"))
			card.setName("Multi-Shot");
		else if (c.getName().equals("Polymorph"))
			card.setName("Polymorph");
		else if (c.getName().equals("Pyroblast"))
			card.setName("Pyroblast");
		else if (c.getName().equals("Seal of Champions"))
			card.setName("Seal of Champions");
		else if (c.getName().equals("Shadow Word: Death"))
			card.setName("Shadow Word: Death");
		else if (c.getName().equals("Siphon Soul"))
			card.setName("Siphon Soul");
			}


@Override
public void onMinionDeath3(Minion m) {
	int index =0;
	
	if(h1.getField().contains(m)){
		index = h1.getField().indexOf(m);
		view.getHerofield().remove(HFieldButtons.get(index));
		HFieldButtons.remove(index);
		h1.getField().remove(m);
	}
	else{
		index = h2.getField().indexOf(m);
		view.getOppfield().remove(OFieldButtons.get(index));
		OFieldButtons.remove(index);
		h2.getField().remove(m);
	}
	
}

public void resetbooleans(){
	
	HPriestPower = false;
	Omagepower = false;
	Hmagepower = false;
	attackcommand = false;
	OPriestPower = false;
	OMinionTSpell=false;
	HMinionTSpell=false;
	OHeroTSpell =false;
	HHeroTSpell = false;
	OLeechboolean = false;
	HLeechboolean = false;
}

//And Finally For Whoever Reads This, (and I seriously doubt that someone ever will)
//Thank you.
//Enjoy your day.

}