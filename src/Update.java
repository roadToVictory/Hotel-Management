import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class Update extends JPanel implements ActionListener{
     private final JButton submitButton;
     ArrayList<String> content;
     ArrayList<JTextField> textField;
     String table;
     JTextArea textArea;
     JTable jTable;
     JScrollPane ScrollPane;
     Object[][] data;

    public Update(String table){
        this.table = table;
        content = new ArrayList<>();
        textField = new ArrayList<>();
        setLayout(new FlowLayout(FlowLayout.TRAILING,210,10));
        this.setPreferredSize(new Dimension(700, 700));

        setForm(table, Table.getFieldsName(table));

        submitButton = new JButton("Aktualizuj");
        submitButton.setLocation(350, 160);
        submitButton.setSize(200, 20);
        submitButton.addActionListener(this);
        submitButton.setAlignmentX(200);
        add(submitButton);

        textArea = new JTextArea(5, 20);
        textArea.setBounds(300, 50, 300, 100);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);    //ładniejsze wyświetlanie, przenoszenie słow do nowej linii
        add(textArea);

        /////////////////////////////////////////

        String[] columns = Table.getFieldsName(table);
        ArrayList<ArrayList<String>> datas = DB.selectQuery(table, columns);  //wynik polecenia select dla podanej jako argument tablicy

        data = new Object[datas.size()][datas.get(0).size()];
        for(int i = 0; i < datas.size(); i++)
            for(int j = 0; j < datas.get(i).size(); j++)
                data[i][j] = datas.get(i).get(j);

        if(table.equals("sprzatanie"))
            for (int i = 0; i < data.length; i++)
                data[i][1] = data[i][1].equals("t") ? "tak" : "nie";

        if(table.equals("rezerwacje"))
            for (int i = 0; i < data.length; i++)
                data[i][data[0].length-1] = data[i][data[0].length-1].equals("t") ? "tak" : "nie";

        jTable = new JTable(data, columns);
        add(jTable);
        ScrollPane = new JScrollPane(jTable);
        jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
        jTable.setFillsViewportHeight(true);
        jTable.setSize(1000,1000);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        jTable.setDefaultRenderer(String.class, center);
        add(ScrollPane, BorderLayout.CENTER);

        jTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int w = jTable.getSelectedRow();
                    TableModel model = jTable.getModel();
                    int i=1;
                    if(table.equals("termin_pokoj")) i=0;
                    for (var text: textField){
                        text.setText(String.valueOf(model.getValueAt(w, i)));
                        i++;
                    }
                }
            }
        });

    }
 
     @Override
 	public void actionPerformed(ActionEvent e) {
 		Object source = e.getSource();
 		if(source == submitButton) {
            content.clear();
            content.add(table);
            content.add( (String) data[jTable.getSelectedRow()][0] ); // id -> dodany osobno poniewaz nie ma mozliwosci jego zmiany podczas edytowania danych

            if (table.equals("termin_pokoj")) {
                content.clear();
                content.add(table);
            }

            String pwdText="";
            if (!DB.isAdmin) {
                JPasswordField pass = new JPasswordField(10);
                JOptionPane.showConfirmDialog(null, pass, "Podaj hasło kierownika", JOptionPane.OK_CANCEL_OPTION);
                pwdText = String.valueOf(pass.getPassword());
            }

            String message;
            if (pwdText.equals("admin") || DB.isAdmin) {
                if(pwdText.equals("admin"))
                    JOptionPane.showMessageDialog(null, "Hasło poprawne!", "Info", JOptionPane.INFORMATION_MESSAGE);

                for (var textField : textField)
                    content.add(textField.getText());   //budowanie polecenia sql

                System.out.println(content);
                message = DB.updateQuery(content);
                if (message == null) message = "Dane zostały pomyślnie zaktualizowane.";

            } else {
                JOptionPane.showMessageDialog(null, "Niepoprawne hasło! ", "Błąd!", JOptionPane.ERROR_MESSAGE);
                message = "Niepoprawne hasło!\nDane nie zostały zaktualizowane.";
            }

            textArea.setText(message);
            revalidate();
            repaint();
         }
    }

    void setForm(String table, String[] fields) {
        int x = 10;
        int i = 1;
        if(table.equals("termin_pokoj")) i = 0;  //brak auto increment dla PK

        for(int j=0; i< fields.length; x+=50, i++, j++) {
            JLabel label = new JLabel(fields[i]);
            label.setSize(90, 20);
            label.setLocation(30, x);
            this.add(label);

            textField.add(new JTextField(30));
            textField.get(j).setLocation(30, x + 20);
            this.add(textField.get(j));
        }
    }

}
