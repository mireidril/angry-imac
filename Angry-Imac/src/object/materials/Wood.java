package object.materials;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe du materiau bois
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Céline, KIELB Adrien et ROLDAO Timothée
 * @version 1.0
 */
public class Wood extends Material{
	/**
	 * Constructeur du materiau bois
	 */
	public Wood(){
		density = 0.4f;// * 20;
		friction = 0.7f;
		restitution = 0.3f;
		weightFactor = 0;
		breakable = true;
		breakableForce = 200.0f;
		color = new Color(165, 89, 4);
		
		Image img=null;
        try {
        	img=ImageIO.read(new File("textures/materiaux/bois.jpg"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        
        this.texture = new TexturePaint((BufferedImage) img, new Rectangle((int)(-img.getWidth(null)/2), (int)(-img.getHeight(null)/2),(int)(img.getWidth(null)), (int)(img.getHeight(null))));
	}
}
