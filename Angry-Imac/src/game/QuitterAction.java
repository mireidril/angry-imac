package game;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class QuitterAction extends AbstractAction {
	public QuitterAction(String texte){
		super(texte);
	}
	
	public void actionPerformed(ActionEvent e) { 
		System.exit(0);
	} 
}