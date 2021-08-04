
package coursework.threaddy_ball_pool.services;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StageCreationService {

    public static Stage createStage(Stage stage, String fxmlLocation) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageCreationService.class.getClassLoader().getResource(fxmlLocation)));
        stage.setTitle("Threaddy Ball Pool");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        return stage;
    }

    public static void setHandCursor(Node node) {
        node.setCursor(Cursor.HAND);
    }

    public static void disableAllControls(AnchorPane area) {
        area.getChildren().stream()
                .filter(node -> !(node instanceof ImageView) || node.getId().equals("startButton"))
                .forEach(node -> node.setDisable(true));
    }
    public static void enableAllControls(AnchorPane area){
        area.getChildren().forEach(node -> node.setDisable(false));
    }
}