package game;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
/**
 * Classe d'action Quitter
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Celine, KIELB Adrien et ROLDAO Timothee
 * @version 1.0
 */
@SuppressWarnings("serial")
public class QuitterAction extends AbstractAction {
	/**
	* 	Constructeur d'une action "Quitter"
		@param  text 		Texte du Quitter                
	*/
	public QuitterAction(String text){
		super(text);
	}
	
	/**
	* 	Action declanche lors de l'appui sur le bouton Quitter 
	*  
		@param  e	Evenement du clic sur Quitter	           
	*/
	public void actionPerformed(ActionEvent e) { 
		System.exit(0);
	} 
}