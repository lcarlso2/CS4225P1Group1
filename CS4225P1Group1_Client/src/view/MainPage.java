package view;

import java.io.IOException;

import application.Main;
import controller.MainPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * The main page class that extends the page class
 * @author Tyler Scott, Dexter Tarver, and Lucas Carlson
 *
 */
public class MainPage extends Page {

	private static String currentUserName;
	
	private MainPageController controller;

	@FXML
	private TextField usernameTextField;

	@FXML
	private TextField passwordTextField;

	@FXML
	private Text usernameText;

	@FXML
	private Text passwordText;

	@FXML
	private Button loginButton;

	@FXML
	private Label errorMessageLabel;

	@FXML
	void handleLoginClicked(MouseEvent event) throws IOException {
		this.errorMessageLabel.setText("Invalid login");
		var result = this.controller.validateLoginCredentials(this.usernameTextField.getText(),
				this.passwordTextField.getText());
		if (result.equals("valid")) {
			currentUserName = this.usernameTextField.getText();
			this.handleMouseClickToNavigateToDifferentPage(event, Main.GAME_PAGE_VIEW);
		} else if (result.startsWith("Error")) {
			this.errorMessageLabel.setText(result);
			this.errorMessageLabel.setVisible(true);
		} else {
			this.errorMessageLabel.setVisible(true);
		}

	}


	/**
	 * Handles the click event for when the user clicks the user name or password
	 * text field
	 * 
	 * @precondition none
	 * @postcondition the error message is hidden
	 * @param event the event
	 * @throws IOException
	 */
	@FXML
	void handleClick(MouseEvent event) throws IOException {
		this.errorMessageLabel.setVisible(false);
	}


	/**
	 * Initializes the instance of the log in page
	 * 
	 * @precondition none
	 * @postcondition the page is initialized
	 */
	@FXML
	void initialize() {
		this.controller = new MainPageController();
		this.errorMessageLabel.setVisible(false);
	}
	
	/**
	 * Gets the current users user name
	 * @return the user name
	 */
	public static String getCurrentUserName() {
		return currentUserName;
	}

}