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
 * Classe du materiau glace
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Céline, KIELB Adrien et ROLDAO Timothée
 * @version 1.0
 */

public class Ice  extends Material{
	/**
	* 	Constructeur d'un bloc de glace                
	*/
	public Ice(){
		density = 0.9f;
		friction = 0.03f;
		restitution = 0.01f;
		weightFactor = 0;
		breakable = true;
		breakableForce = 50.0f;
		color = new Color(126, 186, 190);
		
		Image img=null;
		//BufferedImage img= new BufferedImage();
        try {
        	img=ImageIO.read(new File("textures/materiaux/glace.jpg"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        
        ///g2.drawImage(img,0, 0, gg);
        this.texture = new TexturePaint((BufferedImage) img, new Rectangle((int)(-img.getWidth(null)/2), (int)(-img.getHeight(null)/2),(int)(img.getWidth(null)), (int)(img.getHeight(null))));
	}
}
