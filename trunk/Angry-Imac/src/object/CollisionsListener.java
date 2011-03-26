package object;

import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

public class CollisionsListener implements ContactListener {

	@Override
	public void add(ContactPoint arg0) {
		Block block1 = (Block) arg0.shape1.getBody().getUserData();
		Block block2 = (Block) arg0.shape2.getBody().getUserData();
		
		System.out.println("TOUCHER : "+arg0.velocity.normalize());
		
		/*if(block1.getMaterial().getBreakableForce() <= arg0.velocity.normalize())
			//destruction du block
			System.out.println("destruction objet 1");
		
		if(block2.getMaterial().getBreakableForce() <= arg0.velocity.normalize())
			//destruction du block
			System.out.println("destruction objet 2");*/
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
