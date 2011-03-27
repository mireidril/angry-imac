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
	private JMenuBar menuBar;
	private JPanel contenu;
	private GameWorld g; 
	private Thread gw;
	private boolean alive = true;
	
	private Vec2 posBaseSouris;
	private final float tensionElastique = 10;
	
	public GameWindow(){
		super();
		g = new GameWorld();
		gw = new Thread(g);
		posBaseSouris = new Vec2();
		buildWindow();
		gw.start();
	}
	
	//creation de la fenetre
	public void buildWindow(){
		setTitle("AngrIMAC");
		setSize(1024,700);
		setLocationRelativeTo(null); //On centre la fenï¿½tre sur l'Å½cran
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
		//System.out.println("Pressed !");
		posBaseSouris = new Vec2(evt.getX(), evt.getY());
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		//System.out.println("released !");
		Body b = g.physicalBodies.get(0);
		Vec2 vectForce = new Vec2((posBaseSouris.x - evt.getX()) *b.getMass()*tensionElastique, (posBaseSouris.y - evt.getY())*b.getMass()*tensionElastique);
		b.applyForce(vectForce, b.getPosition());
	}
}
