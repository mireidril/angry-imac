package object;

import org.jbox2d.common.Vec2;
/**
 * Classe d'un Projectile 
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Céline, KIELB Adrien et ROLDAO Timothée
 * @version 1.0
 */
public class Projectile extends Block{

	public boolean active;
	public boolean throwed;
	/**
	 * Constructeur du projectile
	 * @param s			Shape du projectile
	 * @param width		Largeur du projectile
	 * @param height	Hauteur du projectile
	 * @param pos		Position du projectile
	 * @param angle		Angle d'inclinaison du projectile
	 * @param m			Matière du projectile
	 */
	public Projectile(Shape s, int width, int height, Vec2 pos, float angle, Mat m) {
		super(s, width, height, pos, angle, m);
		active = false;
		throwed = false;
	}
}
