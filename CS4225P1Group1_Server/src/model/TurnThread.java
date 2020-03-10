package model;

import java.io.IOException;

/**
 * The turn thread class responsible for keeping track of whose turn it is
 * @author Lucas Carlson, Tyler Scott, and Dexter Tarver
 *
 */
public class TurnThread implements Runnable {

	private volatile boolean terminate = false;
	
	private boolean running = false;
	
	/**
	 * Gets the running value
	 * @precondition none
	 * @postcondition none
	 * @return the running value
	 */
	public boolean getRunning() {
		return this.running;
	}

	/**
	 * Sets the terminate value
	 * 
	 * @param newValue the new value
	 * @precondition none
	 * @postcondition terminate = newValue
	 */
	public void setTerminate(boolean newValue) {
		this.terminate = newValue;
	}
	

	/**
	 * Gets the terminate value
	 * 
	 * @precondition none
	 * @postcondition none
	 * @return the terminate value
	 */
	public boolean getTerminate() {
		return this.terminate;
	}

	@Override
	public void run() {
		this.running = true;
		var currentUserTurn = "";

		for (var current : Server.getUsers().keySet()) {
			if (Server.getUsers().get(current)) {
				currentUserTurn = current;
			}
		}

		this.sendNudge(currentUserTurn, "Your turn!");

		while (System.nanoTime() < Server.getTimeLeftToGuess() && !this.terminate) {

		}
		if (this.terminate) {
			this.running = false;
			return;
		} else {
			this.sendNudge(currentUserTurn, "Take your turn! Five seconds left");
		}

		Server.setTimeLeftToGuess(System.nanoTime() + 5_000_000_000L);
		while (System.nanoTime() < Server.getTimeLeftToGuess() && !this.terminate) {

		}
		if (this.terminate) {
			this.running = false;
			return;
		} else {
			this.sendNudge(currentUserTurn, "Times up!! You were removed from the game");
		}
		this.running = false;
	}

	private void sendNudge(String currentUserTurn, String message) {
		if (!currentUserTurn.isEmpty()) {
			try {
				Server.getUserConnections().get(currentUserTurn).sendMessageBack(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}