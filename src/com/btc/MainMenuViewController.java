package com.btc;

import java.io.FileInputStream;

import com.btc.config.Config;
import com.btc.scene.GameScene;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenuViewController extends Application {
	public static void main(String[] args) {

		launch(args);		
		System.exit(0);
	}
	
	public MainMenuViewController() {
		// TODO Auto-generated constructor stub
	}

	
	private Canvas canvas;
	private Button playButton;
	private Button exitButton;
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Cyclops 's Advanture");
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		canvas = new Canvas(Config.WindowProperties.WINDOW_WIDTH, Config.WindowProperties.WINDOW_HEIGHT);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setGlobalAlpha(0.5);
		
		
		Image backgroundImage2 = new Image(new FileInputStream("menus/background.jpg"));		
		gc.drawImage(backgroundImage2, 0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setGlobalAlpha(1.0);
		
		
		playButton = new Button();
		setupPlayButton();
		exitButton = new Button();
		setupExitButton();
		playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				GameViewController gameViewController = new GameViewController();		
				try {
					
					primaryStage.close();
					gameViewController.start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
		
		
		root.getChildren().add(playButton);
		root.getChildren().add(exitButton);
		primaryStage.show();
	}
	
	private void setupPlayButton() {

		playButton.setFont(Font.font("Chalkduster", FontWeight.BOLD, 40));
		
		playButton.setText("PLAY");
		playButton.setPrefSize(180, 40);
		playButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));		
		
		playButton.setLayoutX(canvas.getWidth() / 2 - playButton.getPrefWidth() / 2 );
		playButton.setLayoutY(canvas.getHeight() / 2 - playButton.getPrefHeight() / 2 - 100);
		playButton.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {	
				try {
					playButton.setTextFill(Color.BLACK);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
		playButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {	
				try {
					SoundManager.playSound("sounds/button.wav");
					playButton.setTextFill(Color.WHITE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
	}
	
	private void setupExitButton() {

		exitButton.setFont(Font.font("Chalkduster", FontWeight.BOLD, 40));
		
		exitButton.setText("EXIT");
		exitButton.setPrefSize(180, 40);
		
		
		exitButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));		
		
		exitButton.setLayoutX(canvas.getWidth() / 2 - playButton.getPrefWidth() / 2 );
		exitButton.setLayoutY(canvas.getHeight() / 2 - playButton.getPrefHeight() / 2);
		exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {	
				try {
					System.exit(0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
		exitButton.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {	
				try {
					exitButton.setTextFill(Color.BLACK);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
		exitButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {	
				try {
					SoundManager.playSound("sounds/button.wav");
					exitButton.setTextFill(Color.WHITE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}

		});
	}
}
