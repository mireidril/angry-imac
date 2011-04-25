package object.materials;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Rock extends Material{
	public Rock(){
		density = 2.4f;
		friction = 0.5f;
		restitution = 0.01f;
		weightFactor = 0;
		breakable = false;
		breakableForce = 300.0f;
		color = new Color(82, 82, 82);
		
		Image img=null;
		//BufferedImage img= new BufferedImage();
        try {
        	img=ImageIO.read(new File("textures/pierre.jpg"));
        }
        catch(IOException e){
        	System.out.println("ok");System.exit(0);
        }
        
        ///g2.drawImage(img,0, 0, gg);
        this.texture = new TexturePaint((BufferedImage) img, new Rectangle((int)(-img.getWidth(null)/2), (int)(-img.getHeight(null)/2),(int)(img.getWidth(null)), (int)(img.getHeight(null))));
	}
}