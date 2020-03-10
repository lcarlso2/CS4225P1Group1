package controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import model.Client;
import model.Message;
import model.MessageInterpreterThread;

/**
 * The main page controller
 * 
 * @author Lucas Carlson, Dexter Tarver, Tyler Scott
 *
 */
public class MainPageController {
	public static final String HOST = "localhost";
	public static final int PORT = 4225;
	private static Client client;
	private static MessageInterpreterThread messageInterpreter;
	private static Thread[] pool = new Thread[2];
	private static String wordToGuess;
	
	private static String currentUserName;
	
	/**
	 * Gets the current users user name
	 * @return the user name
	 */
	public static String getCurrentUserName() {
		return currentUserName;
	}


	/**
	 * Gets the client
	 * 
	 * @return the client
	 * @precondition none
	 * @postcondition none
	 */
	public static Client getClient() {
		return client;
	}
	
	/**
	 * Gets the word to be guessed
	 * @precondition none
	 * @postcondition none
	 * @return the word to be guessed
	 */
	public static String getWordToGuess() {
		return wordToGuess;
	}

	/**
	 * Ends the thread
	 * 
	 * @precondition none
	 * @postcondition the threads are terminated
	 */
	public static void terminateThreads() {
		try {
			client.setKeepListening(false);
			messageInterpreter.setTerminate(true);
			client.close();
			pool[0].join();
			pool[1].join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * The method for validating the log in credentials. This method also
	 * establishes the connection with the server
	 * 
	 * @precondition none
	 * @postcondition the user is logged in or an appropriate error message is
	 *                displayed
	 * @param username the user name
	 * @param password the password
	 * @return The response from the server
	 */
	public String validateLoginCredentials(String username, String password) {
		this.initilizeThreads();
		if (password.isEmpty()) {
			password = " ";
		}
		var message = new Message("LOGIN---user:" + username + " pass:" + password);
		client.sendMessage(message.getSerializedMessage());

		Message messageRecieved = null;

		
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			messageRecieved = client.getLoginMessages().remove();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		if (messageRecieved != null) {
			if (messageRecieved.getMessage().startsWith("valid")) {
				currentUserName = username;
				wordToGuess = messageRecieved.getMessage().split(":")[1];
			}
			return messageRecieved.getMessage();
		} else {
			return "Error: Something went wrong";
		}

	}

	private void initilizeThreads() {
		client = new Client(HOST, PORT);
		client.openPort();
		messageInterpreter = new MessageInterpreterThread();
		pool[0] = new Thread(client);
		pool[1] = new Thread(messageInterpreter);
		pool[0].start();
		pool[1].start();

	}

}
