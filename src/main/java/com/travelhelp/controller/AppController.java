package com.travelhelp.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    public Button button;
    public Text text;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO actualizar datos lista
    }

    @FXML
    public void saludar(Event event) {
        text.setText("Hola mundo!");
    }

}
