package model;

import java.util.ArrayList;

public class GameLogic {
	private char[] wordToGuess;
	private char[] correctLettersSoFar;
	private ArrayList<String> lettersGuessedSoFar;
	private int guesses;

	public GameLogic(char[] wordToGuess) {
		this.guesses = 5;
		this.wordToGuess = wordToGuess;
		this.lettersGuessedSoFar = new ArrayList<String>();
		this.correctLettersSoFar = new char[this.wordToGuess.length];

		for (int i = 0; i < this.wordToGuess.length; i++) {
			this.correctLettersSoFar[i] = '_';
		}
	}

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

}
