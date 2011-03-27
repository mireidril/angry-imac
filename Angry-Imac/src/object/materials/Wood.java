package object.materials;

import java.awt.Color;

public class Wood extends Material{
	public Wood(){
		density = 0.6f;
		friction = 0.3f;
		restitution = 0.3f;
		weightFactor = 0;
		breakable = true;
		breakableForce = 100.0f;
		color = new Color(165, 89, 4);
	}
}
