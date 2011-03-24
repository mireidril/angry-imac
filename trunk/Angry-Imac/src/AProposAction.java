import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class AProposAction extends AbstractAction {
	private GameWindow fenetre;
	
	public AProposAction(GameWindow fenetre, String texte){
		super(texte);
		this.fenetre = fenetre;
	}
	
	public void actionPerformed(ActionEvent e) { 
		JOptionPane.showMessageDialog(fenetre, "Ce programme a été développé par <Nom du développeur>");
	} 
}