import javax.swing.*;

public class TableFrame extends JFrame {
 
 	public TableFrame(String table) {
 		super("Dane " + table);
        add(new Table(table));
 		pack();
 		setVisible(true);
     }
 }