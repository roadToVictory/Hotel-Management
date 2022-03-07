import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Workers extends JFrame implements ActionListener {

		 JLabel nameLabel = new JLabel("Imie");
		 JLabel surnameLabel = new JLabel("Nazwisko");
		 JLabel login = new JLabel("Login");
		 JLabel passwordLabel = new JLabel("Hasło");
		 JTextField nameTextField = new JTextField();
		 JTextField surnameTextField = new JTextField();
		 JTextField loginTextField = new JTextField();
		 JPasswordField passwordField = new JPasswordField();
		 
		 JButton confirmButton = new JButton("Potwierdź");
		 JButton resetButton = new JButton("Reset");
		 JButton returnButton = new JButton("Powrót");
		 JCheckBox showPassword = new JCheckBox("Pokaż hasło");

		Workers() {
			super("Dodawanie pracownika");
			this.setSize(400,450);
			this.setLayout(null);
			this.setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			nameLabel.setBounds(50, 50, 100, 30);
			nameTextField.setBounds(170,50, 150,30);
			surnameLabel.setBounds(50, 100, 100, 30);
			surnameTextField.setBounds(170, 100, 150,30);
			login.setBounds(50, 150, 100, 30);
			loginTextField.setBounds(170,150, 150,30);
			passwordLabel.setBounds(50, 200, 100, 30);
			passwordField.setBounds(170, 200, 150,30);

			confirmButton.setBounds(60, 270, 100,30);
			resetButton.setBounds(210, 270, 100,30);
			showPassword.setBounds(170, 230, 100, 30);
			returnButton.setBounds(100, 320, 180,30);

			this.add(nameLabel);
			this.add(nameTextField);
			this.add(surnameLabel);
			this.add(surnameTextField);
			this.add(login);
			this.add(loginTextField);
			this.add(passwordLabel);
			this.add(passwordField);
			this.add(confirmButton);
			this.add(resetButton);
			this.add(showPassword);
			this.add(returnButton);

			confirmButton.addActionListener(this);
			resetButton.addActionListener(this);
			showPassword.addActionListener(this);
			returnButton.addActionListener(this);
		 }

		 @Override
		 public void actionPerformed(ActionEvent e) {
			 if (e.getSource() == confirmButton) {

				 JPasswordField pass = new JPasswordField(10);
				 JOptionPane.showConfirmDialog(null, pass,"Podaj hasło kierownika",JOptionPane.OK_CANCEL_OPTION);
				 String pwdText = String.valueOf(pass.getPassword());

				 if(pwdText.equals("admin")){
					 JOptionPane.showMessageDialog(null, "Hasło poprawne!", "Info", JOptionPane.INFORMATION_MESSAGE);
					 String name = nameTextField.getText();
					 String surname = surnameTextField.getText();
					 String loginWorker = loginTextField.getText();
					 String passwordWorker = String.valueOf(passwordField.getPassword());

					 if(name.length()!=0 && surname.length()!=0 && loginWorker.length()!=0 && passwordWorker.length()!=0) {
						 if(DB.addWorker(name, surname, loginWorker, passwordWorker))
							 JOptionPane.showMessageDialog(this, "Dodano pracownika!", "info", JOptionPane.INFORMATION_MESSAGE);
						 else JOptionPane.showMessageDialog(null, "Nie dodano pracownika! Upewnij się, że login nie jest już zajęty.", "Error!", JOptionPane.ERROR_MESSAGE);

						 dispose();
						 LoginFrame frame = new LoginFrame();
						 frame.setTitle("System zarządzania hotelem");
						 frame.setBounds(10, 10, 370, 600);
						 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						 frame.setResizable(false);
						 frame.setVisible(true);
					 }else
						 JOptionPane.showMessageDialog(null, "Nie wypełniono wymaganych pól!", "Error!", JOptionPane.ERROR_MESSAGE);

				 }else
					 JOptionPane.showMessageDialog(null, "Niepoprawne hasło!", "Error!", JOptionPane.ERROR_MESSAGE);
			 }

			 if (e.getSource() == resetButton) {
				 nameTextField.setText("");
				 surnameTextField.setText("");
				 loginTextField.setText("");
				 passwordField.setText("");
			 }

			 if (e.getSource() == showPassword) {
				 if (showPassword.isSelected())
					 passwordField.setEchoChar((char) 0);
				 else
					 passwordField.setEchoChar('*');
			 }

			 if(e.getSource() == returnButton){
				 dispose();
				 LoginFrame frame = new LoginFrame();
				 frame.setTitle("System zarządzania hotelem");
				 frame.setBounds(10, 10, 370, 600);
				 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				 frame.setResizable(false);
				 frame.setVisible(true);
			 }

		 }
	 }