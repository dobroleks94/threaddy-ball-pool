package coursework.threaddy_ball_pool.models;

import coursework.threaddy_ball_pool.controller.MainController;
import coursework.threaddy_ball_pool.models.enums.UtilCoordinates;
import coursework.threaddy_ball_pool.models.enums.UtilMeasures;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import lombok.*;

import java.util.concurrent.TimeUnit;

@Builder
@Getter
@Setter
public class Ball extends Thread {

    private final int ballNumber;
    private int ballSpeed;
    private int ballSpeedBoost;
    private final Circle engineInstance;
    private static int currentPlace = 1;
    private int ownPlace;

    @SneakyThrows
    @Override
    public void run() {
        while (this.engineInstance.getCenterX() < UtilCoordinates.FINISH.getX()) {
            TimeUnit.MILLISECONDS.sleep(Math.min(ballSpeed, ballSpeedBoost));
            Platform.runLater(
                    () -> getEngineInstance().setCenterX(getEngineInstance().getCenterX() + UtilMeasures.STEP.getMeasure()));

        }
        setOwnPlace(currentPlace++);
        MainController.getBarrier().await();
    }

    public static Circle cloneEngineInstance(Ball ball) {
        return new Circle(
                ball.getEngineInstance().getCenterX(),
                ball.getEngineInstance().getCenterY(),
                ball.getEngineInstance().getRadius(),
                ball.getEngineInstance().getFill()
        );
    }
}
