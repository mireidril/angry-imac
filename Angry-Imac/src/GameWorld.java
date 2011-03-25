import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.lang.Thread;

import javax.swing.JPanel;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.common.*;

import object.*;
import object.Shape;


public class GameWorld implements Runnable{
	private class GameGraphic extends JPanel {
		
        public void resetTrans(Graphics2D g2) {
            AffineTransform at = new AffineTransform();
            at.setToIdentity();
            g2.setTransform(at);
        }
        
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			//affichage du ciel
			g.setColor(new Color(150, 150, 255));
			g.fillRect(0, 0, 1024, 600);
			
			//affichage rectangle du sol
			g.setColor(new Color(255, 150, 0));
			g.fillRect(0, 520, 1024, 80);

			Graphics2D g2 = (Graphics2D) g;
            for (Body body : physicalBodies) {
            	if(body != null)
            	{
	                resetTrans(g2);
	                Block tmp = (Block) body.getUserData();
	                
	                g2.setColor(tmp.getMaterial().getColor());
	                g2.translate(body.getWorldCenter().x, body.getWorldCenter().y);
	                g2.rotate(body.getAngle());
	                g2.fillRect((int)(-tmp.getWidth()/2), (int)(-tmp.getHeight()/2),(int)(tmp.getWidth()), (int)(tmp.getHeight()));
            	}
            }
			
	  } 
	}
	
	private World m_world;
	private Body ground;
	public  ArrayList<Body> physicalBodies = new ArrayList<Body>();
	public GameGraphic gg = new GameGraphic();
	private boolean alive;
	
	private int step_count;
	private long step_time;
	
	
	public GameWorld(){
		alive = true;
		//creation du monde
        AABB m_worldAABB = new AABB();
		m_worldAABB.lowerBound = new Vec2(0.0f, 0.0f);
		m_worldAABB.upperBound = new Vec2(1000.0f, 1000.0f);
		Vec2 gravity = new Vec2(0.0f, 10.0f);
		boolean doSleep = true;
		m_world = new World(m_worldAABB, gravity, doSleep);
		m_world.setWarmStarting(true);
		
        //definition du sol
		PolygonDef sd = new PolygonDef();
		sd.setAsBox(1024.0f, 40.0f);
		BodyDef bd = new BodyDef();
		bd.position.set(0.0f, 560.0f);
		ground = m_world.createBody(bd);
		ground.createShape(sd);
		
		//definition des murs
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

        addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(30.0f, 150.0f), 0.0f, Mat.METAL));
        addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(49.0f, 10.0f), 0.0f, Mat.WOOD));
        addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(110.0f, 10.0f), 0.0f, Mat.WOOD));
        addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(150.0f, 10.0f), (float)Math.PI/4, Mat.ICE));
        addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(180.0f, 10.0f), 0.0f, Mat.ICE));
		addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(250.0f, 10.0f), 0.0f, Mat.ROCK));
		addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(220.0f, 10.0f), 0.0f, Mat.METAL));
		addBlock(new Block(Shape.BOX, 20.0f, 50.0f, new Vec2(220.0f, 150.0f), 0.0f, Mat.WOOD));
		
		
		step_count = 0;
		step_time = System.currentTimeMillis();
	}
	
	public void addBlock(Block block){
		PolygonDef shapeDef = new PolygonDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((int)(block.getPosition().x), (int)(block.getPosition().y)); //worldposition
        bodyDef.angle=block.getAngle();
        shapeDef.density = block.getMaterial().getDensity(); //density defined -> dynamic object
        shapeDef.friction = block.getMaterial().getFriction();
        shapeDef.restitution = block.getMaterial().getRestitution(); //bouncy
        
        switch(block.getMaterialEnum()){
        	default :
        		shapeDef.setAsBox(block.getWidth()/2.0f, block.getHeight()/2.0f);
        		break;
        }
        Body physicalBody = m_world.createBody(bodyDef);

        physicalBody.createShape(shapeDef);
        physicalBody.setMassFromShapes();
        physicalBody.setBullet(false);
        physicalBody.setUserData(block);
        physicalBodies.add(physicalBody);
	}

	public void addBox(int x, int y) {
        PolygonDef shapeDef = new PolygonDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y); //worldposition
        bodyDef.angle=(float)(Math.PI*Math.random());
        shapeDef.density = 25.0f; //density defined -> dynamic object
        shapeDef.friction = 0.35f;
        shapeDef.restitution = 0.3f; //bouncy
        shapeDef.setAsBox(10, 10);
        Body physicalBody = m_world.createBody(bodyDef);

        physicalBody.createShape(shapeDef);
        physicalBody.setMassFromShapes();
        physicalBody.setBullet(false);
        physicalBodies.add(physicalBody);
    }

	@Override
	public void run() {
		while(alive){
			long tmpTime = System.currentTimeMillis();
			if(tmpTime-step_time > 6){
				//System.out.println("Step ! number : "+step_count);
				step_count++;
				step_time = System.currentTimeMillis();
				
				m_world.step(1.0f/30.0f, 10);
				gg.repaint();
				
				detectStable();
			}
			//Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		}
	}
	
	private void detectStable(){
		int nb = physicalBodies.size();
		int i = 0;
        for (Body body : physicalBodies) {
        	if(body != null)
        	{
        		if(body.isSleeping())
        			i++;
        	}
        }
        if(nb == i)
        {
        	System.out.println("Stable hehe");
        	alive = false;
        }
	}

}
