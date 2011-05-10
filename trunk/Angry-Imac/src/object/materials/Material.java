package object.materials;

import java.awt.Color;
import java.awt.TexturePaint;

/**
 * Classe des materiaux
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Céline, KIELB Adrien et ROLDAO Timothée
 * @version 1.0
 */
public abstract class Material {
	protected float density;
	protected float friction;
	protected float restitution;
	protected float weightFactor;
	protected boolean breakable;
	protected float breakableForce;
	protected Color color;
	protected TexturePaint texture;

	/**
	 * Accesseur (Get) permettant de savoir si le materiau est destructible
	 */
	public boolean isBreakable() {
		return breakable;
	}
	
	/**
	 * Accesseur (Get) permettant de connaitre la densite du materiau
	 */
	public float getDensity() {
		return density;
	}

	/**
	 * Accesseur (Get) permettant de connaitre la friction du materiau
	 */
	public float getFriction() {
		return friction;
	}

	/**
	 * Accesseur (Get) permettant de connaitre la Restitution du materiau
	 */
	public float getRestitution() {
		return restitution;
	}

	/**
	 * Accesseur (Get) permettant de connaitre le facteur de poids du materiau
	 */
	public float getWeightFactor() {
		return weightFactor;
	}
	
	/**
	 * Accesseur (Get) permettant de connaitre la couleur du materiau
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Accesseur (Get) permettant de connaitre la limite de cassure du materiau
	 */
	public float getBreakableForce(){
		return breakableForce;
	}
	
	/**
	 * Accesseur (Get) permettant de connaitre la texture du materiau
	 */
	public TexturePaint getTexture(){
		return texture;
	}

}
