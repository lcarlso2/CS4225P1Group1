package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import io.FileReader;

public class Server {
	
	private static int port;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream incomingMessages;
	private ObjectOutputStream outgoingMessages;
	
	
	/**
	 * Creates a new Server with a specific port to use when it runs
	 * 
	 * @param connectionPort the port to use when the server runs
	 */
	public Server(int connectionPort) {
		port = connectionPort;
		this.serverSocket = null;
		this.clientSocket = null;
		this.incomingMessages = null;
		this.outgoingMessages = null;
	}

	/**
	 * Creates a server socket bound to the specified port
	 * 
	 * @precondition none
	 * @postcondition the received matrices are multiplied and sent back
	 */
	public void openPort() {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			System.err.println("IOException:  " + e + " -- most probably the server is already started.");
			System.exit(-1);
		}
		try {
			while (true) {
				this.clientSocket = this.serverSocket.accept();
				this.incomingMessages = new ObjectInputStream(this.clientSocket.getInputStream());
				this.outgoingMessages = new ObjectOutputStream(this.clientSocket.getOutputStream());

				byte[] messageSerialized = (byte[]) this.incomingMessages.readObject();
				
				Message incomingMessage = Message.getUnserializedMessage(messageSerialized);
				
				var messageType = incomingMessage.getMessage().split("---")[0];
				if (messageType.equals("Login")) {
					System.out.println("INSIDE LOGIN");
					var result = this.handleLogin(incomingMessage);
					this.sendLoginResultBack(result);
					
				}
			
			}
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found:  " + e);
		}
		try {
			this.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void sendResultBack(byte[] result) throws IOException {
		this.outgoingMessages.writeObject(result);
	}
	
	private String handleLogin(Message message) {
		var messageSplitUserData = message.getMessage().split("---")[1];
		var username = messageSplitUserData.split(" ")[0].split(":")[1];
		var password = messageSplitUserData.split(" ")[1].split(":")[1];
		var reader = new FileReader();
		var users = reader.GetUsers("src/data/users.txt");
		if  (users.ValidateCredentials(username, password)) {
			return "valid";
		} else {
			return "invalid";
		}
	}
	
	private void sendLoginResultBack(String result) throws IOException {
		var returnMessage = new Message(result);
		this.sendResultBack(returnMessage.getSerializedMessage());
	}
	

	private void close() throws IOException {
		this.serverSocket.close();
		this.clientSocket.close();
		this.incomingMessages.close();
		this.outgoingMessages.close();
	}

}
