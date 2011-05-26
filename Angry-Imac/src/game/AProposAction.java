package game;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * Classe d'action A propos
 * @author BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Celine, KIELB Adrien et ROLDAO Timothee
 * @version 1.0
 */
@SuppressWarnings("serial")
public class AProposAction extends AbstractAction {
	private GameWindow fenetre;
	
	/**
	* 	Constructeur d'une action "a propos"
	*  
		@param  fenetre 	Fenetre du jeu.
		@param  text 		Titre du A propos                
	*/
	public AProposAction(GameWindow fenetre, String text){
		super(text);
		this.fenetre = fenetre;
	}
	
	/**
	* 	Action declanche lors de l'appui sur le bouton A propos 
	*  
		@param  e	Evenement du clic sur A propos	           
	*/
	public void actionPerformed(ActionEvent e) { 
		JOptionPane.showMessageDialog(fenetre, "Ce programme a été développé par BRUNELIERE Adrien, CHARBONNIER Fiona, COGNY Céline, KIELB Adrien et ROLDAO Timothée");
	} 
}