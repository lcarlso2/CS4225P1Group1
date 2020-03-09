package model;

import controller.GamePageController;
import controller.MainPageController;
import javafx.application.Platform;

/**
 * The class responsible for interpreting message from the server
 * 
 * @author Tyler Scott, Dexter Tarver, and Lucas Carlson
 *
 */
public class MessageInterpreterThread implements Runnable {

	private boolean runIdle = true;

	private boolean terminate = false;

	/**
	 * Sets the run idle value
	 * 
	 * @param newValue the new value
	 * @precondition none
	 * @postcondition runIdle = newValue
	 */
	public void setRunIdle(boolean newValue) {
		this.runIdle = newValue;
	}

	/**
	 * Sets terminate value
	 * 
	 * @param newValue the new value
	 * @precondition none
	 * @postcondition terminate = newValue
	 */
	public void setTerminate(boolean newValue) {
		this.terminate = newValue;
	}

	@Override
	public void run() {
		while (!this.terminate) {
			while (this.runIdle) {
				try {
					var message = MainPageController.getClient().getMiscMessages().remove();
					System.out.println("INSIDE RUN IDLE OF INTERPRETER: " + message.getMessage());
					if (message.getMessage().contains("&")) {
						Platform.runLater(() -> GamePageController.setWordBeingGuessed(message.getMessage().split("&")[1]));
					}
					Platform.runLater(() -> GamePageController.setServerResponse(message.getMessage().split("##")[0]));
					
					
				} catch (Exception ex) {

				}
			}
		}

	}

}
