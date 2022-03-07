import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
 
 public class Form extends JPanel implements ActionListener{

     String tableName;
     JButton submitButton;
     JTextArea textArea;
     ArrayList<String> data;
     ArrayList<JTextField> textField;

 	public Form(String table){
        this.tableName = table;
        data = new ArrayList<>();
        textField = new ArrayList<>();

        setLayout(null);
        setPreferredSize(new Dimension(650, 500));

        setForm(table, Table.getFieldsName(table));

        submitButton = new JButton("Potwierdz");
        submitButton.setLocation(30, 450);
        submitButton.setSize(200, 20);
        submitButton.addActionListener(this);
        add(submitButton);

        textArea = new JTextArea(5, 20);
        textArea.setBounds(30, 330, 300, 100);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);    //ładniejsze wyświetlanie, przenoszenie słow do nowej linii
        textArea.setEditable(false);
        add(textArea);
 	}

    @Override
 	public void actionPerformed(ActionEvent e) {
 		Object source = e.getSource();

 		if(source == submitButton) {
            data.clear();
            data.add(tableName);
            String pwdText = "";
            if(tableName.equals("kategoria") || tableName.equals("pokoj")){
                if(!DB.isAdmin) {
                    JPasswordField pass = new JPasswordField(10);
                    JOptionPane.showConfirmDialog(null, pass, "Podaj hasło kierownika", JOptionPane.OK_CANCEL_OPTION);
                    pwdText = String.valueOf(pass.getPassword());
                }
                if(pwdText.equals("admin") || DB.isAdmin){
                    if(pwdText.equals("admin"))
                        JOptionPane.showMessageDialog(null, "Hasło poprawne!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(null, "Nie masz uprawnien do tej akcji!", "Błąd!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            for (var textField : textField)
                data.add(textField.getText());

             System.out.println(data);
             String message = DB.insertQuery(data);

             if(message == null) message = "Dane zostały pomyślnie dodane.";

             textArea.setText(message);
             revalidate();
             repaint();
         }
 	}

    void setForm(String table, String[] fields) {
        int x = 10;
        int i = 1;
        if(table.equals("termin_pokoj")) i = 0;  //brak auto increment dla PK

        for(int j=0; i< fields.length; x+=50, i++,j++) {
            JLabel label = new JLabel(fields[i]);
            label.setSize(120, 20);
            label.setLocation(30, x);
            add(label);

            textField.add(new JTextField());
            textField.get(j).setSize(200, 20);
            textField.get(j).setLocation(30, x + 20);
            add(textField.get(j));

            switch (table) {
                case "wyzywienie" -> {
                    JLabel wyz = new JLabel("Kategorie wyzywienia - opis");
                    wyz.setSize(220, 20);
                    wyz.setLocation(365, 20);
                    add(wyz);
                    JTextArea category = new JTextArea(5, 20);
                    category.setText("""
                            1 -> BB - samo śniadanie, 30zł\s
                            2 -> HB - śniadanie i obiadokolacja, 70zł\s
                            3 -> FB - pełne wyżywienie (śniadanie, obiad, kolacja), 90zł
                            4 -> AI - pełne wyżywienie + darmowy barek i przekąski, 120zł
                            5 -> OV - bez wyżywienia, 0zł\s
                            \s""");
                    category.setBounds(280, 50, 340, 80);
                    category.setEditable(false);
                    category.setLineWrap(true);
                    category.setWrapStyleWord(true);    //ładniejsze wyświetlanie, przenoszenie słow do nowej linii

                    add(category);
                }
                case "sprzatanie" -> {
                    JLabel spr = new JLabel("Sugerowana cena za sprzatanie");
                    spr.setSize(280, 20);
                    spr.setLocation(365, 20);
                    add(spr);
                    JTextArea category = new JTextArea(5, 20);
                    category.setText("codzienne -> 40zł \n" +
                            "tylko po wyjeździe (jeden raz) -> 100zł");
                    category.setBounds(280, 50, 340, 35);
                    category.setEditable(false);
                    category.setLineWrap(true);
                    category.setWrapStyleWord(true);    //ładniejsze wyświetlanie, przenoszenie słow do nowej linii

                    add(category);
                }
                case "rezerwacja", "pokoj" -> {
                    JLabel pok = new JLabel("Kategorie pokoi - opis");
                    pok.setSize(250, 20);
                    pok.setLocation(365, 20);
                    add(pok);
                    JTextArea category = new JTextArea(5, 20);
                    category.setText("""
                            1 -> 1 podwojne łóżko, 250zł\s
                            2 -> 1 łóżko, 200zł\s
                            3 -> 1 podwojne łóżko + 1 dziecięce, 270zł
                            4 -> 2 łózka, 300zł
                            5 -> 3 łózka, 400zł\s
                            \s""");
                    category.setBounds(280, 50, 340, 80);
                    category.setEditable(false);
                    category.setLineWrap(true);
                    category.setWrapStyleWord(true);    //ładniejsze wyświetlanie, przenoszenie słow do nowej linii

                    add(category);
                }
            }
        }


    }

 	
 }
