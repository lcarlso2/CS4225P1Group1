package view;

import java.io.IOException;
import java.util.HashMap;

import application.Main;
import controller.GamePageController;
import controller.MainPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * The game page class that extends the page class
 * 
 * @author Tyler Scott, Dexter Tarver, and Lucas Carlson
 *
 */
public class GamePage extends Page {

	private GamePageController controller;
	private HashMap<Integer, ImageView> images;

	@FXML
	private TextArea letterToGuessTextArea;

	@FXML
	private Button guessButton;

	@FXML
	private Label wordToGuessLabel;
	
	@FXML
	private Label usedLettersLabel;

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
	private ImageView gameLostImage;

	@FXML
	private ImageView fourAttemptsLeftImage;

	@FXML
	private ImageView threeAttemptsLeftImage;

	@FXML
	private ImageView twoAttemptsLeftImage;

	@FXML
	private ImageView oneAttemptLeftImage;

	@FXML
	private TextArea timerTextArea;

	@FXML
	void handleSendClicked(MouseEvent event) {
		this.errorMessageLabel.setText("One letter at a time please.");
		if (this.letterToGuessTextArea.getText().length() > 1) {
			this.errorMessageLabel.setVisible(true);
		} else if (this.letterToGuessTextArea.getText().isBlank()) {
			this.errorMessageLabel.setText("Invalid guess");
			this.errorMessageLabel.setVisible(true);
		} else {
			var result = this.controller.makeGuess(this.letterToGuessTextArea.getText());
			if (this.controller.checkIfGuessWasAlreadyMade(result)) {
				this.serverResponseTextArea.setText(result);
			} else if (this.controller.checkIfWrongGuessWasMade(result)) {
				this.serverResponseTextArea.setText(result);
			} else if (this.controller.checkIfGameIsOver(result)) {
				this.handleGameOver(result);
			} else {
				this.wordToGuessLabel.setText(result);
			}
		}
		this.letterToGuessTextArea.setText("");
	}

	private void handleGameOver(String result) {
		this.gameLostImage.setVisible(true);
		this.serverResponseTextArea.setText(result.split(":")[0]);
		if (result.contains(":")) {
			this.wordToGuessLabel.setText(result.split(":")[1]);
		}
		this.guessButton.setDisable(true);
	}

	@FXML
	void handLogoutClicked(MouseEvent event) throws IOException {
		this.controller.logout(MainPageController.getCurrentUserName());
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
		this.setDefaultImageVisibility();
		this.controller = new GamePageController(this.serverResponseTextArea, this.wordToGuessLabel, this.guessButton,
				this.images, this.timerTextArea, this.usedLettersLabel);
		this.errorMessageLabel.setVisible(false);
		this.wordToGuessLabel.setText(MainPageController.getWordToGuess());

	}

	private void setDefaultImageVisibility() {
		this.images = new HashMap<Integer, ImageView>();
		this.gameLostImage.setVisible(false);
		this.fourAttemptsLeftImage.setVisible(false);
		this.threeAttemptsLeftImage.setVisible(false);
		this.twoAttemptsLeftImage.setVisible(false);
		this.oneAttemptLeftImage.setVisible(false);
		this.images.put(0, this.gameLostImage);
		this.images.put(4, this.fourAttemptsLeftImage);
		this.images.put(3, this.threeAttemptsLeftImage);
		this.images.put(2, this.twoAttemptsLeftImage);
		this.images.put(1, this.oneAttemptLeftImage);
	}

}
