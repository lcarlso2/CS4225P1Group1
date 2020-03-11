package controller;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import model.Message;

/**
 * The game page controller class
 * 
 * @author Lucas Carlson, Tyler Scott, Dexter Tarver
 *
 */
public class GamePageController {

	private static TextArea serverResponse;
	private static TextArea timer;
	private static Label wordToGuess;
	private static Label usedLettersLabel;
	private static int remainingAttempts;
	private static Button guessButton;
	private static HashMap<Integer, ImageView> hangmanImages;
	private static StringBuilder guessedLetters;
	

	/**
	 * Creates a new game page controller with the specified output text area
	 * 
	 * @param output            the output
	 * @param wordToBeGuessed   the word being guessed
	 * @param buttonToSendGuess the button to send a guess
	 * @param images the hangman images to display when the game is played
	 * @param timerTextArea the timer text area
	 * @param lettersUsedLabel label to display any letters that have been used
	 * @precondition none
	 * @postcondition a new controller is created
	 */
	public GamePageController(TextArea output, Label wordToBeGuessed, Button buttonToSendGuess,
			HashMap<Integer, ImageView> images, TextArea timerTextArea, Label lettersUsedLabel) {
		serverResponse = output;
		wordToGuess = wordToBeGuessed;
		guessButton = buttonToSendGuess;
		hangmanImages = images;
		remainingAttempts = 5;
		timer = timerTextArea;
		usedLettersLabel = lettersUsedLabel;
		guessedLetters = new StringBuilder();
	}
	
	
	/**
	 * Sets the timer text area
	 * 
	 * @param timerValue the timer from the server
	 * @precondition none
	 * @postcondition the timer text area is set
	 */
	public static void setTimer(String timerValue) {
		timer.setText(timerValue);
	}

	/**
	 * Sets the servers response text area
	 * 
	 * @param response the response from the server
	 * @precondition none
	 * @postcondition the server response is set
	 */
	public static void setServerResponse(String response) {
		var currentText = serverResponse.getText();
		serverResponse.setText(response + System.lineSeparator() + currentText);
	}

	/**
	 * Sets the word being guessed
	 * 
	 * @param word the word being guessed
	 * @precondition none
	 * @postcondition the word is set
	 */
	public static void setWordBeingGuessed(String word) {
		wordToGuess.setText(word);
	}

	/**
	 * Sets the guess button to disabled
	 * 
	 * @precondition none
	 * @postcondition the button is disabled
	 */
	public static void disableGuessButton() {
		guessButton.setDisable(true);
	}
	
	/**
	 * Sets the guess button to enabled
	 * 
	 * @precondition none
	 * @postcondition the button is enabled
	 */
	public static void enableGuessButton() {
		guessButton.setDisable(false);
	}

	/**
	 * Sends a guess to the server
	 * 
	 * @param letterToGuess the letter being guessed
	 * @return the response from the server
	 */
	public String makeGuess(String letterToGuess) {
		if (!guessedLetters.toString().contains(letterToGuess + "  ")) {
			guessedLetters.append(letterToGuess + "  ");
			usedLettersLabel.setText(guessedLetters.toString());
		}
		var message = new Message("GUESS---" + MainPageController.getCurrentUserName() + ":" + letterToGuess);
		MainPageController.getClient().sendMessage(message.getSerializedMessage());
		Message messageRecieved = null;
		while (MainPageController.getClient().getGameMessages().isEmpty()) {
			System.out.print("Waiting for server in makeGuess");
		}

		try {
			messageRecieved = MainPageController.getClient().getGameMessages().remove();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		if (messageRecieved != null) {
			System.out.println("MESSAGE: " + messageRecieved.getMessage());
			Platform.runLater(() -> GamePageController.disableGuessButton());
			return messageRecieved.getMessage();
		} else {
			return "Error: Something went wrong";
		}
	}

	/**
	 * Checks if the guess was already made
	 * 
	 * @param message the message from the server
	 * @return true if the letter was guessed
	 */
	public boolean checkIfGuessWasAlreadyMade(String message) {
		return message.startsWith("You already guessed ");
	}

	/**
	 * Checks if the guess was wrong
	 * 
	 * @param message the message from the server
	 * @return true if the guess was wrong
	 */
	public boolean checkIfWrongGuessWasMade(String message) {
		boolean wrongGuess = message.startsWith("Uh-oh");
		if (wrongGuess) {
			remainingAttempts--;
			hangmanImages.get(remainingAttempts).setVisible(true);
		}
		return wrongGuess;
	}

	/**
	 * Checks if the game is over
	 * 
	 * @param message the message from the server
	 * @return true if the game is over
	 */
	public boolean checkIfGameIsOver(String message) {
		return message.contains("Game over") || message.contains("You won");
	}

	/**
	 * Logs the user out
	 * 
	 * @param username the current user's username
	 * @precondition none
	 * @postcondition the user is logged out
	 */
	public void logout(String username) {
		var message = new Message("QUIT---user:" + username + " ");
		MainPageController.getClient().sendMessage(message.getSerializedMessage());
		MainPageController.terminateThreads();

	}

}
