package coursework.threaddy_ball_pool.services;

import coursework.threaddy_ball_pool.models.Ball;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class EventService {

    public static void setEventForBall(Ball ball) {
        ball.getEngineInstance().setOnMouseClicked((mouseEvent) -> {
            VBox sliderArea = PrioritySettingsService.getPriorityScale(ball);
            sliderArea.setVisible(!sliderArea.isVisible());
            PrioritySettingsService.hideAllPrioritySettingsExcept(ball);
        });
    }

    public static void setEventForSlider(Slider slider) {
        slider.setOnDragDetected((event) -> {
            slider.setOnMouseReleased((mouseEvent) -> {
                PrioritySettingsService.setPriorityFrom(slider);
                BallsService.boostBallSpeed(
                        BallsService.getByNumber(
                                Integer.parseInt(slider.getId().replace("slider", ""))));
                System.out.println();
            });
        });
    }
}
