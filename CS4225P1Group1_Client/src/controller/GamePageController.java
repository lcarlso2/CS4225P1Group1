package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Message;

/**
 * The game page controller class
 * @author Lucas Carlson, Tyler Scott, Dexter Tarver
 *
 */
public class GamePageController {
	
	private static TextArea serverResponse;
	
	private static Label wordToGuess;
	
	private static Button guessButton;

	/**
	 * Creates a new game page controller with the specified output text area
	 * @param output the output
	 * @param wordToBeGuessed the word being guessed
	 * @param buttonToSendGuess the button to send a guess
	 * @precondition none
	 * @postcondition a new controller is created
	 */
	public GamePageController(TextArea output, Label wordToBeGuessed, Button buttonToSendGuess) {
		serverResponse = output;
		wordToGuess = wordToBeGuessed;
		guessButton = buttonToSendGuess;
	}
	
	/**
	 * Sets the servers response text area
	 * @param response the response from the server
	 * @precondition none
	 * @postcondition the server response is set
	 */
	public static void setServerResponse(String response) {
		serverResponse.setText(response);
	}
	
	/**
	 * Sets the word being guessed
	 * @param word the word being guessed
	 * @precondition none
	 * @postcondition the word is set 
	 */
	public static void setWordBeingGuessed(String word) {
		wordToGuess.setText(word);
	}
	
	/**
	 * Sets the guess button to disabled
	 * @precondition none
	 * @postcondition the button is disabled
	 */
	public static void disableGuessButton() {
		guessButton.setDisable(true);
	}
	
	
	/**
	 * Sends a guess to the server
	 * @param letterToGuess the letter being guessed
	 * @return the response from the server
	 */
	public String makeGuess(String letterToGuess) {
		var message = new Message("GUESS---" + MainPageController.getCurrentUserName() + ":" + letterToGuess);

		MainPageController.getClient().sendMessage(message.getSerializedMessage());
		
		Message messageRecieved = null;
		while (MainPageController.getClient().getGameMessages().isEmpty()) {
			System.out.println("waiting in guess...");
		}
	
		try {
			messageRecieved = MainPageController.getClient().getGameMessages().remove();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		if (messageRecieved != null) {
			System.out.println("MESSAGE: " + messageRecieved.getMessage());
			return messageRecieved.getMessage();
		} else {
			return "Error: Something went wrong";
		}
	}
	
	/**
	 * Checks if the guess was already made
	 * @param message the message from the server 
	 * @return true if the letter was guessed
	 */
	public boolean checkIfGuessWasAlreadyMade(String message) {
		return message.startsWith("You already guessed ");
	}
	
	/**
	 * Checks if the guess was wrong 
	 * @param message the message from the server
	 * @return true if the guess was wrong 
	 */
	public boolean checkIfWrongGuessWasMade(String message) {
		return message.startsWith("Uh-oh");
	}
	
	/**
	 * Checks if the game is over
	 * @param message the message from the server
	 * @return true if the game is over
	 */
	public boolean checkIfGameIsOver(String message) {
		return message.contains("Game over") || message.contains("You won");
	}
	
	/**
	 * Logs the user out 
	 * @param username the current user's username
	 * @precondition none
	 * @postcondition the user is logged out
	 */
	public void logout(String username) {
		var message = new Message("QUIT---user:" + username + " ");
		MainPageController.getClient().sendMessage(message.getSerializedMessage());
		MainPageController.endListeningThread();
		
		
	}

	

}
