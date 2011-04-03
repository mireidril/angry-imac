package game;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class GWorld extends World{
	/**
	* 	Constructeur d'un World.
	*  
		@param  worldAABB 	Systeme de collision.
		@param  gravity		Gravite qui s'applique sur le monde.
		@param  doSleep		???.                  
	*/
	public GWorld(AABB worldAABB, Vec2 gravity, boolean doSleep) {
		super(worldAABB, gravity, doSleep);
	}

	public  ArrayList<Body> physicalBodies = new ArrayList<Body>();
}
