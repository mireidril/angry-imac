package object;

import game.GWorld;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Launcher {
	private final float elasticTension = 35;
	private ArrayList<Body> rope = new ArrayList<Body>();
	private ArrayList<RevoluteJointDef> jointsRope = new ArrayList<RevoluteJointDef>();
	public TexturePaint textCatapult;
	
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	private int actualMunition = 0;
	
	public Launcher() {
		 //chargement texture catapulte
		Image img = null;
		try {
        	img=ImageIO.read(new File("textures/catapult.png"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        this.textCatapult = new TexturePaint((BufferedImage) img, new Rectangle(10, 0, 43, 107));
	}
	
	public void createElastic(GWorld gameWorld) {
		
		RevoluteJointDef revolute_joint = new RevoluteJointDef();
		DistanceJointDef distance_joint = new DistanceJointDef();
        
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x=600;
		bodyDef.position.y=300;
		PolygonDef boxDef = new PolygonDef();
		boxDef.setAsBox(20, 5);
		boxDef.density=0;
		boxDef.friction=0.5f;
		boxDef.restitution=0.2f;
		Body body=gameWorld.createBody(bodyDef);
		body.createShape(boxDef);
		Body link=body;
		rope.add(body);
		
		bodyDef = new BodyDef();
		bodyDef.position.x=800;
		bodyDef.position.y=300;
		boxDef = new PolygonDef();
		boxDef.setAsBox(20, 5);
		boxDef.density=0;
		boxDef.friction=0.5f;
		boxDef.restitution=0.2f;
		body=gameWorld.createBody(bodyDef);
		body.createShape(boxDef);
		rope.add(body);
		
		Body body2, body3;
		
		// rope
		//for (int i = 1; i <= 2; i++)
		{
			// rope segment
			bodyDef = new BodyDef();
			bodyDef.position.x=650;
			bodyDef.position.y=400;
			boxDef = new PolygonDef();
			boxDef.setAsBox(1, 5);
			boxDef.density=100;
			boxDef.friction=0.5f;
			boxDef.restitution=0.2f;
			body2=gameWorld.createBody(bodyDef);
			body2.createShape(boxDef);
			
			bodyDef = new BodyDef();
			bodyDef.position.x=750;
			bodyDef.position.y=400;
			boxDef = new PolygonDef();
			boxDef.setAsBox(1, 5);
			boxDef.density=100;
			boxDef.friction=0.5f;
			boxDef.restitution=0.2f;
			body3=gameWorld.createBody(bodyDef);
			body3.createShape(boxDef);
			
			// joint
			revolute_joint.initialize(link, body2, new Vec2(link.getPosition().x + 25, link.getPosition().y + 50));
			gameWorld.createJoint(revolute_joint);
			jointsRope.add(revolute_joint);
			body2.setMassFromShapes();
			revolute_joint.initialize(body, body3, new Vec2(body.getPosition().x - 25, body.getPosition().y + 50));
			gameWorld.createJoint(revolute_joint);
			jointsRope.add(revolute_joint);
			body3.setMassFromShapes();
			// saving the reference of the last placed link
			//link=body;
			rope.add(body2);
			rope.add(body3);
		}
		// final body
		bodyDef.position.x=700;
		bodyDef.position.y=500;
		boxDef = new PolygonDef();
		boxDef.setAsBox(5,5);
		boxDef.density=2;
		boxDef.friction=0.5f;
		boxDef.restitution=0.2f;
		body=gameWorld.createBody(bodyDef);
		body.createShape(boxDef);
		/*revolute_joint.initialize(link, body, new Vec2(85, 105));
		gameWorld.createJoint(revolute_joint);
		body.setMassFromShapes();*/
		
		revolute_joint.initialize(body3, body, new Vec2(body3.getPosition().x - 25, body3.getPosition().y + 50));
		gameWorld.createJoint(revolute_joint);
		jointsRope.add(revolute_joint);
		revolute_joint.initialize(body2, body, new Vec2(body2.getPosition().x + 25, body2.getPosition().y + 50));
		gameWorld.createJoint(revolute_joint);
		jointsRope.add(revolute_joint);
		body.setMassFromShapes();
		
		rope.add(body);
	}
	
	public int getActualMunition() {
		return actualMunition;
	}
	
	public void incrementsActualMunition() {
		actualMunition++;
	}
	
	public float getElasticTension() {
		return elasticTension;
	}
	
	public ArrayList<Body> getRope() {
		return rope;
	}
	
	public ArrayList<RevoluteJointDef> getRopeJoints() {
		return jointsRope;
	}
	
	public void resetActualMunition(){
		actualMunition =0;
	}
}
