package controller;

import model.Client;
import model.Message;

public class MainPageController {
	public static final String HOST = "localhost";
	public static final int PORT = 4225;
	private Client client;
	
	public MainPageController() {
		this.client = new Client(HOST, PORT);
		this.client.openPort();
	}
	
	public boolean validateLoginCredentials(String username, String password) {
		if (password.isEmpty()) {
			password = " ";
		}
		var message = new Message("Login---user:" + username + " pass:" + password);
		this.client.sendMessage(message.getSerializedMessage());
		
		var messageRecieved = this.client.receiveMessage();
		
		return messageRecieved.getMessage().equals("valid");
	}

}
