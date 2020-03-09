package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class responsible for keeping track of the game
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class GameLogic {
	private static volatile HashMap<String, Boolean> players;
	private char[] wordToGuess;
	private char[] correctLettersSoFar;
	private ArrayList<String> lettersGuessedSoFar;
	private int guesses;

	/**
	 * A new game logic object is created with the given word to guess
	 * @precondition none
	 * @postcondition the game logic object is created 
	 * @param wordToGuess the word to be guessed
	 */
	public GameLogic(char[] wordToGuess) {
		players = new HashMap<String, Boolean>();
		this.guesses = 5;
		this.wordToGuess = wordToGuess;
		this.lettersGuessedSoFar = new ArrayList<String>();
		this.correctLettersSoFar = new char[this.wordToGuess.length];

		for (int i = 0; i < this.wordToGuess.length; i++) {
			this.correctLettersSoFar[i] = '_';
		}
	}

	/**
	 * Check if the given char is in the word
	 * @param guess the letter being guessed
	 * @return If the word contains the char then the word with '_' for non guessed words is returned, otherwise 
	 * a message stating the guess is incorrect
	 */
	public String makeGuess(char guess) {
		if (this.checkIfLetterWasAlreadyGuessed(guess)) {
			return "You already guessed " + guess;
		}
		var correct = false;
		for (int i = 0; i < this.wordToGuess.length; i++) {
			if (this.wordToGuess[i] == guess) {
				correct = true;
				this.correctLettersSoFar[i] = guess;
			}
		}
		this.lettersGuessedSoFar.add(String.valueOf(guess));
		if (correct) {
			return new String(this.correctLettersSoFar);
		} else {
			this.guesses -= 1;
			return "Uh-oh, you guessed wrong. You have " + (this.guesses) + " guess(es) left";
		}
	}

	private boolean checkIfLetterWasAlreadyGuessed(char letter) {
		for (var current : this.lettersGuessedSoFar) {
			if (current.equals(String.valueOf(letter))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a player to the game
	 * @param username the username of the player being added
	 * @return true if the username was not already in use
	 */
	public boolean addPlayer(String username) {
		if (!players.containsKey(username)) {
			players.put(username, true);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes the given username from the game
	 * @param username the username being removed
	 * @precondition none
	 * @postcondition the username is removed
	 */
	public void removePlayer(String username) {
		players.remove(username);
	}
	

}
