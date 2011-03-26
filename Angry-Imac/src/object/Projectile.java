package object;

import org.jbox2d.common.Vec2;

public class Projectile extends Block{

	/**
	 * Projectile's constructor
	 * 
	 */
	Projectile(Shape s, int width, int height, Vec2 pos, float angle, Mat m) {
		super(s, width, height, pos, angle, m);
	}
}
