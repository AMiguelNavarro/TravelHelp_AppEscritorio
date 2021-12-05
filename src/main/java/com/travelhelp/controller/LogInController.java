package com.travelhelp.controller;

import com.google.gson.Gson;
import com.travelhelp.domain.User;
import com.travelhelp.service.user.UserService;
import com.travelhelp.utils.Alerts;
import com.travelhelp.utils.R;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController  implements Initializable {

    public TextField tfUserNameLogIn;
    public PasswordField tfPasswordLogIn;
    public Button btLogIn,btHome;
    public ImageView imageViewLogo;

    private UserService userService;

    /* IMPORTANTE: En scene builder poner ruta absoluta de imagen sino no carga https://stackoverflow.com/questions/29397367/javafx-scenebuilder-imageview-not-working*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();
    }

    @FXML
    public void logIn(Event event) {

        if (!validateTextFields()) {
            return;
        }

        String username = tfUserNameLogIn.getText();
        String password = tfPasswordLogIn.getText();

        Call<User> userCall = userService.checkUser(username, password);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            AppAdminController controller = new AppAdminController();
                            loader.setLocation(R.getUI("interfaz_admin"));//TODO cambiar a signin
                            loader.setController(controller);
                            Parent root = null;

                            root = loader.load();

                            Stage stage = new Stage();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();

                            Stage actualStage = (Stage) btLogIn.getScene().getWindow();
                            actualStage.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } else {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert("Usuario o contraseña incorrectos");
                        return;
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                System.out.println("INCORRECTO");
            }
        });


    }

    @FXML
    public void goHome(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        HomeController controller = new HomeController();
        loader.setLocation(R.getUI("interfaz_home"));//TODO cambiar a signin
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.btLogIn.getScene().getWindow();
        actualStage.close();
    }

    private boolean validateTextFields() {
        if (tfPasswordLogIn.getText().isBlank() || tfUserNameLogIn.getText().isBlank()) {
            Alerts.showErrorAlert("No puedes dejar las casillas en blanco");
            return false;
        }
        else if (tfUserNameLogIn.getText().length() < 5 || tfUserNameLogIn.getText().length() > 12) {
            Alerts.showErrorAlert("El nombre de usuario debe contener entre 5 y 12 caracteres");
            return false;
        }
        else if (tfPasswordLogIn.getText().length() < 8) {
            Alerts.showErrorAlert("La contraseña debe contener al menos 8 caracteres");
            return false;
        }
        else {
            return true;
        }
    }


}
