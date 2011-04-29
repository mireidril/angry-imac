package game;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import object.Target;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

@SuppressWarnings("serial")
public class GameWindow extends JFrame implements ActionListener, MouseListener, KeyListener, MouseMotionListener{

	//boutons visibles pendant le jeu
	private JButton quitButton;
	private JButton pauseButton;
	JButton resetButton;
	private JButton nextButton;
	
	//Boutons visibles sur la page de titre
	private JButton playButton;
	private JButton helpButton;
	private JButton creditsButton;
	
	//Bouton retour sur la page d'accueil
	private JButton returnHomeButton;
	
	//Bouton failed
	private JButton quitButtonFailed;
	private JButton retryButtonFailed;
	
	private JMenuBar menuBar;
	JPanel contenu;
	private GameWorld g; 
	Thread gw;
	private boolean alive = true;
	private JPanel start;
	private JPanel help;
	private JPanel credits;
	private JPanel failed;
	
	private Vec2 posBaseSouris;
	private Vec2 posDragSouris;
	
	private boolean helpScreen = false;
	
	public GameWindow(){
		super();
		buildWindow();
		this.setFocusable(true);
		this.requestFocus();
	}
	
	//creation de la fenetre
	public void buildWindow(){
		setTitle("AngrIMAC");
		setSize(1024,700);
		setLocationRelativeTo(null); //On centre la fenetre sur l'ecran
		setResizable(false); //On interdit pour le moment le redimensionnement
		contenu = new JPanel();
		contenu.setLayout(new BorderLayout());
		
		//Creation des boutons du menu de la page d'accueil
		playButton = new JButton(new ImageIcon("textures/home/play.png"));
		playButton.setBounds(469,200,86,48);
		playButton.setBorderPainted(false);
		playButton.setContentAreaFilled(false);
		playButton.addActionListener(this);
		
		helpButton = new JButton(new ImageIcon("textures/home/help.png"));
		helpButton.setBounds(467,270,88,49);
		helpButton.setBorderPainted(false);
		helpButton.setContentAreaFilled(false);
		helpButton.addActionListener(this);
		
		creditsButton = new JButton(new ImageIcon("textures/home/credits.png"));
		creditsButton.setBounds(448,340,130,48);
		creditsButton.setBorderPainted(false);
		creditsButton.setContentAreaFilled(false);
		creditsButton.addActionListener(this);
		
		//crtion du bouton retour sur la page d'accueil
		returnHomeButton = new JButton(new ImageIcon("textures/return.png"));
		returnHomeButton.setBorderPainted(false);
		returnHomeButton.setContentAreaFilled(false);
		returnHomeButton.addActionListener(this);
		
		resetButton = new JButton("Reset");
		resetButton.setBounds(0,0,130,48);
		resetButton.setContentAreaFilled(false);
		resetButton.addActionListener(this);
		
		//crtion des boutons failed
		quitButtonFailed = new JButton(new ImageIcon("textures/quit.png"));
		quitButtonFailed.setBounds(550,340,83,48);
		quitButtonFailed.setBorderPainted(false);
		quitButtonFailed.setContentAreaFilled(false);
		quitButtonFailed.addActionListener(this);
		
		retryButtonFailed = new JButton(new ImageIcon("textures/retry.png"));
		retryButtonFailed.setBounds(540,280,103,48);
		retryButtonFailed.setBorderPainted(false);
		retryButtonFailed.setContentAreaFilled(false);
		retryButtonFailed.addActionListener(this);
		
		buildMenu();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		goToHome();
		this.addKeyListener(this);
		this.addMouseMotionListener(this); 
	}
	
	public void goToHome()
	{
		start = new JPanel(){
			public void paint(Graphics g){
				//chargement texture start
				Image img=null;
				try {
		        	img=ImageIO.read(new File("textures/home/startScreen.jpg"));
		        	g.drawImage(img, 0, 0, null);
		        }
		        catch(IOException e){
		        	System.out.println("ok");System.exit(0);
		        }
			}
		};
		start.setSize(1024, 600);
		contenu.add(playButton);
		contenu.add(helpButton);
		contenu.add(creditsButton);
		contenu.add(start);
		setContentPane(contenu);
	}
	
	public void gameFailed()
	{
		failed = new JPanel(){
			public void paint(Graphics g){
				Image img=null;
				try {
		        	img=ImageIO.read(new File("textures/failed.png"));
		        	g.drawImage(img, 0, 0, null);
		        }
		        catch(IOException e){
		        	System.out.println("ok");System.exit(0);
		        }
			}
		};
		failed.setSize(1024, 600);
		contenu.removeAll();
		contenu.add(retryButtonFailed);
		contenu.add(quitButtonFailed);
		contenu.add(failed);
		setContentPane(contenu);
	}
	
	public void buildMenu(){
		menuBar = new JMenuBar();

		JMenu menu1 = new JMenu("Menu");
		JMenuItem quitter = new JMenuItem(new QuitterAction("Quitter"));
		menu1.add(quitter);
		menuBar.add(menu1);

		JMenu menu2 = new JMenu("?");
		JMenuItem aPropos = new JMenuItem(new AProposAction(this, "A propos"));
		menu2.add(aPropos);
		menuBar.add(menu2);
		
		setJMenuBar(menuBar);
	}
    
	//creation des boutons
	private JPanel buildButtons(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setSize(200, 100);
		buttonPanel.setBounds(100, 100, 500, 500);
		
		quitButton = new JButton(new ImageIcon("textures/bois.jpg"));
		quitButton.addActionListener(this);
		quitButton.setBorderPainted(false);
		quitButton.setContentAreaFilled(false);
		buttonPanel.add(quitButton);
		
		pauseButton = new JButton("Pause");
		pauseButton.setContentAreaFilled(false);
		pauseButton.addActionListener(this);
		buttonPanel.add(pauseButton);
		
		nextButton = new JButton("Next Level");
		nextButton.setContentAreaFilled(false);
		nextButton.addActionListener(this);
		nextButton.setVisible(false);
		buttonPanel.add(nextButton);
		
		return buttonPanel;
	}
	
	public void displayNextButton(){
		nextButton.setVisible(true);
		System.out.println("affichage");
		while(!nextButton.isVisible())
			nextButton.setVisible(true);
	}
	
	public void hideNextButton(){
		nextButton.setVisible(false);
		while(nextButton.isVisible())
			nextButton.setVisible(false);
	}
	
	//ajout des listeners
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == quitButton || source == quitButtonFailed){
			gw.stop();
			System.out.println("retour au menu principal");
			System.exit(NORMAL);
		} 
		else if(source == pauseButton){
			if(alive){
				gw.suspend();
				alive = false;
			}
			else{
				gw.resume();
				alive = true;
			}
			System.out.println("Pause");
		}
		else if(source == retryButtonFailed){
			System.out.println("reset");
			
			contenu.removeAll();
	        alive = false;
	        //while(g.runEngaged){}
	        //g.loadWorldReset(g.lvl);
			//g.resetWorld();
		}
		else if(source == nextButton){
			System.out.println("next");
			nextButton.setVisible(false);
	        alive = false;
	        g.lvl++;
	        while(g.runEngaged){}
	        g.loadWorldReset(g.lvl);
			//g.resetWorld();
	        nextButton.setVisible(false);
		}
		else if(source == playButton)
		{
			System.out.println("Play");
			
			//suppression de l'ecran d'accueil
			contenu.remove(start);
			contenu.remove(playButton);
			contenu.remove(helpButton);
			contenu.remove(creditsButton);
			
			//ajout des boutons
			contenu.add("South", buildButtons());
			
			//creation du monde et de son thread
			g = new GameWorld(this);
			gw = new Thread(g);
			posBaseSouris = new Vec2();
			posDragSouris = new Vec2();
			
			//creation de la zone de jeuheight
			contenu.add(resetButton);
			contenu.add(g.gg);
			
			gw.start();
			
			setContentPane(contenu);
				
			//ajout du listener de la souris
			this.addMouseListener(this);
		}
		else if(source == helpButton)
		{
			helpScreen = true;
			returnHomeButton.setBounds(300,380,85,24);
			
			//suppression de l'ecran d'accueil
			contenu.remove(start);
			contenu.remove(playButton);
			contenu.remove(helpButton);
			contenu.remove(creditsButton);
			
			help = new JPanel(){
				public void paint(Graphics g){
					//chargement texture start
					Image img=null;
					try {
			        	img=ImageIO.read(new File("textures/helpScreen.jpg"));
			        	g.drawImage(img, 0, 0, null);
			        }
			        catch(IOException e){
			        	System.out.println("ok");System.exit(0);
			        }
				}
			};
			help.setSize(1024, 600);
			contenu.add(returnHomeButton);
			contenu.add(help);
			setContentPane(contenu);
			this.addMouseListener(this);
		}
		else if(source == creditsButton)
		{
			returnHomeButton.setBounds(400,380,85,24);
			
			//suppression de l'ecran d'accueil
			contenu.remove(start);
			contenu.remove(playButton);
			contenu.remove(helpButton);
			contenu.remove(creditsButton);
			
			credits = new JPanel(){
				public void paint(Graphics g){
					//chargement texture start
					Image img=null;
					try {
			        	img=ImageIO.read(new File("textures/creditsScreen.jpg"));
			        	g.drawImage(img, 0, 0, null);
			        }
			        catch(IOException e){
			        	System.out.println("ok");System.exit(0);
			        }
				}
			};
			credits.setSize(1024, 600);
			contenu.add(returnHomeButton);
			contenu.add(credits);
			setContentPane(contenu);
			this.addMouseListener(this);
		}
		else if(source == returnHomeButton)
		{
			//Suppression des lmentes de la page Help
			if(helpScreen == true)
			{
				contenu.remove(help);
			}
			else
			{
				contenu.remove(credits);
			}
			contenu.remove(returnHomeButton);
			//Retour a la page d'accueil
			goToHome();
			this.addMouseListener(this);
			helpScreen = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
	}

	@Override
	public void mouseEntered(MouseEvent evt) {
		//System.out.println("Enter !");
	}

	@Override
	public void mouseExited(MouseEvent evt) {
		//System.out.println("Exit ! ");
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		System.out.println("Souris : ("+evt.getX()+", "+evt.getY()+")");
		System.out.println("Presssssed");
		
		/*for(Body link : g.catapult.getRope()) {
			System.out.println("Souris : ("+evt.getX()+", "+evt.getY()+") Link : (" + link.getPosition().x + ", "+ link.getPosition().y);
			if( evt.getX() >= link.getPosition().x && evt.getX() <= (link.getPosition().x + 10))
			{
				posBaseSouris = new Vec2(evt.getX(), evt.getY());
				System.out.println("rope");
				Vec2 vectForce = new Vec2((posDragSouris.x - evt.getX()) *link.getMass()*400, (posDragSouris.y - evt.getY())*link.getMass()*400);
				link.applyForce(vectForce, link.getPosition());
				posDragSouris = new Vec2(evt.getX(), evt.getY());
			}
		}*/
		
		if( (evt.getX() >= 105 && evt.getX() <= 136) && (evt.getY() >= 485 && evt.getY() <= 529)) 
		{
			posBaseSouris = new Vec2(evt.getX(), evt.getY());
			g.catapult.setEngaged(true);
		}
		else
		{
			posBaseSouris = new Vec2(0, 0);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		//System.out.println("released !");
		if(posBaseSouris.x != 0 && posBaseSouris.y != 0 && g.catapult.getEngaged()) {
			g.catapult.setEngaged(false);
			Body munition = g.getActualMunitionBody(g.catapult.getActualMunition());
			if(munition != null && g.incrementsActualMunition()) {
				munition.wakeUp();
				Vec2 vectForce = new Vec2((posBaseSouris.x - evt.getX()) *munition.getMass()*g.catapult.getElasticTension(), (posBaseSouris.y - evt.getY())*munition.getMass()*g.catapult.getElasticTension());
				munition.applyForce(vectForce, munition.getPosition());
				
				//Déplacement de la munition suivante sur le lance-pierre
				System.out.println("getActualMunition" + g.catapult.getActualMunition());
				/*if(g.catapult.getActualMunition() < g.catapult.projectiles.size()) {
					g.setProjectileToActive(g.catapult.projectiles.get(g.catapult.getActualMunition()));
					Body nextMunition = g.getActualMunitionBody(g.catapult.getActualMunition());
					if(nextMunition != null) {
						nextMunition.setXForm(new Vec2(120, 450), 0);
					}
				}*/
			}
			posBaseSouris.x = 0;
			posBaseSouris.y = 0;
		}
		else {
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent evt) {
		if(g.catapult.getEngaged()) {
			Body munition = g.getActualMunitionBody(g.catapult.getActualMunition());
			if(munition != null) {
				Vec2 position = munition.getPosition();
				if(evt.getX() > 50 && evt.getX() < 150 && evt.getY() > 400 && evt.getY() < 520) {
					munition.setXForm(new Vec2(evt.getX(), (evt.getY()) - 30), 0);
				}
			}
		}		
	}

	@Override
	public void mouseMoved(MouseEvent evt) {
	}
	
	@Override
	public void keyTyped(KeyEvent evt) {

		/*if(evt.getKeyChar() == ' '){
			System.out.println("test : touche espace appuye");
			
			//ajout des boutons
			contenu.add("South", buildButtons());
			
			//suppression de l'ecran d'accueil
			contenu.remove(start);
			
			//creation du monde et de son thread
			g = new GameWorld(this);
			gw = new Thread(g);
			posBaseSouris = new Vec2();
			posDragSouris = new Vec2();
			gw.start();
			
			//creation de la zone de jeu
			contenu.add(g.gg, "Center");
			
			setContentPane(contenu);
			
			//ajout du listener de la souris
			this.addMouseListener(this);
		}*/
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}
}
