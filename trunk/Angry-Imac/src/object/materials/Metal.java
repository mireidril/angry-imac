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
 * Classe du materiau metal
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Céline, KIELB Adrien et ROLDAO Timothée
 * @version 1.0
 */
public class Metal  extends Material{
	/**
	 * Constructeur d'un materiau en metal
	 */
	public Metal(){
		density = 10.32f;
		friction = 0.1f;
		restitution = 0.05f;
		weightFactor = 0;
		breakable = false;
		breakableForce = 500.0f;
		color = new Color(161, 161, 161);
		
		Image img=null;
		//BufferedImage img= new BufferedImage();
        try {
        	img=ImageIO.read(new File("textures/materiaux/metal.jpg"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        
        ///g2.drawImage(img,0, 0, gg);
        this.texture = new TexturePaint((BufferedImage) img, new Rectangle((int)(-img.getWidth(null)/2), (int)(-img.getHeight(null)/2),(int)(img.getWidth(null)), (int)(img.getHeight(null))));
	}
}
