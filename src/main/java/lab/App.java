package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the program
 *
 * @author Java I
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lab/gameMainMenu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            String css = this.getClass().getResource("/lab/application.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Main Menu");
            primaryStage.show();

            primaryStage.setOnCloseRequest(this::exitProgram);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
