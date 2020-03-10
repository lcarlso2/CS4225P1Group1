package model;

import java.io.IOException;

public class TimeThread implements Runnable {
	
	public boolean timesUp = false;
	
	public boolean terminate = false;

	@Override
	public void run() {
		while (System.nanoTime() < Server.timeLeftToGuess && !this.terminate) {
			
		}
		if (this.terminate) {
			System.out.println("TERMINATED 1 in thread " + Thread.currentThread().getName());
			return;
		}
		System.out.println("outside TERMINATED 1 in thread " + Thread.currentThread().getName());
		
		var currentUserTurn = "";

		for (var current : Server.users.keySet()) {
			if (Server.users.get(current)) {
				currentUserTurn = current;
			}
		}

		try {
			Server.userConnections.get(currentUserTurn)
					.sendMessageBack("Take your turn!" + System.lineSeparator() + "Five seconds left");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Server.timeLeftToGuess = System.nanoTime() + 5_000_000_000L;
		while (System.nanoTime() < Server.timeLeftToGuess && !this.terminate) {

		}
		if (this.terminate) {
			System.out.println("TERMINATED 2 in thread " + Thread.currentThread().getName());
			return;
		}
		System.out.println("outside TERMINATED 2 in thread " + Thread.currentThread().getName());

		try {
			Server.userConnections.get(currentUserTurn).sendMessageBack("Times up!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.timesUp = true;

	}

}
