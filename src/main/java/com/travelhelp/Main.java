package com.travelhelp;

import com.travelhelp.controller.AppAdminController;
import com.travelhelp.controller.AppController;
import com.travelhelp.controller.HomeController;
import com.travelhelp.controller.LogInController;
import com.travelhelp.utils.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) { launch(); }
/* arranca el de usuario */
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader();
//        AppController controller = new AppController();
//        loader.setLocation(R.getUI("interfaz"));
//        loader.setController(controller);
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    /* arranca el de admin */
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader();
//        AppAdminController controller = new AppAdminController();
//        loader.setLocation(R.getUI("interfaz_admin"));
//        loader.setController(controller);
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    /* Arranca el home */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        HomeController controller = new HomeController();
        loader.setLocation(R.getUI("interfaz_home"));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /* Arranca el login */
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader();
//        LogInController controller = new LogInController();
//        loader.setLocation(R.getUI("interfaz_login"));
//        loader.setController(controller);
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
