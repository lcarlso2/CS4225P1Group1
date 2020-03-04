package view;

import java.util.ArrayList;

import controller.GamePageController;
import controller.MainPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class GamePage {

	private ArrayList<String> lettersGuessed;

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
	private TextArea guessLeftTextArea;

	@FXML
	void handleSendClicked(MouseEvent event) {
		if (this.letterToGuessTextArea.getText().length() > 1) {
			this.errorMessageLabel.setVisible(true);
		} else {
			var result = this.controller.makeGuess(this.letterToGuessTextArea.getText());
			if (this.controller.checkIfGuessWasMade(result)) {
				this.guessLeftTextArea.setText(result);
			} else if (this.controller.checkIfWrongGuessWasMade(result)){
				this.guessLeftTextArea.setText(result);
			} else {
				this.wordToGuessLabel.setText(result);
			}
		}

	}

	/**
	 * Initializes the instance of the log in page
	 * 
	 * @precondition none
	 * @postcondition the page is initialized
	 */
	@FXML
	void initialize() {
		this.controller = new GamePageController();
		this.lettersGuessed = new ArrayList<String>();
		this.errorMessageLabel.setVisible(false);

	}

}
