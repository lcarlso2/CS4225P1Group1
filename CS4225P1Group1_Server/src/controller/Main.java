package controller;

import model.GameLogic;
import model.Server;

/**
 * The main entry point for the program 
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class Main {
	public static final  GameLogic GAME = new GameLogic("fantastic".toCharArray());
	private static final int PORT = 4225; 

	/**
	 * Starts the program
	 * @param args the arguments 
	 * @precondition none
	 * @postcondition the program is run
	 */
	public static void main(String[] args) {
		Server server = new Server(PORT);
		server.openPort();
		
	}

}
