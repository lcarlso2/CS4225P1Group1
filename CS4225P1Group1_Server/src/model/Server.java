package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * The server class responsible for getting connecting to the client
 * 
 * @author Lucas Carlson, Tyler Scott, and Dexter Tarver
 * 
 *         References :
 *         https://stackoverflow.com/questions/13115784/sending-a-message-to-all-clients-client-server-communication
 *
 */
public class Server {
	private static int port;
	private ServerSocket serverSocket;
	private static ArrayList<ConnectionThread> clients;

	/**
	 * Creates a new Server with a specific port to use when it runs
	 * 
	 * @param connectionPort the port to use when the server runs
	 */
	public Server(int connectionPort) {
		port = connectionPort;
		clients = new ArrayList<ConnectionThread>();
		this.serverSocket = null;

	}
	
	/**
	 * Gets the clients 
	 * @return the clients
	 */
	public static ArrayList<ConnectionThread> getClients() {
		return clients;
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

			var pool = Executors.newCachedThreadPool();
			while (true) {
				var clientSocket = this.serverSocket.accept();
				var serverThread = new ConnectionThread(clientSocket);
				pool.execute(serverThread);
			}

		} catch (IOException ex) {

			ex.printStackTrace();
		}

		try {
			this.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void close() throws IOException {
		this.serverSocket.close();
	}

	/**
	 * Sends the given message to all clients other than the one calling the function
	 * 
	 * @param message the message 
	 * @param clientRequestOriginatedFrom the client the request originated from
	 * @precondition none
	 * @postcondition the message is sent
	 */
	public static void sendAll(String message, ConnectionThread clientRequestOriginatedFrom) {
		System.out.println("MESSAGE BEING SENT BACK BY SERVER: " + message);
		for (var current : clients) {
			if (!current.equals(clientRequestOriginatedFrom)) {
				try {
					current.sendMessageBack(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
