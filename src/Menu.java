import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.*;

 public class Menu extends JPanel implements ActionListener{

	 private final JButton selectButton;
	 private final JButton reportButton;
	 private final JButton insertButton;
	 private final JButton updateButton;
	 private final JComboBox<String> chooseTable;
	 private final JComboBox<String> insertTable;
	 private final JComboBox<String> chooseReport;
	 private final JComboBox<String> updateTable;
	 private final JButton formButton;
	 private final JButton showButton;
	 private final JButton showReportButton;
	 private final JButton updButton;

	 String[] tables = {"osoba", "lista_gosci", "rezerwacja", "adres", "sprzatanie", "wyzywienie", "dodatkowa_oplata",
			 "termin", "kategoria", "pokoj", "termin_pokoj"};
	 String[] views = {"rezerwacje", "wolne_pokoje", "pokoj_info", "goscie", "podsumowanie_rachunku", "pokoj_rachunek", "wyzywienie_rachunek",
						"sprzatanie_rachunek", "kupon", "ranking"};
 
 	 public Menu() {
		JLabel label1 = new JLabel("\nSystem Zarządzania Hotelem\n\n", SwingConstants.CENTER){
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				this.setFont(new Font("serif", Font.ITALIC|Font.BOLD, 40));
			}
		};

		this.setBackground(new Color(225, 239, 221));
		this.setLayout(new GridLayout(18,0));
		setPreferredSize(new Dimension(600,900));

		selectButton = new JButton("Przegladanie danych");
		reportButton = new JButton("Raporty");
		insertButton = new JButton("Wprowadzanie danych");
		updateButton = new JButton("Aktualizacja danych");

		selectButton.setFocusPainted(false);
		reportButton.setFocusPainted(false);
		insertButton.setFocusPainted(false);
		updateButton.setFocusPainted(false);

		selectButton.addActionListener(this);
		reportButton.addActionListener(this);
		insertButton.addActionListener(this);
		updateButton.addActionListener(this);

		selectButton.setForeground(new Color(21,37,0));
		selectButton.setBackground(Color.LIGHT_GRAY);
		reportButton.setForeground(new Color(21,37,0));
		reportButton.setBackground(Color.LIGHT_GRAY);
		insertButton.setForeground(new Color(21,37,0));
		insertButton.setBackground(Color.LIGHT_GRAY);
		updateButton.setForeground(new Color(21,37,0));
		updateButton.setBackground(Color.LIGHT_GRAY);

		chooseReport = new JComboBox<>(views);
		chooseTable = new JComboBox<>(tables);
		insertTable = new JComboBox<>(tables);
		updateTable = new JComboBox<>(tables);

		chooseTable.addActionListener(this);
		insertButton.addActionListener(this);
		chooseReport.addActionListener(this);
		updateTable.addActionListener(this);

		formButton = new JButton("Wybierz");
		showButton = new JButton("Wybierz");
		showReportButton = new JButton("Wybierz");
		updButton = new JButton("Wybierz");

		formButton.addActionListener(this);
		showButton.addActionListener(this);
		showReportButton.addActionListener(this);
		updButton.addActionListener(this);

		formButton.setVisible(false);
		chooseTable.setVisible(false);
		insertTable.setVisible(false);
		showButton.setVisible(false);

		chooseReport.setVisible(false);
		showReportButton.setVisible(false);
		updButton.setVisible(false);
		updateTable.setVisible(false);

		this.add(label1);
		this.add(new JLabel(" "), "span, grow");
		this.add(selectButton);
		this.add(chooseTable);
		this.add(showButton);
		add(new JLabel(" "), "span, grow");
		this.add(reportButton);
		this.add(chooseReport);
		this.add(showReportButton);
		add(new JLabel(" "), "span, grow");
		this.add(insertButton);
		this.add(insertTable);
		this.add(formButton);
		add(new JLabel(" "), "span, grow");
		this.add(updateButton);
		this.add(updateTable);
		this.add(updButton);

		JLabel label = new JLabel("Stworzone przez: Damian Koperstyński, [BD1] gr4", SwingConstants.CENTER);
		add(label);
 	}

 	@Override
 	public void actionPerformed(ActionEvent e) {	//obsluga zdarzen pochodzacych od przyciskow
 		Object source = e.getSource();
		if(source == selectButton) {
			formButton.setVisible(false);
			showReportButton.setVisible(false);
			chooseReport.setVisible(false);
			chooseTable.setVisible(true);
			showButton.setVisible(true);
			insertTable.setVisible(false);
			updateTable.setVisible(false);
			updButton.setVisible(false);
		}
		else if(source == reportButton) {
			showButton.setVisible(false);
			formButton.setVisible(false);
			chooseTable.setVisible(false);
			chooseReport.setVisible(true);
			showReportButton.setVisible(true);
			insertTable.setVisible(false);
			updateTable.setVisible(false);
			updButton.setVisible(false);
		}
 		else if(source == insertButton) {
			showButton.setVisible(false);
			showReportButton.setVisible(false);
			insertTable.setVisible(true);
			formButton.setVisible(true);
			chooseTable.setVisible(false);
			chooseReport.setVisible(false);
			updateTable.setVisible(false);
			updButton.setVisible(false);
		}
		else if(source == updateButton) {
			showButton.setVisible(false);
			showReportButton.setVisible(false);
			insertTable.setVisible(false);
			formButton.setVisible(false);
			chooseTable.setVisible(false);
			chooseReport.setVisible(false);
			updateTable.setVisible(true);
			updButton.setVisible(true);
		}
		else if(source == showButton)
			new TableFrame((String) chooseTable.getSelectedItem());
		else if(source == showReportButton)
			new TableFrame((String) chooseReport.getSelectedItem());
		else if(source == formButton)
			new FormFrame((String) insertTable.getSelectedItem());
		else if(source == updButton)
			new UpdateFrame((String) updateTable.getSelectedItem());
 	}
 }
