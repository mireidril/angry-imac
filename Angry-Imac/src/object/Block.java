package object;
import java.util.List;


import object.materials.*;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.ShapeDef;
import org.jbox2d.common.Vec2;

/**
 * Classe d'un block
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Celine, KIELB Adrien et ROLDAO Timothee
 * @version 1.3
 */
public class Block {

	private Shape sh;
	private Vec2 position;
	private float angle;
	private Material mat;
	private float height;
	private float width;
	private Mat enumMat;
	private List<Vec2> vertices;
	private ShapeDef shapeDef;
	private boolean toDelete;
	private int score;
	
	//Va contenir les textures de la destruction d'un block
	//private ArrayList<TexturePaint> animationDestruction;
	
/**
* 	Constructeur d'un Block. 
*  	Construit un bloc avec toutes ses proprietes physique.
*  
	@param  s	La forme qu'aura le bloc.
	@param  width	La largeur du bloc.
	@param  height	La hauteur du bloc.
	@param  pos	Position du bloc (Vec2).
	@param  angle	Angle d'inclinaison du bloc.
	@param  m	Le materiau du bloc (proprietes physique).                   
*/
	public Block(Shape s, float width, float height, Vec2 pos, float angle, Mat m) {
		sh = s;
		position = pos;
		this.angle = angle;
		enumMat = m;
		this.width = width;
		this.height = height;
		this.vertices = null;
		toDelete = false;
		//animationDestruction = new ArrayList<TexturePaint>();

		//********************************* verif des longueurs *********************************
		if(s == Shape.CIRCLE || s == Shape.TRIANGLE){
			if(height < width)
				height = width;
			else
				width = height;
		}

		//********************************* creation du materiau associe *************************
		switch (m) {
			case WOOD : 
				mat = new Wood();
				score = 20;
				break;
				
			case METAL : 
				mat = new Metal();
				score = 3000;
				break;
				
			case ICE : 
				mat = new Ice();
				score = 10;
				break;
				
			case ROCK : 
				mat = new Rock();
				score = 100;
				break;
				
			case MAMMOUTH : 
				mat = new Mammouth();
				break;
				
			case MAMMOUTH2 : 
				mat = new Mammouth2();
				break;
				
			case TARGET :
				mat = new object.materials.Target();
				score = 500;
				break;
			
			default :
				mat = new Wood();
				break;
		}
		
		//********************************* definition de la boite de collision *****************
		switch(s){
			case CIRCLE :
				CircleDef circleDef = new CircleDef();
	    		circleDef.radius = getWidth()/2.0f;
	    		shapeDef = (ShapeDef)circleDef;
				break;
			case BOX :
				PolygonDef shapeDefBox = new PolygonDef();
				shapeDefBox.setAsBox(getWidth()/2.0f, getHeight()/2.0f);
	    		shapeDef = (ShapeDef)shapeDefBox;
				break;
			case TRIANGLE :
				PolygonDef shapeDefTmp = new PolygonDef();
				shapeDefTmp.clearVertices();
				shapeDefTmp.addVertex(new Vec2(-getWidth()/2.0f,-getHeight()/2.0f));
				shapeDefTmp.addVertex(new Vec2(getWidth()/2.0f,-getHeight()/2.0f));
				shapeDefTmp.addVertex(new Vec2(0.0f,(float)(getHeight()*Math.sqrt(3.0)/2.0f-getHeight()/2.0f)));
	    		setVertices(shapeDefTmp.getVertexList());
	    		shapeDef = (ShapeDef)shapeDefTmp;
				break;
			case RAMP :
				System.out.println("RAMPE : PAS ENCORE FAIT");
	        	System.exit(0);
	        	break;
			case TARGET :
				PolygonDef shapeTarTmp = new PolygonDef();
				shapeTarTmp.clearVertices();
				shapeTarTmp.addVertex(new Vec2(getWidth()/2.0f,0.0f));
				shapeTarTmp.addVertex(new Vec2(getWidth()/4.0f,getHeight()/2.0f));
				shapeTarTmp.addVertex(new Vec2(-getWidth()/4.0f,getHeight()/2.0f));
				shapeTarTmp.addVertex(new Vec2(-getWidth()/2.0f,0.0f));
				
	    		setVertices(shapeTarTmp.getVertexList());
	    		shapeDef = (ShapeDef)shapeTarTmp;
				break;
			default :
				break;
		}
		
		//********************************* parametres physiques de l'objet **********************
		shapeDef.density = getMaterial().getDensity();
		shapeDef.friction = getMaterial().getFriction();
		shapeDef.restitution = getMaterial().getRestitution();
	}
	
	//********************************* getter / setter *********************************
/**
 * 	accesseur (Get) de la Forme du Block                         
	@return sh La forme du block                
*/
	public Shape getShape(){
		return sh;
	}

/**
 * 	accesseur (Get) de la position du Block                          
	@return position La position du block                
*/
	public Vec2 getPosition(){
		return position;
	}
	
/**
 * 	accesseur (Get) de l'angle de rotation du Block                          
	@return angle L'angle du block                
*/
	public float getAngle(){
		return angle;
	}

/**
 * 	accesseur (Get) du materiau du Block                          
	@return mat Le materiau du block                
*/
	public Material getMaterial(){
		return mat;
	}
	
/**
 * 	accesseur (Get) de l'enum du materiau du Block                          
	@return enumMat L'enum du materiau du block                
*/
	public Mat getMaterialEnum(){
		return enumMat;
	}

/**
 * 	accesseur (Get) de la hauteur du Block                          
	@return height La hauteur du Block               
*/
	public float getHeight() {
		return height;
	}

/**
 * 	accesseur (Get) de la largeur du Block                          
	@return width La largeur du Block               
*/
	public float getWidth() {
		return width;
	}

/**
 * 	accesseur (Set) des vertices du Block                          
	@param vert Les vertices (Vec2) du Block               
*/	
	public void setVertices(List<Vec2> vert){
		vertices = vert;
	}

/**
 * 	accesseur (Get) des vertices du Block                          
	@return vertices Les vertices (Vec2) du Block               
*/	
	public List<Vec2> getVertices(){
		return vertices;
	}

/**
 * 	accesseur (Get) de la definition de la forme du Block                          
	@return shapeDef La definition de la forme du Block               
*/	
	public ShapeDef getShapeDef(){
		return shapeDef;
	}
	
	/**
	 * 	accesseur (Set) pour savoir si le bloc est a supprimer                          
		@param value booleen designant la suppression               
	*/	
	public void setToDelete(boolean value) {
		toDelete = value;
	}
	
	/**
	 * 	accesseur (Get) pour savoir si le bloc est a supprimer 
	 	@return toDelete booleen designant la suppression                             
	*/	
	public boolean getToDelete() {
		return toDelete;
	}
	
	/**
	 * 	accesseur (Get) pour connaitre le score  
	 	@return score Le score fait par la destruction du bloc                                  
	*/	
	public int getScore(){
		return score;
	}

}
