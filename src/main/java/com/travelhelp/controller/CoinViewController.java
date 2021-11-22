package com.travelhelp.controller;

import com.travelhelp.domain.Coin;
import com.travelhelp.domain.Plug;
import com.travelhelp.service.coin.CoinService;
import com.travelhelp.service.plug.PlugService;
import com.travelhelp.utils.Alerts;
import com.travelhelp.utils.R;
import javafx.application.Platform;
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
import java.util.ResourceBundle;

public class CoinViewController implements Initializable {

    public Label lbIsoCode,lbSymbol,lbMonetaryUnit;

    private CoinService coinService;
    private long idCountry;

    public CoinViewController(long idCountry) {
        this.idCountry = idCountry;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coinService = new CoinService();

        Call<Coin> coinCall = coinService.getCoinFromCountry(this.idCountry);
        coinCall.enqueue(new Callback<Coin>() {
            @Override
            public void onResponse(Call<Coin> call, Response<Coin> response) {
                Platform.runLater(() -> {
                    lbIsoCode.setText(response.body().getIsoCode());
                    lbMonetaryUnit.setText(response.body().getMonetaryUnit());
                    lbSymbol.setText(response.body().getSymbol());
                });
            }

            @Override
            public void onFailure(Call<Coin> call, Throwable throwable) {
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

        Stage actualStage = (Stage) this.lbIsoCode.getScene().getWindow();
        actualStage.close();
    }
}
