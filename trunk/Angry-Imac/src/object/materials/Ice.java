package object.materials;

import java.awt.Color;

public class Ice  extends Material{
	public Ice(){
		density = 0.9f;
		friction = 0.03f;
		restitution = 0.01f;
		weightFactor = 0;
		breakable = true;
		breakableForce = 50.0f;
		color = new Color(126, 186, 190);
	}
}
