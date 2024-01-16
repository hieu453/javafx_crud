package gui;
	
import core.BookDAO;
import core.HumanDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginGUI extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			new BookDAO("jdbc:ucanaccess://lib/QLS.accdb", "", "");
			new HumanDAO("jdbc:ucanaccess://lib/QLS.accdb", "", "");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
			Parent root = loader.load();
			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(false);
			primaryStage.setTitle("Đăng nhập");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
