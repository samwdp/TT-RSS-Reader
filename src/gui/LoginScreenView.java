package gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.*;
import controller.LoginController;

/**
 * Login view of the application
 * 
 * @author Sam
 *
 */
public class LoginScreenView extends JFrame {
	private JPanel buttonPanel;
	private JButton loginBtn;
	private JPanel output;
	private JTextField usernameText;
	private JTextField passwordText;
	private JTextField urlText;
	private JLabel passwordLabel;
	private JLabel usernameLabel;
	private JLabel urlLabel;

	public LoginScreenView() {
		buttonPanel = new JPanel();
		output = new JPanel();
		usernameText = new JTextField(20);
		passwordText = new JTextField(20);
		urlText = new JTextField(30);
		loginBtn = new JButton("Login");
		passwordLabel = new JLabel("Password");
		usernameLabel = new JLabel("Username");
		urlLabel = new JLabel("Enter URL to you TT-RSS server");

		this.setTitle("Login to yout TT-RSS Account");

		// add panels to frame
		this.add(output, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		output.setLayout(new BoxLayout(output, BoxLayout.Y_AXIS));
		// add text fields to output panel
		output.add(usernameLabel);
		output.add(usernameText);
		output.add(passwordLabel);
		output.add(passwordText);
		output.add(urlLabel);
		output.add(urlText);

		// add buttons to panel
		buttonPanel.add(loginBtn);

		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// sets size
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		this.setSize(xSize / 2, ySize / 2);
		// centers screen
		this.setLocationRelativeTo(null);

	}

	/**
	 * gets the username the user input
	 * 
	 * @return s: String
	 */
	public String getUsername() {
		String s = usernameText.getText();
		return s;
	}

	/**
	 * gets the password the user enteres
	 * 
	 * @return s: String
	 */
	public String getPassword() {
		String s = passwordText.getText();
		return s;
	}

	/**
	 * gets the usl the user enteres
	 * 
	 * @return s: String
	 */
	public String getURL() {
		String s = urlText.getText();
		return s;
	}

	/**
	 * adds the action listener to the button that is the controller for the
	 * view
	 * 
	 * @param controller
	 *            : LoginController
	 */
	public void addController(LoginController controller) {
		loginBtn.addActionListener(controller);
	}

	/**
	 * hides the display with the dispose() method
	 */
	public void hideDisplay() {
		this.dispose();
	}

	/**
	 * sets the error message for the message box
	 * 
	 * @param message:
	 *            String
	 * @param title:
	 *            String
	 */
	public void setError(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
