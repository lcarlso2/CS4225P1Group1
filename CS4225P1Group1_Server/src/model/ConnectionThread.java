package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;

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
	public TimeThread timer;

	/**
	 * Creates a new connection thread object with the given socket
	 * 
	 * @param clientSocket the socket being used to create the connection
	 * @precondition none
	 * @postcondition the connection thread is created
	 */
	public ConnectionThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.timer = new TimeThread();
		this.incomingMessages = null;
		this.outgoingMessages = null;
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
			Server.getClients().add(this);
			if (Server.users.size() == 0) {

				Server.users.put(username, true);
				Server.userConnections.put(username, this);
				this.startPlayersTurn();
			} else {
				Server.users.put(username, false);
				Server.userConnections.put(username, this);
			}
			return "valid****word:" + Main.GAME.getCorrectLettersSoFar();
		} else {
			return "Error" + System.lineSeparator() + "Player already playing";
		}
	}

	private String handleGuess(char guess) {
		if (Main.GAME.checkIfLetterWasAlreadyGuessed(guess)) {
			return "You already guessed " + guess;
		}

		var wasGuessRight = Main.GAME.makeGuess(guess);

		if (Main.GAME.getGuessesLeft() == 0) {
			return "Guesses all used. Game over.";
		}

		if (Main.GAME.checkIfGameIsOver()) {
			return "You won!!!!:" + Main.GAME.getCorrectLettersSoFar();
		}

		if (wasGuessRight) {
			return new String(Main.GAME.getCorrectLettersSoFar());
		} else {
			return "Uh-oh, you guessed wrong." + System.lineSeparator() + "You have " + (Main.GAME.getGuessesLeft())
					+ " guess(es) left";
		}
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
				if (this.timer.timesUp) {
					System.out.println("--------------TIMES UP------------");
					this.setToNextPlayersTurn();
					this.startPlayersTurn();
				}
				byte[] messageSerialized = (byte[]) this.incomingMessages.readObject();

				Message incomingMessage = Message.getUnserializedMessage(messageSerialized);

				var messageType = incomingMessage.getMessage().split("---")[0];
				if (messageType.equals("LOGIN")) {
					this.handleLogin(incomingMessage);
				} else if (messageType.equals("GUESS")) {
					this.timer.terminate = true;
					this.setToNextPlayersTurn();
					var guess = incomingMessage.getMessage().split("---")[1].split(":")[1];
					var username = incomingMessage.getMessage().split("---")[1].split(":")[0];
					var result = this.handleGuess(guess.toCharArray()[0]);
					this.sendMessageBack("GUESS---" + result);
					Server.sendAll(
							username + " just guessed: " + guess + System.lineSeparator() + "Guesses left "
									+ Main.GAME.getGuessesLeft() + "##word&" + Main.GAME.getCorrectLettersSoFar(),
							this);
					this.startPlayersTurn();
				} else if (messageType.equals("QUIT")) {
					this.logout(incomingMessage);
					this.close();
					Server.getClients().remove(this);
				}
			}
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found:  " + e);
		}

	}

	private void startPlayersTurn() {
		this.timer = new TimeThread();
		Server.timeLeftToGuess = System.nanoTime() + 10_000_000_000L;
		Server.pool.execute(this.timer);
	}

	private void setToNextPlayersTurn() {
		var nextPlayer = "";
		for (var current : Server.users.keySet()) {
			if (!Server.users.get(current)) {
				nextPlayer = current;
			}
			Server.users.put(current, false);
		}

		Server.users.put(nextPlayer, true);
	}

	private void handleLogin(Message incomingMessage) throws IOException {
		var result = this.login(incomingMessage);
		this.sendMessageBack("LOGIN---" + result);
		if (result.startsWith("valid")) {
			Server.sendAll("server---" + this.getUserName(incomingMessage) + " has joined the game.", this);
		}
	}

	private void logout(Message message) {
		var username = this.getUserName(message);
		Server.users.remove(username);
		Server.userConnections.remove(username);
		Main.GAME.removePlayer(username);
		Server.sendAll("server---" + username + " left game", this);
	}

	private String getUserName(Message input) {
		var messageSplitUserData = input.getMessage().split("---")[1];
		var username = messageSplitUserData.split(" ")[0].split(":")[1];
		return username;
	}
}
