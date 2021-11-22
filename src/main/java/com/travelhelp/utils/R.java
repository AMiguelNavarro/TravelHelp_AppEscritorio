package com.travelhelp.utils;

import com.travelhelp.controller.AppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class R {

    /**
     * Acceso a la carpeta interface para cargar las vistas en fxml
     * @param name
     * @return
     */
    public static URL getUI(String name) {
        return Thread.currentThread().getContextClassLoader().getResource("ui" + File.separator + name + ".fxml");
    }

}
