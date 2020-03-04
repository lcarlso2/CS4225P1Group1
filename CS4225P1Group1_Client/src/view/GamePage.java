package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import model.Message;

public class GamePage {


    @FXML
    private TextArea outputTextArea;
    
    @FXML
    private Button sendButton;
    
    @FXML
    void handleSendClicked(MouseEvent event) throws IOException {
    	
    	Message message = new Message("Test Message");
    	var test = message.getSerializedMessage();
    	var message1 = message.getUnserializedMessage(test);
    	this.outputTextArea.setText(message1.getMessage());
    	
    }
    
}
