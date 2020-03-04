package controller;

import model.Message;

public class GamePageController {
	
	public String makeGuess(String letterToGuess) {
		var message = new Message("Guess---" + letterToGuess);
		MainPageController.client.sendMessage(message.getSerializedMessage());
		
		var unserializedMessage = MainPageController.client.receiveMessage();
		return unserializedMessage.getMessage();
	}
	
	public boolean checkIfGuessWasMade(String message) {
		return message.startsWith("You already guessed ");
	}
	

}
