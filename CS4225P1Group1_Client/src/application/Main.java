package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * The main entry point for the program
 * 
 * @author Lucas Carlson, Dexter Tarver, and Tyler Scott
 *
 */
public class Main extends Application {
	
	public static final String MAIN_PAGE_TITLE = "Play Hangman!";
	public static final String MAIN_PAGE_VIEW = "/view/MainPage.fxml";
	public static final String GAME_PAGE_VIEW = "/view/GamePage.fxml";
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(); 
			loader.setLocation(Main.class.getResource(MAIN_PAGE_VIEW));
			loader.load();
			Scene scene = new Scene(loader.getRoot());
			primaryStage.setTitle(MAIN_PAGE_TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches the program
	 * @param args the arguments
	 * @precondition none
	 * @postcondition the program is launched
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
