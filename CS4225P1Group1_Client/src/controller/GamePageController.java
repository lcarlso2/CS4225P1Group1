package controller;

import javafx.scene.control.TextArea;
import model.Message;

/**
 * The game page controller class
 * @author Lucas Carlson, Tyler Scott, Dexter Tarver
 *
 */
public class GamePageController {
	
	private static TextArea serverResponse;

	/**
	 * Creates a new game page controller with the specified output text area
	 * @param output the output
	 * @precondition none
	 * @postcondition a new controller is created
	 */
	public GamePageController(TextArea output) {
		serverResponse = output;
	}
	
	/**
	 * Sets the servers response text area
	 * @param response the response from the server
	 * @precondition none
	 * @postcondition none
	 */
	public static void setServerResponse(String response) {
		serverResponse.setText(response);
	}
	/**
	 * Sends a guess to the server
	 * @param letterToGuess the letter being guessed
	 * @return the response from the server
	 */
	public String makeGuess(String letterToGuess) {
		var message = new Message("Guess---" + letterToGuess);
		MainPageController.getClient().sendMessage(message.getSerializedMessage());
		
		Message messageRecieved = null;
		while (MainPageController.getClient().getGameMessages().isEmpty()) {
			System.out.println("Stuck");
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
	 * Logs the user out 
	 * @param username the current user's username
	 * @precondition none
	 * @postcondition the user is logged out
	 */
	public void logout(String username) {
		var message = new Message("Logout---user:" + username + " ");
		MainPageController.getClient().sendMessage(message.getSerializedMessage());
		MainPageController.endListeningThread();
		
		
	}

	

}
