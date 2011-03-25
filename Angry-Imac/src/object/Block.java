package object;
import object.materials.*;

import org.jbox2d.common.Vec2;

public class Block {

	private Shape sh;
	private Vec2 position;
	private float angle;
	private Material mat;
	private float height;
	private float width;
	private Mat enumMat;
	
/**
	 * 	Constructeur d'un Block. 
	 *  Construit un bloc avec toutes ses proprietes physique.
	 *  
	                          
	@param  s	La forme qu'aura le bloc.
	@param  width	La largeur du bloc.
	@param  height	La hauteur du bloc.
	@param  pos	Position du bloc (Vec2).
	@param  angle	Angle d'inclinaison du bloc.
	@param  m	Le materiau du bloc (proprietes physique).
	 *                   
*/
	public Block(Shape s, float width, float height, Vec2 pos, float angle, Mat m) {
		sh = s;
		position = pos;
		this.angle = angle;
		enumMat = m;
		this.width = width;
		this.height = height;
		
		switch (m) {
			case WOOD : 
				mat = new Wood();
				break;
				
			case METAL : 
				mat = new Metal();
				break;
				
			case ICE : 
				mat = new Ice();
				break;
				
			case ROCK : 
				mat = new Rock();
				break;
			
			default :
				mat = new Wood();
				break;
		}
	}
	
	public Shape getShape(){
		return sh;
	}
	
	public Vec2 getPosition(){
		return position;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public Material getMaterial(){
		return mat;
	}
	
	public Mat getMaterialEnum(){
		return enumMat;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

}
