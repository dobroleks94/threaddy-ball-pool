package coursework.threaddy_ball_pool.controller;

import coursework.threaddy_ball_pool.models.Ball;
import coursework.threaddy_ball_pool.models.enums.UtilCoordinates;
import coursework.threaddy_ball_pool.models.enums.UtilMeasures;
import coursework.threaddy_ball_pool.services.BallsService;
import coursework.threaddy_ball_pool.services.PlaceService;
import coursework.threaddy_ball_pool.services.PrioritySettingsService;
import coursework.threaddy_ball_pool.services.StageCreationService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MainController implements Initializable {

    @FXML
    public AnchorPane ballPoolArea, mainScope;
    @FXML
    public Text ballsCount, ballsSpeed;
    @FXML
    public VBox placesBox;

    private ExecutorService executorService;
    private static CyclicBarrier barrier;

    public static CyclicBarrier getBarrier() {
        return barrier;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ballPoolArea.getChildren().add(new Group());
        PlaceService.setPlacesInstance(placesBox);
        IntStream.range(1, 4).forEach(ballNumber -> {
            Ball ball = BallsService.createNewBall(ballNumber, 1);
            ballPoolArea.getChildren().add(ballPoolArea.getChildren().size() - 2, PrioritySettingsService.getPriorityScale(ball));
            BallsService.addBall(ball);

            FXCollections.synchronizedObservableList(getBalls()).add(ball.getEngineInstance());
        });
    }

    public void remove(MouseEvent actionEvent) {
        if (getBalls().size() > UtilMeasures.MIN_BALLS_SIZE.getMeasure()) {
            int ballToRemove = BallsService.removeBall();
            removeBallAndPriorityScaleOnScene(ballToRemove);
            ballsCount.setText(String.valueOf(getBalls().size()));
        }
    }

    public void add(MouseEvent actionEvent) {
        int ballNumber = getBalls().size() + 1;
        if(ballNumber <= UtilMeasures.MAX_BALLS_SIZE.getMeasure()) {
            Ball ball = BallsService.createNewBall(ballNumber, Integer.parseInt(ballsSpeed.getText()));
            BallsService.addBall(ball);
            ballsCount.setText(String.valueOf(ballNumber));
            addBallAndPriorityScaleOnScene(ball);
        }
    }

    private void addBallAndPriorityScaleOnScene(Ball ball) {
        ballPoolArea.getChildren().add(ballPoolArea.getChildren().size() - 2, PrioritySettingsService.getPriorityScale(ball));
        FXCollections.synchronizedObservableList(getBalls()).add(ball.getEngineInstance());
    }
    private void removeBallAndPriorityScaleOnScene(int ballNumber){
        ballPoolArea.getChildren().remove(ballPoolArea.getChildren().size() - 3);
        FXCollections.synchronizedObservableList(getBalls()).remove(getBalls().size() - 1);
        PrioritySettingsService.updatePriorityScale(ballNumber);
    }

    private ObservableList<Node> getBalls(){
        return ((Group) ballPoolArea.getChildren()
                .get(ballPoolArea.getChildren().size() - 1))
                .getChildren();
    }

    public void startBallsRolling(MouseEvent mouseEvent) {
        barrier = new CyclicBarrier(BallsService.getAllBalls().size(), () -> Platform.runLater(PlaceService::givePlaces));
        executorService = Executors.newFixedThreadPool(BallsService.getAllBalls().size());
        BallsService.getAllBalls().forEach(ball -> {
            executorService.execute(ball);
        });
        PrioritySettingsService.hideAllPrioritySettings();
        StageCreationService.disableAllControls(mainScope);
        executorService.shutdown();

    }

    public void decreaseSpeed(MouseEvent mouseEvent) {
        AtomicInteger currentSpeed = new AtomicInteger(Integer.parseInt(ballsSpeed.getText()));
        if(currentSpeed.get() > UtilMeasures.MIN_SPEED.getMeasure()) {
            currentSpeed.decrementAndGet();
            BallsService.getAllBalls()
                    .forEach(ball -> {
                        ball.setBallSpeed(BallsService.updateBallSpeed(currentSpeed.get()));
                        BallsService.boostBallSpeed(ball);
                    });
            ballsSpeed.setText(String.valueOf(currentSpeed.get()));
        }
    }

    public void increaseSpeed(MouseEvent mouseEvent) {
        AtomicInteger currentSpeed = new AtomicInteger(Integer.parseInt(ballsSpeed.getText()));
        if(currentSpeed.get() < UtilMeasures.MAX_SPEED.getMeasure()) {
            currentSpeed.incrementAndGet();
            BallsService.getAllBalls()
                    .forEach(ball -> {
                        ball.setBallSpeed(BallsService.updateBallSpeed(currentSpeed.get()));
                        BallsService.boostBallSpeed(ball);
                    });
            ballsSpeed.setText(String.valueOf(currentSpeed.get()));
        }
    }

    public void stopExecution(MouseEvent mouseEvent) {
        if (executorService != null){
            BallsService.getAllBalls()
                    .stream()
                    .map(Ball::getEngineInstance)
                    .forEach(engineInstance -> engineInstance.setCenterX(UtilCoordinates.START.getX()));
            StageCreationService.enableAllControls(mainScope);
            PlaceService.clearPlaceBox();
            executorService.shutdownNow();
        }
    }

    public void showAllBallsPriorities(MouseEvent mouseEvent) {
        BallsService.getAllBalls()
                .forEach(ball -> {
                    int ballNum = ball.getBallNumber();
                    int ballPriority = ball.getPriority();
                    System.out.printf("Ball #%d with priority - %d%n", ballNum, ballPriority);
                });
    }
}
