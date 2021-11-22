package com.travelhelp.controller;

import com.travelhelp.domain.Plug;
import com.travelhelp.service.plug.PlugService;
import com.travelhelp.utils.Alerts;
import com.travelhelp.utils.R;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlugViewController implements Initializable {

    public WebView wbImage;
    public Label lbType;

    private PlugService plugService;
    private long idCountry;

    public PlugViewController(long idCountry) {
        this.idCountry = idCountry;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        plugService = new PlugService();
        Call<Plug> plugCall = plugService.getPlugsFromCountry(this.idCountry);
        plugCall.enqueue(new Callback<Plug>() {
            @Override
            public void onResponse(Call<Plug> call, Response<Plug> response) {
                Platform.runLater(() -> {
                    wbImage.getEngine().load(response.body().getImage());
                    lbType.setText("Tipo " + response.body().getType());
                });
            }

            @Override
            public void onFailure(Call<Plug> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert("ERROR");
                });
            }
        });
    }

    public void closeWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        AppController controller = new AppController(this.idCountry);
        loader.setLocation(R.getUI("interfaz"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.lbType.getScene().getWindow();
        actualStage.close();
    }
}
