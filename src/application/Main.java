package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	public static int choixTypeMC ;
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Mots Croisés Djaa & Tchimou");
			FXMLLoader loader = new FXMLLoader() ;
            loader.setLocation(Main.class.getResource("VueAccueilTP6.fxml"));
            Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();						
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
