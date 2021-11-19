package com.travelhelp.controller;

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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    public TextField tfUserNameSignIn,tfPasswordSignIn;
    public Button btSignIn,btHome;
    public ImageView imageViewLogo;

    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();
    }

    @FXML
    public void SignIn(Event event) {

        if (!validateTextFields()) {
            return;
        }

        String username = tfUserNameSignIn.getText();
        String password = tfPasswordSignIn.getText();

        User user = new User();
        user.setUserName(username);
        user.setPassword(password);

        Call<User> userCall = userService.addNewUser(user);

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

                            Stage actualStage = (Stage) btSignIn.getScene().getWindow();
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
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    return;
                });
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

        Stage actualStage = (Stage) this.btSignIn.getScene().getWindow();
        actualStage.close();
    }

    private boolean validateTextFields() {
        if (tfPasswordSignIn.getText().isBlank() || tfUserNameSignIn.getText().isBlank()) {
            Alerts.showErrorAlert("No puedes dejar las casillas en blanco");
            return false;
        }
        else if (tfUserNameSignIn.getText().length() < 5 || tfUserNameSignIn.getText().length() > 12) {
            Alerts.showErrorAlert("El nombre de usuario debe contener entre 5 y 12 caracteres");
            return false;
        }
        else if (tfPasswordSignIn.getText().length() < 8) {
            Alerts.showErrorAlert("La contraseña debe contener al menos 8 caracteres");
            return false;
        }
        else {
            return true;
        }
    }

}
