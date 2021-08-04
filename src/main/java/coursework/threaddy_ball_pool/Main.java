package coursework.threaddy_ball_pool;

import coursework.threaddy_ball_pool.services.StageCreationService;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        StageCreationService.createStage(primaryStage, "fxml/mainPage.fxml");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
