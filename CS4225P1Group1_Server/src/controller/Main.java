package controller;

import io.FileReader;
import model.GameLogic;
import model.Server;

/**
 * The main entry point for the program 
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class Main {
	
	public static final  FileReader READER = new FileReader();
	public static final String GUESSWORD = READER.loadGuessWord("src/data/dictionary.txt");
	public static final  GameLogic GAME = new GameLogic(GUESSWORD.toCharArray());
	private static final int PORT = 4225; 

	/**
	 * Starts the program
	 * @param args the arguments 
	 * @precondition none
	 * @postcondition the program is run
	 */
	public static void main(String[] args) {
		var reader = new FileReader();
		System.out.println("game word " + GUESSWORD);
		Server server = new Server(PORT);
		server.openPort();
		
	}

}
