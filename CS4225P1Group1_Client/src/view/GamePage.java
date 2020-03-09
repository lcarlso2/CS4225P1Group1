package view;


import java.io.IOException;

import application.Main;
import controller.GamePageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * The game page class that extends the page class 
 *  @author Tyler Scott, Dexter Tarver, and Lucas Carlson
 *
 */
public class GamePage extends Page {


	private GamePageController controller;

	@FXML
	private TextArea letterToGuessTextArea;

	@FXML
	private Button guessButton;

	@FXML
	private Label wordToGuessLabel;

	@FXML
	private Label errorMessageLabel;

	@FXML
	private Label usedLabel;

	@FXML
	private Text lettersUsedText;

	@FXML
	private TextArea serverResponseTextArea;

    @FXML
    private Button logoutButton;

	@FXML
	void handleSendClicked(MouseEvent event) {
		if (this.letterToGuessTextArea.getText().length() > 1) {
			this.errorMessageLabel.setVisible(true);
		} else {
			var result = this.controller.makeGuess(this.letterToGuessTextArea.getText());
			if (this.controller.checkIfGuessWasAlreadyMade(result)) {
				this.serverResponseTextArea.setText(result);
			} else if (this.controller.checkIfWrongGuessWasMade(result)) {
				this.serverResponseTextArea.setText(result);
			} else {
				this.wordToGuessLabel.setText(result);
			}
		}

	}
	
	@FXML
	void handLogoutClicked(MouseEvent event) throws IOException {
		this.controller.logout(MainPage.getCurrentUserName());
		this.handleMouseClickToNavigateToDifferentPage(event, Main.MAIN_PAGE_VIEW);
	}

	/**
	 * Initializes the instance of the log in page
	 * 
	 * @precondition none
	 * @postcondition the page is initialized
	 */
	@FXML
	void initialize() {
		this.controller = new GamePageController(this.serverResponseTextArea);
		this.errorMessageLabel.setVisible(false);
		
	}

}
