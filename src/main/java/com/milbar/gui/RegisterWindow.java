package com.milbar.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class RegisterWindow extends JavaFXWindow {
    
    public RegisterWindow(LoginWindowController parentController) throws IOException {
        super(parentController);
        
        fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(
                "fxml/" + RegisterWindow.class.getSimpleName() + ".fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setParentController(parentController);
        stage = new Stage();
        stage.setTitle("");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        setIcon();
        controller.setStage(stage);
        stage.setOnCloseRequest(event -> controller.closeWindow());
        stage.show();
    }
    
}
