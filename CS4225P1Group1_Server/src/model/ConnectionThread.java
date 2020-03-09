package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.Main;
import io.FileReader;

/**
 * The connection thread class responsible for keeping track of the connection
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 */
public class ConnectionThread implements Runnable {
	private Socket clientSocket;
	private ObjectInputStream incomingMessages;
	private ObjectOutputStream outgoingMessages;

	/**
	 * Creates a new connection thread object with the given socket
	 * 
	 * @param clientSocket the socket being used to create the connection
	 * @precondition none
	 * @postcondition the connection thread is created
	 */
	public ConnectionThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.incomingMessages = null;
		this.outgoingMessages = null;
	}

	private String login(Message message) {
		var messageSplitUserData = message.getMessage().split("---")[1];
		var username = this.getUserName(message);
		var password = messageSplitUserData.split(" ")[1].split(":")[1];
		var reader = new FileReader();
		var users = reader.getUsers("src/data/users.txt");
		if (users.validateCredentials(username, password)) {
			if (Main.GAME.addPlayer(username)) {
				Server.getClients().add(this);
				return "valid";
			} else {
				return "Error. Player already signed in.";
			}
		} else {
			return "invalid";
		}
	}

	private String handleGuess(char guess) {
		return Main.GAME.makeGuess(guess);
	}

	/**
	 * Sends the given message back 
	 * @param result the message being sent back
	 * @throws IOException the exception thrown
	 * @precondition none
	 * @postcondition the message is sent back
	 */
	public void sendMessageBack(String result) throws IOException {
		var returnMessage = new Message(result);
		this.outgoingMessages.writeObject(returnMessage.getSerializedMessage());
	}

	/**
	 * Closes the connections
	 * @throws IOException the exception thrown
	 * @precondition none
	 * @postcondition the connection is closed
	 */
	public void close() throws IOException {
		this.clientSocket.close();
		this.incomingMessages.close();
		this.outgoingMessages.close();
	}

	@Override
	public void run() {
		try {
			this.incomingMessages = new ObjectInputStream(this.clientSocket.getInputStream());
			this.outgoingMessages = new ObjectOutputStream(this.clientSocket.getOutputStream());

			while (true) {
				byte[] messageSerialized = (byte[]) this.incomingMessages.readObject();

				Message incomingMessage = Message.getUnserializedMessage(messageSerialized);

				var messageType = incomingMessage.getMessage().split("---")[0];
				if (messageType.equals("Login")) {
					this.handleLogin(incomingMessage);
				} else if (messageType.equals("Guess")) {
					var guess = incomingMessage.getMessage().split("---")[1];
					var result = this.handleGuess(guess.toCharArray()[0]);
					this.sendMessageBack("guess---" + result);
				} else if (messageType.equals("Logout")) {
					this.logout(incomingMessage);
				}
			}
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found:  " + e);
		}

	}

	private void handleLogin(Message incomingMessage) throws IOException {
		var result = this.login(incomingMessage);
		this.sendMessageBack("login---" + result);
		if (result.equals("valid")) {
			Server.sendAll("server---" + this.getUserName(incomingMessage) + " has joined the game.",
					this);
		}
	}

	private void logout(Message message) {
		var username = this.getUserName(message);
		Main.GAME.removePlayer(username);
		Server.sendAll("server---" + username + " left game", this);
	}

	private String getUserName(Message input) {
		var messageSplitUserData = input.getMessage().split("---")[1];
		var username = messageSplitUserData.split(" ")[0].split(":")[1];
		return username;
	}
}
