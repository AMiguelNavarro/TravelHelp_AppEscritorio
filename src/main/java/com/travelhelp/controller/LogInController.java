package com.travelhelp.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController  implements Initializable {

    public TextField tfUserNameLogIn,tfPasswordLogIn;
    public Button btLogIn;
    public ImageView imageViewLogo;

    /* IMPORTANTE: En scene builder poner ruta absoluta de imagen sino no carga https://stackoverflow.com/questions/29397367/javafx-scenebuilder-imageview-not-working*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void logIn(Event event) {}


}
