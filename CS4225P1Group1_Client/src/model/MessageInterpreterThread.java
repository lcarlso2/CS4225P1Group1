package model;

import controller.GamePageController;
import controller.MainPageController;
import javafx.application.Platform;

/**
 * The class responsible for interpreting message from the server
 * 
 * @author Tyler Scott, Dexter Tarver, and Lucas Carlson
 *
 */
public class MessageInterpreterThread implements Runnable {

	private boolean terminate = false;

	/**
	 * Sets terminate value
	 * 
	 * @param newValue the new value
	 * @precondition none
	 * @postcondition terminate = newValue
	 */
	public void setTerminate(boolean newValue) {
		this.terminate = newValue;
	}

	@Override
	public void run() {
		while (!this.terminate) {
			try {
				var message = MainPageController.getClient().getMiscMessages().remove();
				var serverResponse = message.getMessage().split("##")[0];
				var endGameMessage = this.handleGuessMadeByOtherPlayer(message, serverResponse);
				if (!endGameMessage.isEmpty()) {
					Platform.runLater(() -> GamePageController.setServerResponse(endGameMessage));
				} else if (message.getMessage().contains("Your turn")) {
					Platform.runLater(() -> GamePageController.enableGuessButton());
					Platform.runLater(() -> GamePageController.setServerResponse(serverResponse));
				} else if (message.getMessage().contains("Times up")) {
					Platform.runLater(() -> GamePageController.disableGuessButton());
					Platform.runLater(() -> GamePageController.setServerResponse(serverResponse));
				} else {
					Platform.runLater(() -> GamePageController.setServerResponse(serverResponse));
				}
			} catch (Exception ex) {

			}
		}
	}

	private String handleGuessMadeByOtherPlayer(Message message, String serverResponse) {
		var endGameMessage = "";
		if (message.getMessage().contains("&")) {
			Platform.runLater(() -> GamePageController.enableGuessButton());
			var wordBeingGuessed = message.getMessage().split("&")[1];
			if (!wordBeingGuessed.contains("_")) {
				endGameMessage = serverResponse.split(" ")[0] + " won!";
				Platform.runLater(() -> GamePageController.disableGuessButton());
			}
			Platform.runLater(() -> GamePageController.setWordBeingGuessed(wordBeingGuessed));
		}
		return endGameMessage;
	}

}
