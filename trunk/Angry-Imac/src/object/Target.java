package object;

import org.jbox2d.common.Vec2;

/**
 * Classe de la cible
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Celine, KIELB Adrien et ROLDAO Timothee
 * @version 1.0
 */
public class Target extends Block{
	
	private static int nbTarget = 0;
	private static Mat enumMat = Mat.TARGET;
	
	/**
	* 	Constructeur d'une cible 
	*  
		@param  s	La forme qu'aura le bloc.
		@param  width	La largeur du bloc.
		@param  height	La hauteur du bloc.
		@param  pos	Position du bloc (Vec2).
		@param  angle	Angle d'inclinaison du bloc.                  
	*/
	public Target(Shape s, float width, float height, Vec2 pos, float angle) {
		super(s, width, height, pos, angle, enumMat);
		nbTarget++;
	}
	
	/**
	* 	Destructeur d'une cible                  
	*/
	@Override
	public void finalize(){
		nbTarget--;
	}
	
	/**
	* 	Accesseur (Get) du nombre de cibles                 
	*/
	public static int getNbTarget(){
		return nbTarget;
	}
	
	/**
	* 	Remise a zero du compteur de cible                 
	*/
	public static void resetNbTarget(){
		nbTarget=0;
	}
}