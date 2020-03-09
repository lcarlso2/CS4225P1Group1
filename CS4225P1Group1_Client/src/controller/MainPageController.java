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
	 * Ends the listening thread
	 * 
	 * @precondition none
	 * @postcondition the listening thread is terminated
	 */
	public static void endListeningThread() {
		try {
			client.setKeepListening(false);
			messageInterpreter.setTerminate(true);
			messageInterpreter.setRunIdle(false);
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
		var message = new Message("Login---user:" + username + " pass:" + password);
		client.sendMessage(message.getSerializedMessage());

		Message messageRecieved = null;

		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			messageRecieved = client.getLoginMessages().remove();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

		if (messageRecieved != null) {
			if (messageRecieved.getMessage().equals("valid")) {
				messageInterpreter.setRunIdle(true);
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
