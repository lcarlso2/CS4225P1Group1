package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class responsible for keeping track of the game
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class GameLogic {
	
	public static final int MAX_PLAYERS = 2;
	private static volatile HashMap<String, Boolean> players;
	private char[] wordToGuess;
	private char[] correctLettersSoFar;
	private ArrayList<String> lettersUsedSoFar;
	private int guesses;

	/**
	 * A new game logic object is created with the given word to guess
	 * @precondition none
	 * @postcondition the game logic object is created 
	 * @param wordBeingGuessed the word to be guessed
	 */
	public GameLogic(char[] wordBeingGuessed) {
		players = new HashMap<String, Boolean>();
		this.guesses = 5;
		this.wordToGuess = wordBeingGuessed;
		this.lettersUsedSoFar = new ArrayList<String>();
		this.correctLettersSoFar = new char[this.wordToGuess.length];

		for (int i = 0; i < this.wordToGuess.length; i++) {
			this.correctLettersSoFar[i] = '_';
		}
	}
	
	/**
	 * Gets the word being guessed
	 * @precondition none
	 * @postcondition none
	 * @return the word being guessed
	 */
	public String getWordBeingGuessed() {
		return new String(this.wordToGuess);
	}
	
	/**
	 * Gets the guesses left
	 * @return the guesses left
	 */
	public int getGuessesLeft() {
		return this.guesses;
	}
	
	/**
	 * Gets players
	 * @return the players
	 */
	public static HashMap<String, Boolean> getPlayers() {
		return players;
	}
	
	/**
	 * Checks if all the letters have been guessed. 
	 * @return Returns true if they have otherwise false
	 */
	public boolean checkIfGameIsOver() {
		for (var current : this.correctLettersSoFar) {
			if (current == '_') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets correct Letters So Far
	 * @precondition none
	 * @postcondition none
	 * @return correct Letters So Far
	 */
	public String getCorrectLettersSoFar() {
		return new String(this.correctLettersSoFar);
	}
	
	/**
	 * Gets letters used so far
	 * @precondition none
	 * @postcondition none
	 * @return the letters used to far
	 */
	public ArrayList<String> getLettersUsedSoFar() {
		return this.lettersUsedSoFar;
	}

	/**
	 * Check if the given char is in the word
	 * @param guess the letter being guessed
	 * @return true if the word was in the word
	 */
	public boolean makeGuess(char guess) {
		var wasGuessRight = false;
		for (int i = 0; i < this.wordToGuess.length; i++) {
			if (this.wordToGuess[i] == guess) {
				wasGuessRight = true;
				this.correctLettersSoFar[i] = guess;
			}
		}
		this.lettersUsedSoFar.add(String.valueOf(guess));
		
		if (!wasGuessRight) {
			this.guesses -= 1;
		}
		
		return wasGuessRight;
	}

	/**
	 * Checks if the letter being guessed was already guessed
	 * @param letter the letter being checked
	 * 
	 * @return true if it was guessed, otherwise false
	 */
	public boolean checkIfLetterWasAlreadyGuessed(char letter) {
		for (var current : this.lettersUsedSoFar) {
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
