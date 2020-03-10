package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import controller.Main;
import io.FileReader;

/**
 * The connection thread class responsible for keeping track of the connection
 * 
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 */
public class ConnectionThread implements Runnable {
	private Socket clientSocket;
	private ObjectInputStream incomingMessages;
	private ObjectOutputStream outgoingMessages;
	private static volatile TurnThread timer;
	private static Thread[] timerPool = new Thread[1];

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

	/**
	 * Sends the given message back
	 * 
	 * @param result the message being sent back
	 * @throws IOException the exception thrown
	 * @precondition none
	 * @postcondition the message is sent back
	 */
	public void sendMessageBack(String result) throws IOException {
		var returnMessage = new Message(result);
		this.outgoingMessages.writeObject(returnMessage.getSerializedMessage());
		if (result.contains("Times up")) {
			timer.setTerminate(true);
			var currentPlayer = this.getCurrentPlayer();
			this.removePlayerFromGame(currentPlayer);
		}
	}

	/**
	 * Closes the connections
	 * 
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
				if (messageType.equals("LOGIN")) {
					this.handleLogin(incomingMessage);
				} else if (messageType.equals("GUESS")) {
					this.handleGuessRequestFromClient(incomingMessage);
				} else if (messageType.equals("QUIT")) {
					this.logout(incomingMessage);
				}
			}
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found:  " + e);
		}

	}

	private void handleGuessRequestFromClient(Message incomingMessage) throws IOException {
		timer.setTerminate(true);
		this.setToNextPlayersTurn();
		var guess = incomingMessage.getMessage().split("---")[1].split(":")[1];
		var username = incomingMessage.getMessage().split("---")[1].split(":")[0];
		var result = this.handleGuess(guess.toCharArray()[0]);
		this.sendMessageBack("GUESS---" + result);
		Server.sendAll(username + " just guessed: " + guess + System.lineSeparator() + "Guesses left "
				+ Main.GAME.getGuessesLeft() + "##word&" + Main.GAME.getCorrectLettersSoFar(), this);
		if (!result.contains("You won") && !result.contains("Game over")) {
			this.startPlayersTurn();
		}
	}

	private void startPlayersTurn() {
		timer = new TurnThread();
		Server.setTimeLeftToGuess(System.nanoTime() + 10_000_000_000L);
		timerPool[0] = new Thread(timer);
		timerPool[0].start();
	}

	private void setToNextPlayersTurn() {
		if (Server.getUsers().size() == 1) {
			Map.Entry<String, Boolean> entry = Server.getUsers().entrySet().iterator().next();
			Server.getUsers().put(entry.getKey(), true);
		} else {
			var nextPlayer = "";
			for (var current : Server.getUsers().keySet()) {
				if (!Server.getUsers().get(current)) {
					nextPlayer = current;
				}
				Server.getUsers().put(current, false);
			}
			if (!nextPlayer.isEmpty()) {
				Server.getUsers().put(nextPlayer, true);
			}
		}
	}

	private String getCurrentPlayer() {
		var currentPlayer = "";
		for (var current : Server.getUsers().keySet()) {
			if (Server.getUsers().get(current)) {
				currentPlayer = current;
			}
		}
		return currentPlayer;
	}

	private String handleGuess(char guess) {
		if (Main.GAME.checkIfLetterWasAlreadyGuessed(guess)) {
			return "You already guessed " + guess;
		}
		var wasGuessRight = Main.GAME.makeGuess(guess);

		if (Main.GAME.getGuessesLeft() == 0) {
			timer.setTerminate(true);
			return "Guesses all used. Game over.";
		}

		if (Main.GAME.checkIfGameIsOver()) {
			timer.setTerminate(true);
			return "You won!!!!:" + Main.GAME.getCorrectLettersSoFar();
		}

		if (wasGuessRight) {
			return new String(Main.GAME.getCorrectLettersSoFar());
		} else {
			return "Uh-oh, you guessed wrong." + System.lineSeparator() + "You have " + (Main.GAME.getGuessesLeft())
					+ " guess(es) left";
		}
	}

	private void handleLogin(Message incomingMessage) throws IOException {
		var result = this.login(incomingMessage);
		this.sendMessageBack("LOGIN---" + result);
		if (result.startsWith("valid")) {
			Server.sendAll("server---" + this.getUserName(incomingMessage) + " has joined the game.", this);
			if (!timer.getRunning()) {
				this.startPlayersTurn();
			}
		}
	}

	private String login(Message message) {
		if (GameLogic.getPlayers().size() == GameLogic.MAX_PLAYERS) {
			return "Game room full-" + System.lineSeparator() + "please try again later.";
		}
		var messageSplitUserData = message.getMessage().split("---")[1];
		var username = this.getUserName(message);
		var password = messageSplitUserData.split(" ")[1].split(":")[1];
		var reader = new FileReader();
		var users = reader.getUsers("src/data/users.txt");
		return this.validateLogin(username, password, users);
	}

	private String validateLogin(String username, String password, Users users) {
		if (users.validateCredentials(username, password)) {
			return this.addPlayerToGame(username);
		} else {
			return "invalid";
		}
	}

	private String addPlayerToGame(String username) {
		if (Main.GAME.addPlayer(username)) {
			Server.getUserConnections().put(username, this);
			if (Server.getUsers().size() == 0) {
				timer = new TurnThread();
				Server.getUsers().put(username, true);
			} else {
				Server.getUsers().put(username, false);
			}
			return "valid****word:" + Main.GAME.getCorrectLettersSoFar();
		} else {
			return "Error" + System.lineSeparator() + "Player already playing";
		}
	}

	private void logout(Message message) {
		var username = this.getUserName(message);
		this.removePlayerFromGame(username);
	}

	private void removePlayerFromGame(String username) {
		Server.getUsers().remove(username);
		Server.getUserConnections().remove(username);
		Main.GAME.removePlayer(username);
		Server.sendAll("server---" + username + " left game", this);
		try {
			this.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setToNextPlayersTurn();
		this.startPlayersTurn();
	}

	private String getUserName(Message input) {
		var messageSplitUserData = input.getMessage().split("---")[1];
		var username = messageSplitUserData.split(" ")[0].split(":")[1];
		return username;
	}
}
