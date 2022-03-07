import javax.swing.*;
import java.awt.*;

public class ActionFrame extends JFrame {
 	public ActionFrame() {
 		super("Projekt BD1 - obsługa hotelu");
		Frame[] fr = LoginFrame.getFrames(); fr[0].dispose();	//zamknięcie okna logowania
		add(new Menu());
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 		setVisible(true);
 	}
 }
