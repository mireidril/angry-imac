package object;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Classe du Lance-Pierre
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Celine, KIELB Adrien et ROLDAO Timothee
 * @version 1.0
 */
public class Launcher {
	private final float elasticTension = 35;
	private boolean engaged = false;
	public TexturePaint textCatapult;
	
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	private int actualMunition = 0;
	
	/**
	 * Constructeur du Lance-Pierre
	 */
	public Launcher() {
		 //chargement texture catapulte
		Image img = null;
		try {
        	img=ImageIO.read(new File("textures/catapult.png"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        this.textCatapult = new TexturePaint((BufferedImage) img, new Rectangle(10, 0, 43, 107));
	}
	
	/**
	 * Accesseur (Get)
	 * @return indice de la munition actuelle
	 */
	public int getActualMunition() {
		return actualMunition;
	}
	
	/**
	 * Incrémente l'indice de la munition actuelle
	 */
	public void incrementsActualMunition() {
		actualMunition++;
	}
	
	/**
	 * Accesseur (Get)
	 * @return tension de l'élastique du lance-pierre
	 */
	public float getElasticTension() {
		return elasticTension;
	}
	
	/**
	 * Met l'indice de la munition actuelle à 0
	 */
	public void resetActualMunition(){
		actualMunition =0;
	}
	
	/**
	 * Accesseur (Set)
	 * @param value, pour indiquer si le lance-pierre est en cours d'utilisation ou non
	 */
	public void setEngaged(boolean value) {
		engaged = value;
	}
	
	/**
	 * Accesseur (Get)
	 * @return engaged, si le lance-pierre est en cours d'utilisation ou non
	 */
	public boolean getEngaged() {
		return engaged;
	}
}
