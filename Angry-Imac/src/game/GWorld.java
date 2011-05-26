package game;

import java.util.ArrayList;

import object.Block;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

/**
 * Classe du monde
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Céline, KIELB Adrien et ROLDAO Timothée
 * @version 1.5
 */
public class GWorld extends World implements ContactListener {

	public  ArrayList<Body> physicalBodies = new ArrayList<Body>();
	public ArrayList<Body> munitions = new ArrayList<Body>();
	
	/**
	* 	Constructeur d'un World.
	*  
		@param  worldAABB 	Systeme de collision.
		@param  gravity		Gravite qui s'applique sur le monde.
		@param  doSleep		Detection de la stabilite.                  
	*/
	public GWorld(AABB worldAABB, Vec2 gravity, boolean doSleep) {
		super(worldAABB, gravity, doSleep);
		setContactListener(this);
	}
	
	/**
	* 	Ajout d'un listener de contact
	*  
		@param  arg0 	Point de contact entre deux blocs.                 
	*/
	@Override
	public void add(ContactPoint arg0) {
		Block block1 = (Block) arg0.shape1.getBody().getUserData();
		Block block2 = (Block) arg0.shape2.getBody().getUserData();
		
		if(block1 != null){
			if(block1.getMaterial().getBreakableForce() <= arg0.velocity.length()){
				//Impossible de supprimer des Body ici, il faut le faire avant le m_world.step()
				//Body p = arg0.shape1.getBody();
				//int pos = physicalBodies.indexOf(p);
				block1.setToDelete(true);
				//arg0.shape1.destructor(); // utile ?
				
				//System.out.println("destruction d'un block => "+arg0.velocity.length());
			}
		}
		
		if(block2 != null){
			if(block2.getMaterial().getBreakableForce() <= arg0.velocity.length()){
				//Impossible de supprimer des Body ici, il faut le faire avant le m_world.step()
				//Body p = arg0.shape2.getBody();
				//int pos = physicalBodies.indexOf(p);
				block2.setToDelete(true);			
				//arg0.shape2.destructor(); //utile ?
				
				//System.out.println("destruction d'un block => "+arg0.velocity.length());
			}
		}
	}
	
	/**
	* 	Contact persistant
	*  
		@param  arg0 	Point de contact entre deux blocs.                 
	*/
	@Override
	public void persist(ContactPoint arg0) {
		
	}
	
	/**
	* 	Separation des blocs
	*  
		@param  arg0 	Point de contact entre deux blocs.                 
	*/
	@Override
	public void remove(ContactPoint arg0) {
		
	}
	
	/**
	* 	Resultat du contact entre les deux blocs
	*  
		@param  arg0 	Point de contact entre deux blocs.                 
	*/
	@Override
	public void result(ContactResult arg0) {
		
	}
}
