package com.btc;

import java.io.File;

import com.btc.config.Config;
import com.btc.scene.GameScene;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;;

public class GameViewController extends Application {
	
	// Properties
	GameScene gameScene;
	Button backButton;
	public static void main(String[] args) {
		
		launch(args);		
		System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Platformer Game");
		gameScene = new GameScene();
		
		backButton = new Button();
		backButton.setFont(Font.font("Chalkduster", FontWeight.BOLD, 18));
		backButton.setText("BACK");
		backButton.setPrefSize(100, 20);
		backButton.setLayoutX(gameScene.gameWidth() - backButton.getPrefWidth() - 5);
		backButton.setLayoutY(20);
		backButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		gameScene.root.getChildren().add(backButton);
		backButton.setFocusTraversable(false);
		backButton.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {	
				try {
					backButton.setTextFill(Color.BLACK);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
		backButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {	
				try {
					backButton.setTextFill(Color.WHITE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				MainMenuViewController mainMenuViewController = new MainMenuViewController();
				try {
					gameScene.backToMainMenu();
					primaryStage.close();
					mainMenuViewController.start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		
		primaryStage.setScene(gameScene);
		primaryStage.show();
	}
}
