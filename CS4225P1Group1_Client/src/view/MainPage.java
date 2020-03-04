package view;


import java.io.IOException;

import application.Main;
import controller.MainPageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPage {
	
	private MainPageController controller;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text usernameText;

    @FXML
    private Text passwordText;

    @FXML
    private Button loginButton;
    
    @FXML
    private Label errorMessageLabel;
    
    @FXML 
    void handleLoginClicked(MouseEvent event) throws IOException {
    	if (this.controller.validateLoginCredentials(this.usernameTextField.getText(), this.passwordTextField.getText())) {
    		this.handleMouseClickToNavigateToDifferentPage(event, Main.GAME_PAGE_VIEW);
    	} else {
    		this.errorMessageLabel.setVisible(true);
    	}
    	
    }
    
    /**
     * Handles the click event for when the user 
     * selects a button that will navigate to a new page 
     * 
     * @precondition none
     * @postcondition the desired page is navigated to 
     * @param event the event
     * @param page the new page being navigated to 
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
	 * Handles the click event for when the user clicks the user name or password text field
	 * 
	 * @precondition none
	 * @postcondition the error message is hidden
	 * @param event the event
	 * @throws IOException
	 */
	@FXML
	void handleClick(MouseEvent event) throws IOException {
		this.errorMessageLabel.setVisible(false);
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
	
	/**
     * Initializes the instance of the log in page
     * @precondition none
     * @postcondition the page is initialized 
     */
    @FXML
    void initialize() {
    	this.controller = new MainPageController();
    	this.errorMessageLabel.setVisible(false);
    }
	

}