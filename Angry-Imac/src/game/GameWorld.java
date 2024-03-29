package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;

import object.*;
import parser.ParserXML;

/**
 * Classe de l'application du jeu
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Celine, KIELB Adrien et ROLDAO Timothee
 * @version 1.0
 */
public class GameWorld implements Runnable{
	
	/**
	 * Classe du rendu graphique du jeu
	 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY C�line, KIELB Adrien et ROLDAO Timoth�e
	 * @version 1.0
	 */
	@SuppressWarnings("serial")
	private class GameGraphic extends JPanel{
		/**
		 * fonction permettant la remise a 0 des translations pour l'affichage
		 * @param g2 element de dessin 2D
		 */
        public void resetTrans(Graphics2D g2){
            AffineTransform at = new AffineTransform();
            at.setToIdentity();
            g2.setTransform(at);
        }
        /**
         * fonction affichant les differents elements du jeu (boutons, objets, fond, score, ...)
         */
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			int xProjectile = -1, yProjectile = -1;
			boolean throwed = true;
			
			//********************************* affichage du ciel *********************************
			g2.setPaint(textSky);
			g2.fillRect(0, 0, 1024, 600);

			//********************************* affichage rectangle du sol *************************
			g.fillRect(0, 520, 1024, 80);
			
			//********************************* affichage de la catapulte *************************
			g2.setPaint(catapult.textCatapult);
			g2.fillRect(100, 430, 43, 107);
			
			g2.translate(90,70);
			gameWindow.getScoreLabel().paint(g2);
			resetTrans(g2);
			
			g2.translate(20,10);
			gameWindow.getQuitButtonHUD().paint(g2);
			resetTrans(g2);
			
			g2.translate(120,10);
			gameWindow.getReloadButtonHUD().paint(g2);
			resetTrans(g2);
			
			g2.translate(170,10);
			gameWindow.getPauseButtonHUD().paint(g2);
			resetTrans(g2);
			
			//********************************* affichage des munitions/projectiles + elastique*****************
			if(alive)
			{
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (int i = 0; i < catapult.projectiles.size(); i++){
            	resetTrans(g2);          	
            	Projectile p = catapult.projectiles.get(i);
            	
            	if(p != null) {
            		g2.setColor(p.getMaterial().getColor());
                	g2.rotate(p.getAngle());
                	
            		if(p.active == true && i<m_world.munitions.size()){
            			Body b = m_world.munitions.get(i);            			
            			p = (Projectile) b.getUserData();            	
            			g2.translate(b.getWorldCenter().x, b.getWorldCenter().y);
            			xProjectile = (int)b.getWorldCenter().x;
            			yProjectile = (int)b.getWorldCenter().y;
            			throwed = p.throwed;
            		}
            		else {
            			g2.translate(p.getPosition().x, p.getPosition().y);
            			xProjectile = (int)p.getPosition().x;
            			yProjectile = (int)p.getPosition().y;
            		}
	               
	                //chargemen de la texture
	                g2.setPaint(p.getMaterial().getTexture());
	                
	                switch(p.getShape()){
		                case RAMP : // meme trace que pour triangle
		                case TRIANGLE :
		                	Polygon pol = new Polygon();
		                	List<Vec2> list = p.getVertices();
		                	for(int id = 0; id < 3; id++)
		                		pol.addPoint((int)(list.get(id).x),(int)(list.get(id).y+0.2*p.getWidth()));
		                	g2.fillPolygon(pol);
		                	break;
		                case CIRCLE :
		                	g2.fillOval((int)(-p.getWidth()/2), (int)(-p.getHeight()/2),(int)(p.getWidth()), (int)(p.getHeight()));
		                	break;
		                case BOX :
		                	g2.fillRect((int)(-p.getWidth()/2), (int)(-p.getHeight()/2),(int)(p.getWidth()), (int)(p.getHeight()));
		                	break;
		                case TARGET :
		                	Polygon pTar = new Polygon();
		                	List<Vec2> listTar = p.getVertices();
		                	for(int id = 0; id < listTar.size(); id++)
		                		pTar.addPoint((int)(listTar.get(id).x),(int)(listTar.get(id).y+0.2*p.getWidth()));
		                	g2.fillPolygon(pTar);
		                	break;
		                default :
		                	break;
	                }
            	}
            	//********************************** affichage elastique *****************************
                g2.setColor(new Color(0, 0, 0));
                g2.translate(-xProjectile, -yProjectile);
                if(!throwed && catapult.getEngaged()) {
                	g2.drawLine(xProjectile, yProjectile, 108, 450);
                	g2.drawLine(xProjectile, yProjectile, 130, 450);
                	xProjectile = -1;
                	yProjectile = -1;
                	throwed = true;
                }
            }
            
			//********************************* affichage des objets mouvants **********************
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            
			if(m_world.physicalBodies.size() > 0){
				try{
	            for (Body body : m_world.physicalBodies){
	            	if(body != null){
		                resetTrans(g2);
		                Block tmp = (Block) body.getUserData(); // recuperation du block lie a l'objet pour avoir ses caracteristiques
			                g2.setColor(tmp.getMaterial().getColor());
			                g2.translate(body.getWorldCenter().x, body.getWorldCenter().y);
			                g2.rotate(body.getAngle());
			               
			                //chargement de la texture
			                g2.setPaint(tmp.getMaterial().getTexture());
			                
			                switch(tmp.getShape()){
				                case RAMP : // meme tracet que pour triangle
				                case TRIANGLE :
				                	Polygon p = new Polygon();
				                	List<Vec2> list = tmp.getVertices();
				                	for(int id = 0; id < 3; id++)
				                		p.addPoint((int)(list.get(id).x),(int)(list.get(id).y+0.2*tmp.getWidth()));
				                	g2.fillPolygon(p);
				                	break;
				                case CIRCLE :
				                	g2.fillOval((int)(-tmp.getWidth()/2), (int)(-tmp.getHeight()/2),(int)(tmp.getWidth()), (int)(tmp.getHeight()));
				                	break;
				                case BOX :
				                	g2.fillRect((int)(-tmp.getWidth()/2), (int)(-tmp.getHeight()/2),(int)(tmp.getWidth()), (int)(tmp.getHeight()));
				                	break;
				                case TARGET :
				                	Polygon pTar = new Polygon();
				                	List<Vec2> listTar = tmp.getVertices();
				                	for(int id = 0; id < listTar.size(); id++)
				                		pTar.addPoint((int)(listTar.get(id).x),(int)(listTar.get(id).y+0.2*tmp.getWidth()));
				                	g2.fillPolygon(pTar);
				                	break;
				                default :
				                	break;
			                }
	            	}
	            }
				}
				catch(Exception e){}
			} 
		}
	  } 
	}
	
	private GWorld m_world;
	private GameWindow gameWindow;
	private Body ground;
	public Launcher catapult; 
	public GameGraphic gg = new GameGraphic();
	private boolean alive;
	private boolean notMunition = false;
	private ParserXML parser;
	private TexturePaint textSky;
	int lvl;
	int score;
	
	boolean runEngaged;
	private int step_count;
	private long step_time;
	
	private long timer = 0;
	public void resetTimer(){timer=0;}
	
	/**
	 * Constructeur de GameWorld permet d'instancier le jeu
	 * @param gameWindow pointeur vers la fenetre d'affichage
	 * @param lvlForcage niveau par lequel on souhaite faire commencer le jeu
	 * @param forcage booleen permettant de forcer le jeu a commencer par le niveau passer par le parametre lvlForcage
	 */
	public GameWorld(GameWindow gameWindow, int lvlForcage, boolean forcage){
		this.gameWindow = gameWindow;
		this.gameWindow.setFocusable(true);
		this.gameWindow.requestFocus();
		alive = true;
		catapult = new Launcher();
		
		//********************************* creation du monde *********************************
		createWorld();
		defineWalls();

        parser = new ParserXML();
        //********************************* Variables de jeu *******************************
        if(forcage)
        	lvl = lvlForcage;
        else
        	lvl = parser.parseSave();
        score = 0;
		//********************************* Creation des objets *******************************
        parser.parseXML(this,"levels/Niveau"+lvl+".xml",false);
        parser.parseAllAndCreatorLevel();
        
		//********************************* parametrage pour le framerate *********************
		step_count = 0;
		step_time = System.currentTimeMillis();
	}
	/**
	 * Fonction permettant de creer le monde physique avec JBox2D
	 */
	private void createWorld(){
		// creation du monde physique
		AABB m_worldAABB = new AABB();
		m_worldAABB.lowerBound = new Vec2(-8000.0f, -8000.0f);
		m_worldAABB.upperBound = new Vec2(8000.0f, 8000.0f);
		Vec2 gravity = new Vec2(0.0f, 10.0f);
		boolean doSleep = true;
		setWorld(new GWorld(m_worldAABB, gravity, doSleep));
		m_world.setWarmStarting(true);
		
		//chargement texture fond
		Image img=null;
		try {
        	img=ImageIO.read(new File("textures/ciel.jpg"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        this.textSky = new TexturePaint((BufferedImage) img, new Rectangle(0, 0,1024, 600));

	}
	/**
	 * Fonction definissant les limites du terrain en placant des murs invisibles
	 */
	private void defineWalls(){
		// Ground
		PolygonDef sd = new PolygonDef();
		sd.setAsBox(4096.0f, 40.0f);
		sd.friction = 2.0f;
		BodyDef bd = new BodyDef();
		bd.position.set(0.0f, 560.0f);
		ground = m_world.createBody(bd);
		ground.createShape(sd);
		
        // Left
        bd.position.set(-10, 600);
        sd.friction = 1.0f;
        sd.setAsBox(1, 4096);
        m_world.createBody(bd).createShape(sd);

        // Right
        bd.position.set(3000, 600);
        sd.friction = 1.0f;
        sd.setAsBox(1, 4096);
        m_world.createBody(bd).createShape(sd);

        // Top
        bd.position.set(0, -2000);
        sd.setAsBox(8000, 1);
        m_world.createBody(bd).createShape(sd);
        
	}
	/**
	 * Fonction permettant d'ajouter des elements physiques au jeu
	 * @param block objet derivant de la classe Block contenant les parametres physiques a ajouter
	 */
	public void addBlock(Block block){
		BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((int)(block.getPosition().x), (int)(block.getPosition().y));
        bodyDef.angle=block.getAngle();
        Body physicalBody = m_world.createBody(bodyDef);
        physicalBody.createShape(block.getShapeDef());
        physicalBody.setMassFromShapes();
        physicalBody.setBullet(false);
        physicalBody.setUserData(block);
        m_world.physicalBodies.add(physicalBody);
	}
	/**
	 * Fonction permettant d'ajouter un projectile au chargeur
	 * @param p projectile a ajouter
	 */
	public void addMunition(Projectile p){
		catapult.projectiles.add(p);
		if(catapult.getActualMunition() == catapult.projectiles.size() - 1) {
			setProjectileToActive(p);
		}
	}
	/**
	 * Function permettant de rendre un projectile afin de pouvoir le lancer ou declencher des actions speciales
	 * @param p projectile a rendtre actif
	 */
	public void setProjectileToActive(Projectile p) {
		p.active = true;
		BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((int)(p.getPosition().x), (int)(p.getPosition().y));
        bodyDef.angle=p.getAngle();
        Body physicalBody = m_world.createBody(bodyDef);
        physicalBody.createShape(p.getShapeDef());
        physicalBody.setMassFromShapes();
        physicalBody.setBullet(false);
        physicalBody.setUserData(p);
        physicalBody.putToSleep();
        m_world.munitions.add(physicalBody);
	}

	/**
	 * Fonction du thread permettant de faire fonctionner le jeu
	 */
	@Override
	public void run() {
		while(alive){
			compute();
			detectStable();
		}
	}

	/**
	 * Fonction permettant de supprimer les blocks a detruire, placer les nouveaux projectiles, calculer la physique et lancer l'actualisation de l'affichage
	 */
	private void compute(){
		runEngaged = true;
		long tmpTime = System.currentTimeMillis();
		if(tmpTime-step_time > 4){
			step_count++;
			step_time = System.currentTimeMillis();
			
			//On v�rifie qu'il n'y a pas des objets � supprimer
			for(int i = 0; i < m_world.physicalBodies.size(); i++) {
				Block block = (Block) m_world.physicalBodies.get(i).getUserData();
				if(block != null && block.getToDelete() == true) {
					score += block.getScore();
					gameWindow.getScoreLabel().setText(Integer.toString(score)+" pts");
					System.out.println(score);
					m_world.destroyBody(m_world.physicalBodies.get(i));
					m_world.physicalBodies.remove(m_world.physicalBodies.get(i));
					if(block instanceof Target)
						((Target)block).finalize();
					System.out.println("nb de body : "+ m_world.getBodyCount());
				}
			}
			
			//On place le prochain projectile
			if(catapult.getActualMunition() >= m_world.munitions.size() && catapult.projectiles.size() > 0) {
				int munitionPrec = catapult.getActualMunition() - 1;
				if( munitionPrec >= 0 && munitionPrec < catapult.projectiles.size()) {
					Body prec = getActualMunitionBody(munitionPrec);
					if(prec != null && (prec.getPosition().x > 200 || prec.isSleeping()) ) {
						if (catapult.getActualMunition() < catapult.projectiles.size()) {
							setProjectileToActive(catapult.projectiles.get(catapult.getActualMunition()));
							Body nextMunition = getActualMunitionBody(catapult.getActualMunition());
							if(nextMunition != null) {
								nextMunition.setXForm(new Vec2(120, 450), 0);
							}
						}
						else
							notMunition = true;
					}
				}
			}
			
			m_world.step(1.0f/30.0f, 10);
			runEngaged = false;
		}
		gg.repaint();
		runEngaged = false;
	}
	/**
	 * Fonction gerant les conditions de victoire
	 */
	private void detectStable(){
		if(alive)
		{
			int nb = m_world.physicalBodies.size() + m_world.munitions.size();
			int i = 0;
			int k = 0;
	        for (int j = 0; j<m_world.physicalBodies.size();j++) {
	        	Body body = m_world.physicalBodies.get(j);
	        	if(body != null){
	        		if(body.isSleeping())
	        			i++;
	        	}
	        }
	       // for (Body body : m_world.munitions) {
	        for(int j = 0; j < m_world.munitions.size(); j++) {
	        	Body body = m_world.munitions.get(j);
	        	if(body != null){
	        		if(body.isSleeping())
	        			i++;
	        			k++;
	        	}
	        }
	        if(Target.getNbTarget() == 0 && nb == i){
	        	System.out.println("SOUCI 1 ?");
	        	gameWindow.gameNext();
	        	notMunition = false;
	        }
	        else if(notMunition == true && nb == i){
	        	System.out.println("SOUCI 2 ?");
    			gameWindow.gameFailed();
    			notMunition = false;
	        }
	        
	        if(Target.getNbTarget() == 0 ){
	        	if(timer == 0) timer = System.currentTimeMillis();
	        	if(System.currentTimeMillis()-timer > 3000 && Target.getNbTarget() == 0)
	        	{
	        		System.out.println("SOUCI 3 ?");
		        	timer = 0;
		        	gameWindow.gameNext();
		        	notMunition = false;
	        	}
	        }
		}
	}

	/**
	 * Setteur permettant de changer l'attribut m_world
	 * @param m_world
	 */
	public void setWorld(GWorld m_world) {
		this.m_world = m_world;
	}

	/**
	 * Accesseur a l'attribut m_world
	 * @return m_world
	 */
	public GWorld getWorld() {
		return m_world;
	}
	
	/**
	 * Fonction retournant un projectile en fonction de son index
	 * @param value index de l'objet a retourner
	 * @return le projectile ou null si il n'est pas trouve
	 */
	public Body getActualMunitionBody(int value) {
		if(value < m_world.munitions.size())
			return m_world.munitions.get(value);
		else
			return null;
	}
	
	/**
	 * Fonction permettant d'incrementer le nombre de munitions disponibles
	 * @return true si l'incrementation c'est correctement deroule, sinon false
	 */
	public boolean incrementsActualMunition() {
		if(catapult.getActualMunition() < m_world.munitions.size()) {
			catapult.incrementsActualMunition();
			return true;
			
		}
		else
		{
			return false;
		}
	}

	/**
	 * Fonction permettant de nettoyer le jeu et de changer le niveau passer en parametre
	 * @param lvl numero du niveau a charger
	 */
	void loadWorldReset(int lvl){
		this.gameWindow.setFocusable(true);
		this.gameWindow.requestFocus();
		for(int i = 0; i < m_world.physicalBodies.size(); i++) {
			m_world.destroyBody(m_world.physicalBodies.get(i));
			m_world.physicalBodies.remove(m_world.physicalBodies.get(i));
		}
		m_world.physicalBodies.clear();
		m_world.physicalBodies = new ArrayList<Body>();
		
		for (int i = 0; i< m_world.munitions.size(); i++) {
        	m_world.munitions.remove(i);
        }
		m_world.munitions.clear();
		while(m_world.getBodyCount() != 0)/*for(int i = 0; i<m_world.getBodyCount(); i++)*/{
			m_world.destroyBody(m_world.getBodyList());
		}
		for(int i = 0; i<catapult.projectiles.size(); i++){
			catapult.projectiles.remove(i);
		}
		catapult.projectiles.clear();
		catapult.resetActualMunition();
		
		Target.resetNbTarget();
		
		defineWalls();
		
		parser.parseSave();
		if(parser.getScore(lvl-1) < score)
			parser.setScore(lvl-1, score);
        parser.save();
		score = 0;
        
		parser.parseXML(this,"levels/Niveau"+lvl+".xml",false);
        parser.parseAllAndCreatorLevel();
        
        parser.parseSave();
        parser.unlockWorld(lvl);
        parser.save();

        alive = true;
	}
}
