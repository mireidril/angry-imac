package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
//import java.lang.Thread;

import javax.swing.JPanel;

import org.jbox2d.dynamics.*;
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
			
			//********************************* affichage du ciel *********************************
			g.setColor(new Color(150, 150, 255));
			g.fillRect(0, 0, 1024, 600);
			
			//********************************* affichage rectangle du sol *************************
			g.setColor(new Color(255, 150, 0));
			g.fillRect(0, 520, 1024, 80);

			//********************************* affichage des objets mouvants **********************
			Graphics2D g2 = (Graphics2D) g;
            for (Body body : physicalBodies){
            	if(body != null){
	                resetTrans(g2);
	                Block tmp = (Block) body.getUserData(); // recuperation du block lie a l'objet pour avoir ses caracteristiques
	                	System.out.println(); 
		                g2.setColor(tmp.getMaterial().getColor());
		                g2.translate(body.getWorldCenter().x, body.getWorldCenter().y);
		                g2.rotate(body.getAngle());
		                
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
	  } 
	}
	
	private World m_world;
	private Body ground;
	public  ArrayList<Body> physicalBodies = new ArrayList<Body>();
	public GameGraphic gg = new GameGraphic();
	private boolean alive;
	private ParserXML parser;
	
	private int step_count;
	private long step_time;
	
	
	public GameWorld(){
		alive = true;
		
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
		m_world = new World(m_worldAABB, gravity, doSleep);
		m_world.setWarmStarting(true);
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
        m_world.createBody(bd).createShape(sd);

        // Top
        bd.position.set(0, 0);
        sd.setAsBox(1024, 1);
        m_world.createBody(bd).createShape(sd);
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
        physicalBodies.add(physicalBody);
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
		int nb = physicalBodies.size();
		int i = 0;
        for (Body body : physicalBodies) {
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
	
}
