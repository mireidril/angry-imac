package parser;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import object.*;
import game.*;


public class ParserXML
{
   private org.jdom.Document document;
   private Element racine;
   private GameWorld gameworld;
   private boolean display;
   
   public ParserXML(GameWorld gameworld, String path, boolean display){
	   this.gameworld = gameworld;
	   this.display = display;
	   SAXBuilder sxb = new SAXBuilder();
	   try{
		   document = sxb.build(new File(path));
		}
		catch(Exception e){}

		racine = document.getRootElement();
   }

   public void parseAllAndCreatorLevel()
   {
      //On cree une Liste contenant tous les noeuds "block" de l'Element racine
      List<?> listBlocks = racine.getChildren("block");

      //On cree un Iterator sur notre liste de blocks
      Iterator<?> iBlocks = listBlocks.iterator();
      while(iBlocks.hasNext())
      {
         //On recree l'Element courant a chaque tour de boucle afin de
         //pouvoir utiliser les methodes propres aux Element comme :
         //selectionner un noeud fils, modifier du texte, etc...
         Element current = (Element)iBlocks.next();
         
         //On affiche le nom de l'element courant
         if(display){
	         System.out.println("New block : \t"
	        		 				+current.getAttributeValue("shape")+"/"
	        		 				+Float.parseFloat(current.getAttributeValue("width"))+"/"
	        		 				+Float.parseFloat(current.getAttributeValue("height"))+"/"
	        		 				+Float.parseFloat(current.getAttributeValue("posX"))+"/"
	        		 				+Float.parseFloat(current.getAttributeValue("posY"))+"/"
	        		 				+Float.parseFloat(current.getAttributeValue("angle"))+"/"
	        		 				+current.getAttributeValue("type"));
	         System.out.println("_____________________");
         }
         
         Shape shape = Shape.BOX;
         if(current.getAttributeValue("shape").equals("circle")){
        	 shape = Shape.CIRCLE;
         }
         else if(current.getAttributeValue("shape").equals("triangle")){
        	 shape = Shape.TRIANGLE;
         }
         else if(current.getAttributeValue("shape").equals("ramp")){
        	 shape = Shape.RAMP;
         }
         
         float width = Float.parseFloat(current.getAttributeValue("width"));
         float height = Float.parseFloat(current.getAttributeValue("height"));
         float x = Float.parseFloat(current.getAttributeValue("posX"));
         float y = Float.parseFloat(current.getAttributeValue("posY"));
         float angle = Float.parseFloat(current.getAttributeValue("angle"));
         Mat material = Mat.ICE;
         if(current.getAttributeValue("type").equals("metal")){
        	 material = Mat.METAL;
         }
         else if(current.getAttributeValue("type").equals("rock")){
        	 material = Mat.ROCK;
         }
         else if(current.getAttributeValue("type").equals("wood")){
        	 material = Mat.WOOD;
         }
         
         gameworld.addBlock(new Block(shape, width, height, new Vec2(x, y), angle, material));
         
      }
      /*
      List<?> listWeapons = racine.getChildren("weapon");
      Iterator<?> iWeapons = listWeapons.iterator();
      while(iWeapons.hasNext())
      {
         Element current = (Element)iWeapons.next();
         System.out.println("Type de weapon : "+current.getAttributeValue("type"));
         //current.getAttributeValue("type") retourne l'attribut"type" de l'element courant
         
         if(current.getChild("width")!=null)
        	 System.out.println("--Largeur weapon: "+current.getChild("width").getText());
         	 //current.getChild("width").getText() retourne le nom de la balise width qui est un fils de l'element courant
         if(current.getChild("height")!=null)
        	 System.out.println("--Hauteur weapon: "+current.getChild("height").getText());
         if(current.getChild("posX")!=null && current.getChild("posY")!=null)
        	 System.out.println("--PosX: "+current.getChild("posX").getText()+" PosY: "+current.getChild("posY").getText());
         if(current.getChild("angle")!=null)
        	 System.out.println("--Angle weapon: "+current.getChild("angle").getText());
         if(current.getChild("color")!=null)
        	 System.out.println("--Color weapon: "+current.getChild("color").getText());
         System.out.println("_____________________");
      }
      */
   }
   
  /*
   * Fonction searchFilter (bonus) pour faire des recherches dans le xml
   */ 
   /*
	@SuppressWarnings("serial")
	static void searchFilter()
   {
	   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      //On cree un nouveau filtre
      Filter filter = new Filter()
      {
		//On defini les proprietes du filtre a l'aide de la methode matches
         public boolean matches(Object ob)
         {
            //1 ere verification : on verifie que les objets qui seront filtres sont bien des Elements
            if(!(ob instanceof Element)){return false;}

            //On cree alors un Element sur lequel on va faire les verifications suivantes.
            Element element = (Element)ob;

            //test par couleur
            int testColor = 0;
            Element color = element.getChild("color");
            if(color == null){return false;}          
            if(color.getText().equals("red"))
        	   testColor = 1;

            //Si nos conditions sont remplies on retourne true, false sinon
            if(testColor == 1)
               return true;
            return false;
         }
      };

      //On fait une liste des resultats correspondant a nos criteres
      List<?> resultat = racine.getContent(filter);
      
      //Affichage du resultat
      Iterator<?> i = resultat.iterator();
      while(i.hasNext())
      {
         Element current = (Element)i.next();
         System.out.println("______FiltreColor____");
         System.out.println(current.getAttributeValue("type"));
         System.out.println("_____________________");
      }
      //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      //On crée un nouveau filtre pour trouver tous les blocks "wood"
      Filter filterTypeBlock = new Filter()
      {
         public boolean matches(Object ob)
         {
            if(!(ob instanceof Element))
            	return false;

            Element element = (Element)ob;
            int testType = 0;
            
            if(element.getAttributeValue("type").equals("wood"))
            	testType = 1;

            if(testType == 1)
               return true;
            
            return false;
         }
      };
      List<?> resultatTypeBlock = racine.getContent(filterTypeBlock);
      
      Iterator<?> iTypeBlock = resultatTypeBlock.iterator();
      while(iTypeBlock.hasNext())
      {
         Element current = (Element)iTypeBlock.next();
         System.out.println("++++++++FiltreTypeBlock++++++++");
         System.out.println(current.getAttributeValue("type"));
         System.out.println("++"+current.getChildText("color"));
         System.out.println("+++++++++++++++++++++++++++++++");
      }
      //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   }*/
}