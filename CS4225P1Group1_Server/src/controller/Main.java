package controller;

import model.Server;

public class Main {
	private static final int PORT = 4225; 

	public static void main(String[] args) {
		Server server = new Server(PORT);
		server.openPort();

	}

}
