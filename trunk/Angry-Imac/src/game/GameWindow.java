package game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import object.Projectile;
import object.Target;

import org.jbox2d.collision.MassData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import parser.ParserXML;

/**
 * Classe de la fenetre du jeu
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY C≈Ωline, KIELB Adrien et ROLDAO Timoth≈Ωe
 * @version 1.0
 */
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
	private JButton selectWorldButton;
	
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
	private JPanel selectWorld;
	private JPanel failed;
	private JPanel next;
	
	private Vec2 posBaseSouris;
	private Vec2 posDragSouris;
	
	private boolean helpScreen = false;
	private boolean creditsScreen = false;
	
	/**
	 * Constructeur de la fenetre de jeu
	 */
	public GameWindow(){
		super();
		
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		
		buildWindow();
	}
	
	/**
	 * Creation de la fenetre de jeu
	 */
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
		helpButton.setBounds(467,260,88,49);
		helpButton.setBorderPainted(false);
		helpButton.setContentAreaFilled(false);
		helpButton.addActionListener(this);
		
		creditsButton = new JButton(new ImageIcon("textures/home/credits.png"));
		creditsButton.setBounds(448,320,130,48);
		creditsButton.setBorderPainted(false);
		creditsButton.setContentAreaFilled(false);
		creditsButton.addActionListener(this);
		
		selectWorldButton = new JButton(new ImageIcon("textures/home/select.png"));
		selectWorldButton.setBounds(415,380,200,48);
		selectWorldButton.setBorderPainted(false);
		selectWorldButton.setContentAreaFilled(false);
		selectWorldButton.addActionListener(this);
		
		//creation du bouton retour sur la page d'accueil
		returnHomeButton = new JButton(new ImageIcon("textures/return.png"));
		returnHomeButton.setBorderPainted(false);
		returnHomeButton.setContentAreaFilled(false);
		returnHomeButton.addActionListener(this);
		
		resetButton = new JButton("Reset");
		resetButton.setBounds(0,0,130,48);
		resetButton.setContentAreaFilled(false);
		resetButton.addActionListener(this);
		
		//creation des boutons failed
		quitButtonFailed = new JButton(new ImageIcon("textures/failed/quit.png"));
		quitButtonFailed.setBounds(550,340,83,48);
		quitButtonFailed.setBorderPainted(false);
		quitButtonFailed.setContentAreaFilled(false);
		quitButtonFailed.addActionListener(this);
		
		retryButtonFailed = new JButton(new ImageIcon("textures/failed/retry.png"));
		retryButtonFailed.setBounds(540,280,103,48);
		retryButtonFailed.setBorderPainted(false);
		retryButtonFailed.setContentAreaFilled(false);
		retryButtonFailed.addActionListener(this);
		
		nextButton = new JButton(new ImageIcon("textures/nextButton.png"));
		nextButton.setBounds(480,280,187,48);
		nextButton.setBorderPainted(false);
		nextButton.setContentAreaFilled(false);
		nextButton.addActionListener(this);
		
		buildMenu();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		goToHome();
		
	}
	
	/**
	 * Affichage du menu de jeu
	 */
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
		contenu.add(selectWorldButton);
		contenu.add(start);
		setContentPane(contenu);
	}
	
	public void gameFailed()
	{
		failed = new JPanel(){
			public void paint(Graphics g){
				Image img=null;
				try {
		        	img=ImageIO.read(new File("textures/failed/failed.png"));
		        	g.drawImage(img, 0, 0, null);
		        }
		        catch(IOException e){
		        	System.out.println("ok");System.exit(0);
		        }
			}
		};
		failed.setSize(1024, 600);
		contenu.removeAll();
		
		contenu.add("South", buildButtons());
		contenu.add(retryButtonFailed);
		contenu.add(quitButtonFailed);
		contenu.add(failed);
		setContentPane(contenu);
		
		gw.suspend();
	}
	public void gameNext()
	{
		next = new JPanel(){
			public void paint(Graphics g){
				Image img=null;
				try {
		        	img=ImageIO.read(new File("textures/next.png"));
		        	g.drawImage(img, 0, 0, null);
		        }
		        catch(IOException e){
		        	System.out.println("ok");System.exit(0);
		        }
			}
		};
		next.setSize(1024, 600);
		contenu.removeAll();
		
		contenu.add("South", buildButtons());
		contenu.add(nextButton);
		contenu.add(next);
		setContentPane(contenu);
		
		gw.suspend();
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
		
		return buttonPanel;
	}
	
	private void launchGame(int lvl, boolean forcage){
		contenu.add("South", buildButtons());
		
		//creation du monde et de son thread
		g = new GameWorld(this, lvl, forcage);
		gw = new Thread(g);
		posBaseSouris = new Vec2();
		posDragSouris = new Vec2();
		
		//creation de la zone de jeuheight
		contenu.add(g.gg);
		
		gw.start();
		
		setContentPane(contenu);
			
		//ajout du listener de la souris
		this.addMouseListener(this);
		
	
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
	        g.loadWorldReset(g.lvl);
			
			//creation de la zone de jeuheight
	        contenu.add(g.gg);
			setContentPane(contenu);
			gw.resume();
				
			//ajout du listener de la souris
			this.addMouseListener(this);
		}
		else if(source == nextButton){
			System.out.println("next");
			contenu.removeAll();
	        alive = false;
	        g.lvl++;
	        
	        while(g.runEngaged){}
	        g.loadWorldReset(g.lvl);
	        
			//creation de la zone de jeuheight
	        contenu.add(g.gg);
			setContentPane(contenu);
			gw.resume();
			
			//ajout du listener de la souris
			this.addMouseListener(this);
		}
		else if(source == playButton)
		{
			System.out.println("Play");
			
			//suppression de l'ecran d'accueil
			contenu.remove(start);
			contenu.remove(playButton);
			contenu.remove(helpButton);
			contenu.remove(creditsButton);
			contenu.remove(selectWorldButton);
			
			launchGame(1, false);
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
			contenu.remove(selectWorldButton);
			
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
			creditsScreen = true;
			returnHomeButton.setBounds(400,380,85,24);
			
			//suppression de l'ecran d'accueil
			contenu.remove(start);
			contenu.remove(playButton);
			contenu.remove(helpButton);
			contenu.remove(creditsButton);
			contenu.remove(selectWorldButton);
			
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
		else if(source == selectWorldButton)
		{
			returnHomeButton.setBounds(840,340,85,24);
			
			//suppression de l'ecran d'accueil
			contenu.remove(start);
			contenu.remove(playButton);
			contenu.remove(helpButton);
			contenu.remove(creditsButton);
			contenu.remove(selectWorldButton);
			
			selectWorld = new JPanel(){
				public void paint(Graphics g){
					Image img=null;
					try {
			        	img=ImageIO.read(new File("textures/selectScreen.jpg"));
			        	g.drawImage(img, 0, 0, null);
			        }
			        catch(IOException e){
			        	System.out.println("ok");System.exit(0);
			        }
				}
			};
			selectWorld.setSize(1024, 600);
			ParserXML parser = new ParserXML();
			parser.parseSave();
			int i;
			int j;
			for(i = 1; i <= 3; i++){
				for(j = 1; j<=5; j++){
					JButton lvl1;
					//System.out.println(parser.isUnlock(1));
					if(parser.isUnlock(5*(i-1)+j)){
						lvl1=  new JButton(new ImageIcon("textures/unlock.png"));
					}
					else{
						lvl1=  new JButton(new ImageIcon("textures/lock.png"));
						lvl1.setEnabled(false);
					}
					lvl1.setBounds(95+j*130,150+i*100,30,30);
					lvl1.setBorderPainted(false);
					lvl1.setContentAreaFilled(false);
					final int lvltmp = 5*(i-1)+j;
					System.out.println(5*(i-1)+j);
					lvl1.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							helpScreen = false;
							creditsScreen = false;
							contenu.removeAll();
							launchGame(lvltmp, true);
						}
					});
					contenu.add(lvl1);
					JLabel title = new JLabel("Level "+(5*(i-1)+j));
					title.setFont(new Font("Arial", Font.BOLD, 18));
					title.setForeground(new Color(110,47,2));
					title.setBounds(80+j*130,50+i*100,100,160);
					JLabel score;
					if(parser.isUnlock(5*(i-1)+j))
						score = new JLabel(Integer.toString(parser.getScore(5*(i-1)+j))+" pts");
					else
						score = new JLabel(" Lock");
					score.setBounds(90+j*130,140+i*100,100,100);
					score.setForeground(new Color(255,150,0));
					contenu.add(title);
					contenu.add(score);
				}
			}
			contenu.add(returnHomeButton);
			contenu.add(selectWorld);
			setContentPane(contenu);
			this.addMouseListener(this);
		}
		else if(source == returnHomeButton)
		{
			//Suppression des ≈Ωl≈Ωmentes de la page Help
			if(helpScreen){
				contenu.remove(help);
			}
			else if(creditsScreen){
				contenu.remove(credits);
			}
			else{
				contenu.remove(selectWorld);
				contenu.removeAll();
			}
			contenu.remove(returnHomeButton);
			//Retour a la page d'accueil
			goToHome();
			this.addMouseListener(this);
			helpScreen = false;
			creditsScreen = false;
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
			//On récupère la munition précédemment lancée
			int actual = g.catapult.getActualMunition() - 1;
			if(actual >= 0 && actual < g.getWorld().munitions.size()) {
				Body munition = g.getWorld().munitions.get(actual);
				Projectile p = (Projectile) munition.getUserData();
				if(p.type == 1) {
					System.out.println("Ecrasement !!!!");
					munition.applyForce(new Vec2(0, 1000*1000*1000), munition.getPosition());
					p.type--;
				}
			}
				
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
				Projectile p = (Projectile) munition.getUserData();
				p.throwed = true;
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
			munition.putToSleep();
			if(munition != null) {
				Vec2 position = munition.getPosition();
				if(evt.getX() > 0 && evt.getX() < 150 && evt.getY() > 350 && evt.getY() < 520) {
					munition.setXForm(new Vec2(evt.getX(), (evt.getY()) - 30), 0);
				}
				else {
					int x = evt.getX(), y = evt.getY();
					if(evt.getX() > 150){
						x = 150;
					}
					if(evt.getX() < 10 ) {
						x = 10;
					}
					
					if(evt.getY() > 540){
						y =  540 - 30;
						
					}
					if(evt.getY() < 350 ) {
						y = 350 - 30;
					}
					
					if(x != -1) {
						
					}
					munition.setXForm(new Vec2(x, y), 0);
				}
				munition.allowSleeping(true);
			}
		}		
	}

	
	@Override
	public void mouseMoved(MouseEvent evt) {
	}
	

	
	@Override
	public void keyTyped(KeyEvent evt) {
		System.out.println("tamere");
		if(evt.getKeyChar() == ' '){
			//On récupère la munition précédemment lancée
			int actual = g.catapult.getActualMunition() - 1;
			if(actual >= 0 && actual < g.getWorld().munitions.size()) {
				Body munition = g.getWorld().munitions.get(actual);
				Projectile p = (Projectile) munition.getUserData();
				if(p.type == 1) {
					System.out.println("Ecrasement !!!!");
					munition.applyForce(new Vec2(0, 1000*1000*1000), munition.getPosition());
					p.type--;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent evt) {
	}
}
