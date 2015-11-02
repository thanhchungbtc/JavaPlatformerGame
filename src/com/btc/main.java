package com.btc;

import com.btc.config.Config;
import com.btc.scene.GameScene;

import javafx.application.*;
import javafx.stage.Stage;;

public class main extends Application {
	
	// Properties
	GameScene gameScene;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Cyclops 's Advanture");
		gameScene = new GameScene();
		primaryStage.setScene(gameScene);
		primaryStage.show();
	}
}
