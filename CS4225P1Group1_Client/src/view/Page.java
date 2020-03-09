package view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The base page class
 * @author Lucas Carlson, Tyler Scott, Dexter Tarver
 *
 */
public class Page {
	

	/**
	 * Handles the click event for when the user selects a button that will navigate
	 * to a new page
	 * 
	 * @precondition none
	 * @postcondition the desired page is navigated to
	 * @param event the event
	 * @param page  the new page being navigated to
	 * @throws IOException an exception
	 */
	public void handleMouseClickToNavigateToDifferentPage(MouseEvent event, String page) throws IOException {
		Stage currentStage = this.getStage(event);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(page));
		loader.load();
		Scene sceneToNavigateTo = new Scene(loader.getRoot());
		currentStage.setScene(sceneToNavigateTo);
	}
	
	/**
	 * Gets the stage from the specific event
	 * 
	 * @precondition none
	 * @param event the event fired
	 * @return the desired stage
	 */
	public Stage getStage(MouseEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}


}
