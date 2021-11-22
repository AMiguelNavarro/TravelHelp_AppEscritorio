package com.travelhelp.controller;

import com.travelhelp.domain.Electricity;
import com.travelhelp.service.electricity.ElectricityService;
import com.travelhelp.utils.Alerts;
import com.travelhelp.utils.R;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ElectricityViewController implements Initializable {

    public Label lbVoltage,lbFrecuency;

    private long idCountry;
    private String name;
    private ElectricityService electricityService;

    public ElectricityViewController(long idCountry, String name) {
        this.idCountry = idCountry;
        this.name = name;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        electricityService = new ElectricityService();
        Call<Electricity> electricityCall = electricityService.getElectricityFromCountry(this.idCountry);
        electricityCall.enqueue(new Callback<Electricity>() {
            @Override
            public void onResponse(Call<Electricity> call, Response<Electricity> response) {
                Platform.runLater(() -> {
                    lbFrecuency.setText(String.valueOf(response.body().getFrecuency()));
                    lbVoltage.setText(String.valueOf(response.body().getVoltage()));
                });
            }

            @Override
            public void onFailure(Call<Electricity> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert("ERROR");
                });
            }
        });

    }

    public void closeWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        AppController controller = new AppController();
        loader.setLocation(R.getUI("interfaz"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.lbFrecuency.getScene().getWindow();
        actualStage.close();
    }


}
