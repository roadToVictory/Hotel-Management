import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
		 Container container = getContentPane();
		 JLabel userLabel = new JLabel("Login");
		 JLabel passwordLabel = new JLabel("Hasło");
		 JTextField userTextField = new JTextField();
		 JPasswordField passwordField = new JPasswordField();
		 JButton loginButton = new JButton("Zaloguj");
		 JButton resetButton = new JButton("Reset");
		 JButton createButton = new JButton("Założ konto dla pracownika");
		 JCheckBox showPassword = new JCheckBox("Pokaż hasło");

		 public LoginFrame() {
			 setLayoutManager();
			 setLocationAndSize();
			 addComponentsToContainer();
			 addActionEvent();
			 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 }

		 public void setLayoutManager() {
			 container.setLayout(null);
		 }

		 public void setLocationAndSize() {
			 userLabel.setBounds(50, 150, 100, 30);
			 passwordLabel.setBounds(50, 220, 100, 30);
			 userTextField.setBounds(150, 150, 150, 30);
			 passwordField.setBounds(150, 220, 150, 30);
			 showPassword.setBounds(150, 250, 150, 30);
			 loginButton.setBounds(50, 300, 100, 30);
			 resetButton.setBounds(200, 300, 100, 30);
			 createButton.setBounds(75, 350, 200,30);
		 }

		 public void addComponentsToContainer() {
			 container.add(userLabel);
			 container.add(passwordLabel);
			 container.add(userTextField);
			 container.add(passwordField);
			 container.add(showPassword);
			 container.add(loginButton);
			 container.add(resetButton);
			 container.add(createButton);
		 }

		 public void addActionEvent() {
			 loginButton.addActionListener(this);
			 resetButton.addActionListener(this);
			 showPassword.addActionListener(this);
			 createButton.addActionListener(this);
		 }

		 @Override
		 public void actionPerformed(ActionEvent e) {
			 if (e.getSource() == loginButton) {
				 String userText= userTextField.getText();
				 String pwdText = String.valueOf(passwordField.getPassword());
				 if (DB.toLogged(userText, pwdText)){		//sprawdzania czy podane dane logowania są poprawne -> są w bazie
					 JOptionPane.showMessageDialog(this, "Zalogowano pomyślnie!","info", JOptionPane.INFORMATION_MESSAGE  );
					 EventQueue.invokeLater(new Runnable() {		//zapewnienie wykonania kodu z run() przez wątek do obsługi GUI Swing
						 @Override
						 public void run() {
							 new ActionFrame().getContentPane().setBackground( new java.awt.Color(255, 255, 255));
						 }
					 });

				 } else
					 JOptionPane.showMessageDialog(this, "Niepoprawny login lub hasło!", "info", JOptionPane.ERROR_MESSAGE);
			 }

			 if (e.getSource() == resetButton) {
				 userTextField.setText("");
				 passwordField.setText("");
			 }

			 if (e.getSource() == showPassword) {
				 if (showPassword.isSelected())
					 passwordField.setEchoChar((char) 0);
				 else
					 passwordField.setEchoChar('*');
			 }

			 if (e.getSource() == createButton) {
				 dispose();
				 new Workers();
			 }

		 }

	 }