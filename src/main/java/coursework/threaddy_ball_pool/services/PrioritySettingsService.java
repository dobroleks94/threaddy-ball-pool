package coursework.threaddy_ball_pool.services;

import coursework.threaddy_ball_pool.models.Ball;
import coursework.threaddy_ball_pool.models.enums.JavaFXConfigValues;
import coursework.threaddy_ball_pool.models.enums.UtilMeasures;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class PrioritySettingsService {


    private static final Map<Integer, VBox> priorityScale = new HashMap<>();

    public static VBox getPriorityScale(Ball ball){
        return priorityScale.getOrDefault(ball.getBallNumber(), createSliderAreaFor(ball));
    }
    public static void updatePriorityScale(int ballNumber){
        priorityScale.remove(ballNumber);
    }
    public static void setPriorityFrom(Slider slider){
        int ballNumber = Integer.parseInt(slider.getId().replace("slider", ""));
        priorityScale.keySet().stream()
                .filter(ball -> ball == ballNumber)
                .findFirst()
                .ifPresent(ball -> BallsService.getByNumber(ball).setPriority((int) slider.getValue()));
    }
    public static void hideAllPrioritySettingsExcept(Ball ball) {
        priorityScale.entrySet().stream()
                .filter(entry -> BallsService.getByNumber(entry.getKey()).getBallNumber() != ball.getBallNumber())
                .map(Map.Entry::getValue)
                .filter(Node::isVisible)
                .forEach(vBox -> vBox.setVisible(false));
    }
    public static void hideAllPrioritySettings() {
        priorityScale.values().stream()
                .filter(Node::isVisible)
                .forEach(PrioritySettingsService::hideVBox);
    }
    public static void hideVBox(VBox vBox){
        vBox.setVisible(false);
    }
    public static VBox createSliderAreaFor(Ball ball) {
        Label sign = createSign(ball.getBallNumber());
        Slider slider = createSlider(
                ball,
                UtilMeasures.MIN_PRIORITY.getMeasure(),
                UtilMeasures.MAX_PRIORITY.getMeasure(),
                ball.getPriority(),
                UtilMeasures.PRIORITY_STEP.getMeasure());

        VBox vbox = configureSliderContainer(ball, sign, slider);

        priorityScale.putIfAbsent(ball.getBallNumber(), vbox);
        return vbox;
    }

    private static VBox configureSliderContainer(Ball ball, Node ... nodes) {
        VBox vbox = new VBox(nodes);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setLayoutX(ball.getEngineInstance().getCenterX() + UtilMeasures.NODE_SPACING.getMeasure());
        vbox.setLayoutY(ball.getEngineInstance().getCenterY() - UtilMeasures.RADIUS.getMeasure() * 2);
        vbox.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, null)));
        vbox.setVisible(false);
        return vbox;
    }

    private static Label createSign(int ballNumber){
        Label sign = new Label("Priority for Ball " + ballNumber);
        sign.setFont(new Font("Manjari Bold", UtilMeasures.SLIDER_TITLE_FONT_SIZE.getMeasure()));
        sign.setTextFill(Color.WHITE);
        return sign;
    }
    private static Slider createSlider(Ball ball, int min, int max, double value, double step){
        Slider slider = new Slider(min, max, value);
        slider.setBlockIncrement(step);
        slider.showTickLabelsProperty().set(false);
        slider.showTickMarksProperty().set(false);
        slider.snapToTicksProperty().set(true);
        slider.setMajorTickUnit(JavaFXConfigValues.MAJOR_TICK_UNIT.getValue());
        slider.setMinorTickCount((int) JavaFXConfigValues.MINOR_TICK_COUNT.getValue());
        StageCreationService.setHandCursor(slider);
        EventService.setEventForSlider(slider);

        slider.setId("slider".concat(String.valueOf(ball.getBallNumber())));

        return slider;
    }
}
