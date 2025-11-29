package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the program
 *
 * @author Java I
 */
public class App extends Application {

    private GameController gameController;
    private GameOverController gameOverController;
    private Stage primaryStage;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        loadCustomFonts();
        this.primaryStage = primaryStage;
        try {
            switchToMenu();
            //switchToGameOver(10);
            //switchToScoreboard();
            //primaryStage.resizableProperty().set(false);
            //primaryStage.setTitle("Java 1 - 8th laboratory");
            primaryStage.show();
            // Exit program when main window is closed
            primaryStage.setOnCloseRequest(this::exitProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop() throws Exception {
        gameController.stop();
        super.stop();
    }
    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
    public void switchToGame(GameScene loadedScene) throws IOException {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("gameWindow.fxml"));
        Parent root = gameLoader.load();

        gameController = gameLoader.getController();
        gameController.setApp(this);

        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);

        // ZMĚNA: Rozhodování, zda startujeme novou hru, nebo načítáme uloženou
        if (loadedScene != null) {
            // Máme uloženou hru -> předáme ji
            gameController.startGame(loadedScene);
        } else {
            // Nemáme uloženou hru (loadedScene je null) -> vytvoříme novou
            // Pozor: Zde musíš zadat rozměry plátna (např. podle velikosti okna)
            gameController.startGame(new GameScene(800, 600));
        }
    }

    public void switchToMenu() throws IOException {
        // Construct a main window with a canvas.
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("gameMainMenu.fxml"));
        Parent root = menuLoader.load();
        MainMenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
    }
    public void switchToGameOver(int score) throws IOException {
        FXMLLoader gameOverLoader = new FXMLLoader(getClass().getResource("gameOver.fxml"));
        Parent root = gameOverLoader.load();
        gameOverController = gameOverLoader.getController();
        gameOverController.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        gameOverController.setFinalScore(score);
    }

    public void switchToScoreboard() throws IOException {
        FXMLLoader scoreboardLoader = new FXMLLoader(getClass().getResource("scoreboard.fxml"));
        Parent root = scoreboardLoader.load();
        ScoreboardController scoreboardController = scoreboardLoader.getController();
        scoreboardController.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        scoreboardController.loadScores();
    }

    private void loadCustomFonts()
    {
        Font.loadFont(getClass().getResourceAsStream("ARCADE_FULL.ttf"), 43);
    }
}
