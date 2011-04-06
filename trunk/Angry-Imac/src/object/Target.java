package object;

import org.jbox2d.common.Vec2;

public class Target extends Block{
	
	private static int nbPoint = 0;
	private static int nbLaunch = 0;
	private static Mat enumMat = Mat.TARGET;
	
	public Target(Shape s, float width, float height, Vec2 pos, float angle) {
		super(s, width, height, pos, angle, enumMat);
	}
}