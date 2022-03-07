import javax.swing.*;
 
 public class FormFrame extends JFrame {
 	public FormFrame(String table) {
        add(new Form(table));
 		pack();
 		setVisible(true);
     }
 }
