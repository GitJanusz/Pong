package com.game.pong;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

import static javafx.application.Platform.exit;

public class PongMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private final Pane pane = new Pane();
    private final Image table = new Image("file:src/main/resources/table.jpg");
    private final int widthTable = 950;
    private final int heightTable = 500;
    private final Rectangle playerOne = new Rectangle(15, 70, Color.BLACK);
    private final Rectangle playerTwo = new Rectangle(15, 70, Color.RED);
    private final Circle ball = new Circle(10, Color.ORANGE);
    private final int ballPosX = widthTable / 2;
    private final int ballPosY = heightTable / 2;
    private int ballVelocityX;
    private int ballVelocityY;
    private int playerOnePoints;
    private int playerTwoPoints;
    private final Text playerOnePointsText = new Text("" + 0);
    private final Text playerTwoPointsText = new Text("" + 0);
    private final Text wonPlayer = new Text();
    private final Button newGame = new Button("New game");
    private final Button endGame = new Button("End game");
    private final Random random = new Random();

    public void board() {
        BackgroundSize backgroundSize = new BackgroundSize(950, 500, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(table, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        pane.setPrefWidth(widthTable);
        pane.setPrefHeight(heightTable);
        pane.setBackground(background);

        pane.getChildren().addAll(wonPlayer, playerOne, playerTwo, ball, newGame, endGame, playerOnePointsText, playerTwoPointsText);
    }

    public void objectsPositions() {
        playerOne.setLayoutX(5);
        playerOne.setLayoutY(5);

        playerTwo.setLayoutX(930);
        playerTwo.setLayoutY(5);

        ball.setLayoutX(ballPosX);
        ball.setLayoutY(ballPosY);

        playerOnePointsText.setLayoutX(250);
        playerOnePointsText.setLayoutY(450);

        playerTwoPointsText.setLayoutX(700);
        playerTwoPointsText.setLayoutY(450);

        wonPlayer.setLayoutX(80);
        wonPlayer.setLayoutY(250);

        newGame.setLayoutX(342);

        endGame.setLayoutX(465);
    }

    private void objectsStyles() {
        newGame.setFont(Font.font("Monospace",20));

        endGame.setFont(Font.font("Monospace", 20));
        endGame.setTextFill(Color.RED);

        playerOnePointsText.setFont(Font.font("Monospace", 40));

        playerTwoPointsText.setFont(Font.font("Monospace", 40));
        playerTwoPointsText.setFill(Color.RED);

        wonPlayer.setFont(Font.font("Copperplate", 60));
    }

    public void startGame() {
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                ball.setLayoutY(ball.getLayoutY() + ballVelocityY);
                ball.setLayoutX(ball.getLayoutX() + ballVelocityX);

                Bounds bounds = pane.getBoundsInLocal();

                if (ball.getLayoutY() <= (bounds.getMinY() + ball.getRadius())
                        || ball.getLayoutY() >= (bounds.getMaxY() - ball.getRadius())) {
                    ballVelocityY = -ballVelocityY;
                }

                if ((ball.getLayoutX() + ball.getRadius() > playerTwo.getLayoutX())
                        && ball.getLayoutY() >= playerTwo.getLayoutY()
                        && ball.getLayoutY() <= playerTwo.getLayoutY() + playerTwo.getHeight()) {
                    ballVelocityX = -ballVelocityX;
                    ballVelocityX--;
                }

                if ((ball.getLayoutX() < playerOne.getLayoutX() + playerOne.getWidth() + ball.getRadius())
                        && ball.getLayoutY() >= playerOne.getLayoutY()
                        && ball.getLayoutY() <= playerOne.getLayoutY() + playerOne.getHeight()) {
                    ballVelocityX = -ballVelocityX;
                    ballVelocityX++;
                }


                if (ball.getLayoutX() < playerOne.getLayoutX() - 50) {
                    playerTwoPoints++;
                    resetBallPosition();
                    resetBallSpeed();
                    playerTwoPointsText.setText("" + playerTwoPoints);
                    if (playerTwoPoints == 10) {
                        wonPlayer.setText("WINNER IS RED PLAYER!");
                        wonPlayer.setFill(Color.RED);
                        stop();
                    }
                }

                if (ball.getLayoutX() > playerTwo.getLayoutX() + 50) {
                    playerOnePoints++;
                    resetBallPosition();
                    resetBallSpeed();
                    playerOnePointsText.setText("" + playerOnePoints);
                    if (playerOnePoints == 10) {
                        wonPlayer.setText("WINNER IS BLACK PLAYER!");
                        stop();
                    }
                }
            }
        };

        newGame.setOnAction((e) -> {
            runNewGame();
            timer.start();
        });

        endGame.setOnAction((e) -> exit());
    }

    private Parent parent() {
        board();
        objectsPositions();
        objectsStyles();
        startGame();
        control();
        return pane;
    }

    public void resetBallSpeed() {
        ballVelocityY = random.nextInt(2) == 0 ? random.nextInt(3) +1 : -3;
        ballVelocityX = random.nextInt(2) == 0 ? random.nextInt(3) +1 : -3;
    }

    public void resetBallPosition() {
        ball.setLayoutX(ballPosX);
        ball.setLayoutY(ballPosY);
    }

    public void resetPlayersPoints() {
        playerOnePoints = 0;
        playerTwoPoints = 0;
        playerTwoPointsText.setText("" + 0);
        playerOnePointsText.setText("" + 0);
        wonPlayer.setText(null);
    }

    private void runNewGame() {
        resetBallPosition();
        resetPlayersPoints();
        resetBallSpeed();
    }

    public void control() {
        pane.setOnKeyPressed(event -> {
            if (playerTwo.getLayoutY() <= 420) {
                switch (event.getCode()) {
                    case K:
                        playerTwo.relocate(playerTwo.getLayoutX(), playerTwo.getLayoutY() + 30);
                }
            }
            if (playerTwo.getLayoutY() >= 20) {
                switch (event.getCode()) {
                    case I:
                        playerTwo.relocate(playerTwo.getLayoutX(), playerTwo.getLayoutY() - 30);
                }
            }
            if (playerOne.getLayoutY() <= 420) {
                switch (event.getCode()) {
                    case S:
                        playerOne.relocate(playerOne.getLayoutX(), playerOne.getLayoutY() + 30);
                }
            }
            if (playerOne.getLayoutY() >= 20) {
                switch (event.getCode()) {
                    case W:
                        playerOne.relocate(playerOne.getLayoutX(), playerOne.getLayoutY() - 30);
                }
            }
        });
    }

        @Override
        public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(parent());

        primaryStage.setTitle("PONG");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
