package object.materials;

import java.awt.Color;

public class Rock extends Material{
	public Rock(){
		density = 2.4f;
		friction = 0.5f;
		restitution = 0.01f;
		weightFactor = 0;
		breakable = false;
		breakableForce = 0.5f;
		color = new Color(82, 82, 82);
	}
}
