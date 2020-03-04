package controller;

import model.Client;
import model.Message;

public class MainPageController {
	public static final String HOST = "localhost";
	public static final int PORT = 4225;
	public static Client client;
	
	public MainPageController() {
		client = new Client(HOST, PORT);
		client.openPort();
	}
	
	public boolean validateLoginCredentials(String username, String password) {
		if (password.isEmpty()) {
			password = " ";
		}
		var message = new Message("Login---user:" + username + " pass:" + password);
		client.sendMessage(message.getSerializedMessage());
		
		var messageRecieved = client.receiveMessage();
		
		System.out.println("MESSAGE: " + messageRecieved.getMessage());
		return messageRecieved.getMessage().equals("valid");
	}

}
