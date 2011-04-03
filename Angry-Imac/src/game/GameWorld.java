package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
//import java.lang.Thread;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;

import object.*;
import parser.ParserXML;


public class GameWorld implements Runnable{
	
	@SuppressWarnings("serial")
	private class GameGraphic extends JPanel{
		
        public void resetTrans(Graphics2D g2){
            AffineTransform at = new AffineTransform();
            at.setToIdentity();
            g2.setTransform(at);
        }
        
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			
			//********************************* affichage du ciel *********************************
			g2.setPaint(textSky);
			//g2.setColor(new Color(150, 150, 255));
			g2.fillRect(0, 0, 1024, 600);
			
			//********************************* affichage rectangle du sol *************************
			//chargemen de la texture
			g.setColor(new Color(255, 150, 0));
			g.fillRect(0, 520, 1024, 80);
			
			//********************************* affichage de la catapulte *************************
			g2.setPaint(textCatapult);
			//g2.setColor(new Color(150, 150, 255));
			g2.fillRect(100, 430, 43, 107);

			//********************************* affichage des objets mouvants **********************
			//Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (Body body : m_world.physicalBodies){
            	if(body != null){
	                resetTrans(g2);
	                Block tmp = (Block) body.getUserData(); // recuperation du block lie a l'objet pour avoir ses caracteristiques
		                g2.setColor(tmp.getMaterial().getColor());
		                g2.translate(body.getWorldCenter().x, body.getWorldCenter().y);
		                g2.rotate(body.getAngle());
		               
		               
		                //chargemen de la texture
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
			                default :
			                	break;
		                }
            	}
            }
            
            //********************************* affichage de rope **********************
			for (int i = 0; i < rope.size(); i++){
            	Body link = rope.get(i);
            	if(link != null){
            		 resetTrans(g2);
            		 g2.setColor(new Color(255, 0, 0));
            		 g2.fillRect((int)link.getPosition().x, (int) link.getPosition().x, 10, 30);
            	}
            }            
         
	  } 
	}
	
	private GWorld m_world;
	private Body ground;
	public Launcher catapult; 
	//public  ArrayList<Body> physicalBodies = new ArrayList<Body>();
	public  ArrayList<Body> rope = new ArrayList<Body>();
	public GameGraphic gg = new GameGraphic();
	private boolean alive;
	private ParserXML parser;
	private TexturePaint textSky;
	private TexturePaint textCatapult;
	private TexturePaint textGround;
	
	private int step_count;
	private long step_time;
	
	
	public GameWorld(){
		alive = true;
		catapult = new Launcher();
		
		//********************************* creation du monde *********************************
		createWorld();
		defineWalls();

		//********************************* Creation des objets *******************************
        parser = new ParserXML(this,"map1.xml",false);
        parser.parseAllAndCreatorLevel();
		
		//********************************* parametrage pour le framerate *********************
		step_count = 0;
		step_time = System.currentTimeMillis();
	}
	
	private void createWorld(){
		// creation du monde physique
		AABB m_worldAABB = new AABB();
		m_worldAABB.lowerBound = new Vec2(0.0f, 0.0f);
		m_worldAABB.upperBound = new Vec2(1000.0f, 1000.0f);
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
		
        //texture herbe
        /*img=null;
        try {
        	img=ImageIO.read(new File("textures/ciel.jpg"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        this.textGround = new TexturePaint((BufferedImage) img, new Rectangle((int)(-img.getWidth(null)/2), (int)(-img.getHeight(null)/2),(int)(img.getWidth(null)), (int)(img.getHeight(null))));
		*/
        
        //chargement texture catapulte
		img = null;
		try {
        	img=ImageIO.read(new File("textures/catapult.png"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        this.textCatapult = new TexturePaint((BufferedImage) img, new Rectangle(10, 0, 43, 107));
		
        
		CollisionsListener listener = new CollisionsListener();
		m_world.setContactListener(listener);
	}
	
	private void defineWalls(){
		// Ground
		PolygonDef sd = new PolygonDef();
		sd.setAsBox(1024.0f, 40.0f);
		BodyDef bd = new BodyDef();
		bd.position.set(0.0f, 560.0f);
		ground = m_world.createBody(bd);
		ground.createShape(sd);
		
        // Left
        bd.position.set(1, 600);
        sd.friction = 1.0f;
        sd.setAsBox(1, 600);
        m_world.createBody(bd).createShape(sd);

        // Right
        bd.position.set(1023, 600);
        sd.friction = 1.0f;
        sd.setAsBox(1, 600);
        m_world.createBody(bd).createShape(sd);

        // Top
        bd.position.set(0, 0);
        sd.setAsBox(1024, 1);
        m_world.createBody(bd).createShape(sd);
        
        /* TEST CORDE/ELASTIQUE CELINE - 
        // Support Rope
        bd.position.x=8.5f;
        bd.position.y=0;
		PolygonDef bdef = new PolygonDef();
		bdef.setAsBox(2, 0.5f);
		bdef.density=0;
		bdef.friction=0.5f;
		bdef.restitution=0.2f;
		Body body=m_world.createBody(bd);
        body.createShape(bdef);
        Body link = body;
               
        // Rope
		for (int i = 1; i <= 10; i++) {
			// rope segment
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.x= 8.5f;
			bodyDef.position.y= i*30;
			PolygonDef boxDef = new PolygonDef();
			boxDef.setAsBox(10, 30);
			boxDef.density=100;
			boxDef.friction=0.5f;
			boxDef.restitution=0.2f;
			body=m_world.createBody(bodyDef);
			body.createShape(boxDef);
			// joint
			RevoluteJointDef revolute_joint = new RevoluteJointDef();
			revolute_joint.initialize(link, body, link.getPosition());
			m_world.createJoint(revolute_joint);
			body.setMassFromShapes();
			// saving the reference of the last placed link
			link=body;
			rope.add(body);
		}

        */
	}
	
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

	@Override
	public void run() {
		while(alive){
			long tmpTime = System.currentTimeMillis();
			if(tmpTime-step_time > 6){
				step_count++;
				step_time = System.currentTimeMillis();
				
				m_world.step(1.0f/30.0f, 10);
				gg.repaint();
				
				detectStable();
			
			}
		}
	}
	
	private void detectStable(){
		int nb = m_world.physicalBodies.size();
		int i = 0;
        for (Body body : m_world.physicalBodies) {
        	if(body != null){
        		if(body.isSleeping())
        			i++;
        	}
        }
        if(nb == i){
        	System.out.println("Stable hehe");
        	alive = false;
        }
	}

	public void setWorld(GWorld m_world) {
		this.m_world = m_world;
	}

	public GWorld getWorld() {
		return m_world;
	}
	
}
