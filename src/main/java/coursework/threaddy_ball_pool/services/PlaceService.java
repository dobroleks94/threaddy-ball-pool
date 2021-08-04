package coursework.threaddy_ball_pool.services;

import coursework.threaddy_ball_pool.models.Ball;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.Comparator;

public class PlaceService {

    private static VBox places;
    public static void setPlacesInstance(VBox places){
        PlaceService.places = places;
    }
    public static void givePlaces() {
        places.setAlignment(Pos.TOP_CENTER);
        BallsService.getAllBalls().stream()
                .sorted(Comparator.comparingInt(Ball::getOwnPlace))
                .map(Ball::cloneEngineInstance)
                .forEach(ballInstance -> Platform.runLater(() -> places.getChildren().add(ballInstance)));
    }

    public static void clearPlaceBox() {
        places.getChildren().clear();
    }
}
