package lab;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ScoreboardController {

    private App app;

    @FXML
    private TableView<Score> scoreTable;

    @FXML
    private TableColumn<Score, String> nickColumn;

    @FXML
    private TableColumn<Score, Integer> scoreColumn;

    public void setApp(App app) {
        this.app = app;
        loadScores();
    }

    public void loadScores() {
        try {
            scoreTable.setItems(FXCollections.observableArrayList(ScoreManager.load()));

            scoreTable.getSortOrder().clear();
            scoreTable.getSortOrder().add(scoreColumn);
            scoreColumn.setSortType(TableColumn.SortType.DESCENDING);
            scoreTable.sort();
        } catch (ScoreException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onBackToMenu() {
        try {
            app.switchToMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        nickColumn.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        scoreTable.setRowFactory(tv -> new TableRow<Score>() {
            @Override
            protected void updateItem(Score item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle(""); // Reset style
                } else {
                    int index = getIndex();
                    switch (index) {
                        case 0 -> setStyle("-fx-background-color: rgba(255, 255, 0, 0.5); -fx-font-weight: bold;");
                        case 1 -> setStyle("-fx-background-color: rgba(192, 192, 192, 0.5); -fx-font-weight: bold;");
                        case 2 -> setStyle("-fx-background-color: rgba(205, 133, 63, 0.5); -fx-font-weight: bold;");
                        default -> setStyle("");
                    }
                }
            }
        });
    }
}
