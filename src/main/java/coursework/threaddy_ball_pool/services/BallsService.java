package coursework.threaddy_ball_pool.services;

import coursework.threaddy_ball_pool.models.Ball;
import coursework.threaddy_ball_pool.models.enums.ResourcesPaths;
import coursework.threaddy_ball_pool.models.enums.UtilCoordinates;
import coursework.threaddy_ball_pool.models.enums.UtilMeasures;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class BallsService {

    private final static Set<Ball> balls = new LinkedHashSet<>();

    public static Ball createNewBall(int ballNumber, int ballSpeed) {
        Ball ball = Ball.builder()
                .ballNumber(ballNumber)
                .ballSpeed(updateBallSpeed(ballSpeed))
                .engineInstance(createEngineInstance(ballNumber))
                .build();
        BallsService.boostBallSpeed(ball);
        utilUpdates(ball);
        return ball;
    }

    public static void boostBallSpeed(Ball ball) {
        int boosted = ball.getBallSpeed() - ball.getPriority();
        ball.setBallSpeedBoost(Math.max(boosted, UtilMeasures.MIN_SPEED.getMeasure()));
    }

    private static void utilUpdates(Ball ball) {
        ball.setDaemon(true);
        StageCreationService.setHandCursor(ball.getEngineInstance());
        PrioritySettingsService.createSliderAreaFor(ball);
        EventService.setEventForBall(ball);
    }

    public static Set<Ball> getAllBalls() {
        return balls;
    }

    public static void addBall(Ball ball) {
        balls.add(ball);
    }

    public static int removeBall() {
        int lastBallNumber = balls.size();
        balls.removeIf(ball -> ball.getBallNumber() == lastBallNumber);
        return lastBallNumber;
    }

    public static int updateBallSpeed(int intermediateValue) {
        return UtilMeasures.MILLISECOND_SPEED_START_THRESHOLD.getMeasure()
                - (intermediateValue * UtilMeasures.SPEED_REGULATION.getMeasure() - UtilMeasures.SPEED_REGULATION.getMeasure());
    }

    private static Circle createEngineInstance(int ballNumber) {
        String ballPath = String.format(ResourcesPaths.BALL_IMAGE.getPath(), ballNumber);
        String resourcePath = Objects.requireNonNull(BallsService.class.getClassLoader().getResource(ballPath)).toString();

        Circle engineInstance = new Circle(UtilCoordinates.START.getX(), locateOneByOne(ballNumber), UtilMeasures.RADIUS.getMeasure());
        engineInstance.setFill(new ImagePattern(new Image(resourcePath)));

        return engineInstance;
    }

    private static double locateOneByOne(int ballNumber) {
        return UtilCoordinates.START.getY() + (ballNumber - 1) * (UtilMeasures.RADIUS.getMeasure() * 2);
    }

    public static Ball getByNumber(int ballNumber) {
        return balls.stream()
                .filter(ball -> ballNumber == ball.getBallNumber())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ball doesn't exist!"));
    }
}
