package lab;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MainMenuController {

    @FXML
    private void startGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"));
            Parent gameRoot = loader.load();

            GameController gameController = loader.getController();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            gameController.setStage(stage);

            Scene gameScene = new Scene(gameRoot);
            stage.setScene(gameScene);
            stage.setTitle("Asteroids");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
