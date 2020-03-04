package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
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
	 * @param inputFileName the input file being used for the data 
	 */
	public Client(String connectionHost, int connectionPort) {
		host = connectionHost;
		port = connectionPort;
		this.clientSocket = null;
		this.incomingMessage = null;
		this.outgoingMessages = null;
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
	
	public Message receiveMessage() {
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
}
