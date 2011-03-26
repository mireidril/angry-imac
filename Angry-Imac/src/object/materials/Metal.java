package object.materials;

import java.awt.Color;

public class Metal  extends Material{
	public Metal(){
		density = 7.32f;
		friction = 0.2f;
		restitution = 0.05f;
		weightFactor = 0;
		breakable = false;
		breakableForce = 0.5f;
		color = new Color(161, 161, 161);
	}
}
