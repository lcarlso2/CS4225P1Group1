package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The client class for communicating with the server
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class Client implements Runnable {
	
	private boolean keepListening = true;
	private Queue<Message> loginMessages;
	private Queue<Message> gameMessages;
	private Queue<Message> miscMessages;
	private static String host;
	private static int port;

	private Socket clientSocket;
	private ObjectInputStream incomingMessage;
	private ObjectOutputStream outgoingMessages;

	

	/**
	 * Creates a new Client with the specified connect host and port to use
	 * 
	 * @param connectionHost the host connection
	 * @param connectionPort the port
	 */
	public Client(String connectionHost, int connectionPort) {
		host = connectionHost;
		port = connectionPort;
		this.loginMessages = new LinkedList<Message>();
		this.gameMessages = new LinkedList<Message>();
		this.miscMessages = new LinkedList<Message>();
		this.clientSocket = null;
		this.incomingMessage = null;
		this.outgoingMessages = null;
	}
	
	/**
	 * Sets the keep listening value
	 * @param newValue the new value
	 * @precondition none
	 * @postcondition keepListening = newValue
	 */
	public void setKeepListening(boolean newValue) {
		this.keepListening = newValue;
	}

	/**
	 * Gets the messages from the server 
	 * @return the messages from the server
	 * @precondition none
	 * @postcondition none
	 */
	public Queue<Message> getLoginMessages() {
		return this.loginMessages;
	}
	
	/**
	 * Gets the messages from the server 
	 * @return the messages from the server
	 * @precondition none
	 * @postcondition none
	 */
	public Queue<Message> getGameMessages() {
		return this.gameMessages;
	}
	
	/**
	 * Gets the messages from the server 
	 * @return the messages from the server
	 * @precondition none
	 * @postcondition none
	 */
	public Queue<Message> getMiscMessages() {
		return this.miscMessages;
	}
	/**
	 * Sends the given serialized message to the server
	 * 
	 * @precondition none
	 * @postcondition the response is received
	 * @param messageSerialized the serialized message being sent
	 */
	public void sendMessage(byte[] messageSerialized) {
		if (this.clientSocket != null && this.outgoingMessages != null && this.incomingMessage != null) {
			try {
				System.err.println("Session started...");
				this.outgoingMessages.writeObject(messageSerialized);
				System.err.println("Message sent...");

			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}

	private Message receiveMessage() {
		try {
			var serializedResults = (byte[]) this.incomingMessage.readObject();

			System.err.println("Serialized message received...");
			var result = Message.getUnserializedMessage(serializedResults);
			return result;
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found:  " + e);
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
		return null;
	}

	/**
	 * Creates a stream socket and connects to the specified port.
	 * 
	 * @precondition none
	 * @postcondition the port is opened
	 */
	public void openPort() {
		try {
			this.initialze();
		} catch (UnknownHostException e) {
			System.err.println("Problem with the host.");
		} catch (IOException e) {
			System.err.println("Unable to connect; very likely that the server was not started.");
		}
	}

	/**
	 * Closes the connection to the server 
	 * @throws IOException the exception
	 * @precondition none
	 * @postcondition the server is closed
	 */
	public void close() throws IOException {
		this.incomingMessage.close();
		this.outgoingMessages.close();
		this.clientSocket.close();
	}

	private void initialze() throws UnknownHostException, IOException {
		this.clientSocket = new Socket(host, port);
		this.outgoingMessages = new ObjectOutputStream(this.clientSocket.getOutputStream());
		this.incomingMessage = new ObjectInputStream(this.clientSocket.getInputStream());
	}

	@Override
	public void run() {
		while (this.keepListening) {
			var message = this.receiveMessage();
			System.out.println("Recieved Message: " + message.getMessage());
			if (message.getMessage().startsWith("LOGIN")) {
				message.stripMessageOfType();
				this.loginMessages.add(message);
			} else if (message.getMessage().startsWith("GUESS")) {
				message.stripMessageOfType();
				this.gameMessages.add(message);
			} else {
				this.miscMessages.add(message);
			}
		}

	}
}
