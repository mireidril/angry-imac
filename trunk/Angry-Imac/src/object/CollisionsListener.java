package object;

import game.GWorld;

import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

public class CollisionsListener implements ContactListener {

	@Override
	public void add(ContactPoint arg0) {
		Block block1 = (Block) arg0.shape1.getBody().getUserData();
		Block block2 = (Block) arg0.shape2.getBody().getUserData();
		
		if(block1 != null){
			if(block1.getMaterial().getBreakableForce() <= arg0.velocity.length()){
				GWorld world = (GWorld) arg0.shape1.m_body.m_world;
				world.destroyBody(arg0.shape1.m_body);
				world.physicalBodies.remove(arg0.shape1.m_body);
				arg0.shape1.destructor();
				//System.out.println("destruction d'un block => "+arg0.velocity.length());
			}
		}
		
		if(block2 != null){
			if(block2.getMaterial().getBreakableForce() <= arg0.velocity.length()){
				GWorld world = (GWorld) arg0.shape2.m_body.m_world;
				world.destroyBody(arg0.shape2.m_body);
				world.physicalBodies.remove(arg0.shape2.m_body);
				arg0.shape2.destructor();
				//System.out.println("destruction d'un block => "+arg0.velocity.length());
			}
		}
	}

	@Override
	public void result(ContactResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void persist(ContactPoint arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void remove(ContactPoint arg0) {
		// TODO Auto-generated method stub
	}
	
}
