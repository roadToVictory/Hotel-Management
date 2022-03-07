import javax.swing.*;
 
 public class UpdateFrame extends JFrame {
 
 	public UpdateFrame(String table) {
        super("Aktualizacja danych w tabeli: " + table);
        add(new Update(table));
 		pack();
 		setVisible(true);
     }
 }
