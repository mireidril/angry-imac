package object;

import org.jbox2d.common.Vec2;

public class Projectile extends Block{

	public boolean active;
	/**
	 * Projectile's constructor
	 * 
	 */
	public Projectile(Shape s, int width, int height, Vec2 pos, float angle, Mat m) {
		super(s, width, height, pos, angle, m);
		active = false;
	}
}
