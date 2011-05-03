package parser;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import object.*;
import game.*;


public class ParserXML
{
   private org.jdom.Document document;
   private org.jdom.Document documentSave;
   private Element racine;
   private Element racineSave;
   private GameWorld gameworld;
   private boolean display;
   
   public void parseXML(GameWorld gameworld, String path, boolean display){
	   this.gameworld = gameworld;
	   this.display = display;
	   SAXBuilder sxb = new SAXBuilder();
	   try{
		   document = sxb.build(new File(path));
		}
		catch(Exception e){}

		racine = document.getRootElement();
   }
   
   
   public int parseSave(){
	   SAXBuilder sxb = new SAXBuilder();
	   try{
		   documentSave = sxb.build(new File("levels/save.xml"));
		}
		catch(Exception e){}

		racineSave = documentSave.getRootElement();
		List<?> listLvl = racineSave.getChildren("level");
	    Iterator<?> i = listLvl.iterator();
	    while(i.hasNext()){
	       Element courant = (Element)i.next();
	       if(courant.getAttributeValue("unlock").equals("true") && courant.getAttributeValue("heightscore").equals("0"))
		       return Integer.parseInt(courant.getAttributeValue("num"));
	    }
	    return 1;
   }
   
   public void unlockWorld(int lvl){
	  List<?> listLvl = racineSave.getChildren("level");
      Iterator<?> i = listLvl.iterator();
      while(i.hasNext()){
         Element courant = (Element)i.next();
         if(courant.getAttributeValue("num").equals(Integer.toString(lvl))){
            courant.setAttribute("unlock", "true");
         }
      }
   }
   
   public boolean isUnlock(int lvl){
	   List<?> listLvl = racineSave.getChildren("level");
	      Iterator<?> i = listLvl.iterator();
	      while(i.hasNext()){
	         Element courant = (Element)i.next();
	         if(courant.getAttributeValue("num").equals(Integer.toString(lvl))){
	            if(courant.getAttribute("unlock").getValue().equals("true")) return true;
	            else return false;
	         }
	        	 
	      }
	   return false;
   }
   
   public int getScore(int lvl){
		  List<?> listLvl = racineSave.getChildren("level");
	      Iterator<?> i = listLvl.iterator();
	      while(i.hasNext()){
	         Element courant = (Element)i.next();
	         if(courant.getAttributeValue("num").equals(Integer.toString(lvl))){
	            return Integer.parseInt(courant.getAttributeValue("heightscore"));
	         }
	      }
	      return 0;
	   }
   
   public void setScore(int lvl, int score){
		  List<?> listLvl = racineSave.getChildren("level");
	      Iterator<?> i = listLvl.iterator();
	      while(i.hasNext()){
	         Element courant = (Element)i.next();
	         if(courant.getAttributeValue("num").equals(Integer.toString(lvl))){
	            courant.setAttribute("heightscore", Integer.toString(score));
	         }
	      }
	   }
   
   public void save(){
      try{
         XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
         sortie.output(documentSave, new FileOutputStream("levels/save.xml"));
      }
      catch (java.io.IOException e){}
   }

   public void parseAllAndCreatorLevel()
   {
	   /*Récupération des munitions */
	   List<?> listMunitions = racine.getChildren("munition");
	   Iterator<?> iMunitions = listMunitions.iterator();
	   while(iMunitions.hasNext()) {
		   Element current = (Element)iMunitions.next();
		   
		   Shape shape = Shape.BOX;
		   if(current.getAttributeValue("shape").equals("circle")) {
			   shape = Shape.CIRCLE;
		   }
		   else if(current.getAttributeValue("shape").equals("triangle")){
			   shape = Shape.TRIANGLE;
		   }
		   else if(current.getAttributeValue("shape").equals("ramp")){
			   shape = Shape.RAMP;
		   }
		   
		   int width = Integer.parseInt(current.getAttributeValue("width"));
		   int height = Integer.parseInt(current.getAttributeValue("height"));
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
		   else if(current.getAttributeValue("type").equals("mammouth")){
			   material = Mat.MAMMOUTH;
		   }
		   else if(current.getAttributeValue("type").equals("mammouth2")){
			   material = Mat.MAMMOUTH2;
		   }
		   gameworld.addMunition(new Projectile(shape, width, height, new Vec2(x, y), angle, material));
      }  
	   
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
         else if(current.getAttributeValue("shape").equals("target")){
        	 shape = Shape.TARGET;
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
         else if(current.getAttributeValue("type").equals("target")){
        	 material = Mat.TARGET;
         }
         else if(current.getAttributeValue("type").equals("mammouth")){
        	 material = Mat.MAMMOUTH;
         }
         else if(current.getAttributeValue("type").equals("mammouth2")){
        	 material = Mat.MAMMOUTH2;
         }
         gameworld.addBlock(new Block(shape, width, height, new Vec2(x, y), angle, material));
         
      }
      
      List<?> listTargets = racine.getChildren("target");

      Iterator<?> iTargets = listTargets.iterator();
      while(iTargets.hasNext())
      {
         Element current = (Element)iTargets.next();

         if(display){
	         System.out.println("New target : \t"
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
         else if(current.getAttributeValue("shape").equals("target")){
        	 shape = Shape.TARGET;
         }
         
         float width = Float.parseFloat(current.getAttributeValue("width"));
         float height = Float.parseFloat(current.getAttributeValue("height"));
         float x = Float.parseFloat(current.getAttributeValue("posX"));
         float y = Float.parseFloat(current.getAttributeValue("posY"));
         float angle = Float.parseFloat(current.getAttributeValue("angle"));
         //Mat material = Mat.TARGET;
         Target t = new object.Target(shape, width, height, new Vec2(x, y), angle);
         gameworld.addBlock(t);
      }
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
      //On crŽe un nouveau filtre pour trouver tous les blocks "wood"
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