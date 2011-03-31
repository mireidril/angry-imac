package object.materials;

import java.awt.Color;
import java.awt.TexturePaint;

public abstract class Material {
	protected float density;
	protected float friction;
	protected float restitution;
	protected float weightFactor;
	protected boolean breakable;
	protected float breakableForce;
	protected Color color;
	protected TexturePaint texture;


	public boolean isBreakable() {
		return breakable;
	}
	
	public float getDensity() {
		return density;
	}

	public float getFriction() {
		return friction;
	}

	public float getRestitution() {
		return restitution;
	}

	public float getWeightFactor() {
		return weightFactor;
	}
	
	public Color getColor(){
		return color;
	}
	
	public float getBreakableForce(){
		return breakableForce;
	}
	
	public TexturePaint getTexture(){
		return texture;
	}

}
