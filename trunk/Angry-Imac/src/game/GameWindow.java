package game;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class GameWindow extends JFrame implements ActionListener{
	private JButton quitButton;
	private JButton pauseButton;
	private JMenuBar menuBar;
	private JPanel contenu;
	private GameWorld g; 
	private Thread gw;
	private boolean alive = true;
	
	public GameWindow(){
		super();
		g = new GameWorld();
		gw = new Thread(g);
		buildWindow();
		gw.start();
	}
	
	//creation de la fenetre
	public void buildWindow(){
		setTitle("AngrIMAC");
		setSize(1024,700);
		setLocationRelativeTo(null); //On centre la fen�tre sur l'Žcran
		setResizable(false); //On interdit pour le moment le redimensionnement
		contenu = new JPanel();
		contenu.setLayout(new BorderLayout());
		
		buildMenu();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//ajout des boutons
		contenu.add("South", buildButtons());
		
		//creation de la zone de jeu
		contenu.add(g.gg,"Center");
		
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
	

}
