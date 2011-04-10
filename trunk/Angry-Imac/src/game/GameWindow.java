package game;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

@SuppressWarnings("serial")
public class GameWindow extends JFrame implements ActionListener, MouseListener{
	private JButton quitButton;
	private JButton pauseButton;
	private JButton resetButton;
	private JMenuBar menuBar;
	private JPanel contenu;
	private GameWorld g; 
	private Thread gw;
	private boolean alive = true;
	
	private Vec2 posBaseSouris;
	private Vec2 posDragSouris;
	
	public GameWindow(){
		super();
		g = new GameWorld();
		gw = new Thread(g);
		posBaseSouris = new Vec2();
		posDragSouris = new Vec2();
		buildWindow();
		gw.start();
	}
	
	//creation de la fenetre
	public void buildWindow(){
		setTitle("AngrIMAC");
		setSize(1024,700);
		setLocationRelativeTo(null); //On centre la fenetre sur l'ecran
		setResizable(false); //On interdit pour le moment le redimensionnement
		contenu = new JPanel();
		contenu.setLayout(new BorderLayout());
		
		buildMenu();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//ajout des boutons
		contenu.add("South", buildButtons());
		
		//creation de la zone de jeu
		contenu.add(g.gg,"Center");
		
		this.addMouseListener(this);
		
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
		buttonPanel.setSize(1024, 100);
		
		quitButton = new JButton("Quitter");
		quitButton.addActionListener(this);
		buttonPanel.add(quitButton);
		
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(this);
		buttonPanel.add(pauseButton);
		
		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		buttonPanel.add(resetButton);
		
		return buttonPanel;
	}
	
	//ajout des listeners
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == quitButton){
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
		else if(source == resetButton){
			/*gw.stop();
			g = new GameWorld();
			gw = new Thread(g);
			gw.start();*/
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		//L'action se fait une fois qu'on a relaché le doigt sur la souris
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
		}
		else
		{
			posBaseSouris = new Vec2(0, 0);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		//System.out.println("released !");
		if(posBaseSouris.x != 0 && posBaseSouris.y != 0) {
			Body munition = g.getActualMunition();
			if(munition != null && g.incrementsActualMunition()) {
				Vec2 vectForce = new Vec2((posBaseSouris.x - evt.getX()) *munition.getMass()*g.catapult.getElasticTension(), (posBaseSouris.y - evt.getY())*munition.getMass()*g.catapult.getElasticTension());
				munition.applyForce(vectForce, munition.getPosition());
				
				//Déplacement de la munition suivante sur le lance-pierre
				Body nextMunition = g.getActualMunition();
				if(nextMunition != null) {
					nextMunition.setXForm(new Vec2(125, 450), 0);
				}
			}
			posBaseSouris.x = 0;
			posBaseSouris.y = 0;
			
		}
		else {
			System.out.println("lol");
			
		}
	}
}
