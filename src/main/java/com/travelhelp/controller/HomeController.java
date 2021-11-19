package com.travelhelp.controller;

import com.travelhelp.utils.R;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    public Button btSignInHome,btLogInHome,btAplication;

    @FXML
    public void navigateAplication(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        AppController controller = new AppController();
        loader.setLocation(R.getUI("interfaz"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.btAplication.getScene().getWindow();
        actualStage.close();
    }

    @FXML
    public void navigateLogIn(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        LogInController controller = new LogInController();
        loader.setLocation(R.getUI("interfaz_login"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.btAplication.getScene().getWindow();
        actualStage.close();
    }

    @FXML
    public void navigateSignIn(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        SignInController controller = new SignInController();
        loader.setLocation(R.getUI("interfaz_singin"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.btAplication.getScene().getWindow();
        actualStage.close();
    }

}
