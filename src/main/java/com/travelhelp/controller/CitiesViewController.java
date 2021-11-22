package com.travelhelp.controller;

import com.travelhelp.domain.City;
import com.travelhelp.service.city.CityService;
import com.travelhelp.utils.Alerts;
import com.travelhelp.utils.R;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class CitiesViewController implements Initializable {

    public ListView lvCities;
    public Label lbName,lbExtension,lbNumberOfHabitants;

    private ObservableList<City> listCities;

    private CityService cityService;
    private City citySelected;

    private long idCountry;

    public CitiesViewController(long idCountry) {
        this.idCountry = idCountry;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cityService = new CityService();

        listCities = FXCollections.observableArrayList();
        lvCities.setItems(listCities);
        getCitiesFromCountry();
    }

    @FXML
    public void getCitySelected(Event event) {
        citySelected = (City) lvCities.getSelectionModel().getSelectedItem();
        if (citySelected == null) {
            Alerts.showErrorAlert("No has seleccionado ningún item del listado");
            return;
        }
        lbName.setText(citySelected.getName());
        lbExtension.setText(String.valueOf(citySelected.getExtension()));
        lbNumberOfHabitants.setText(String.valueOf(citySelected.getNumberOfHabitants()));
    }

    private void getCitiesFromCountry() {
        lvCities.getItems().clear();
        cityService.getCitiesFromCountry(this.idCountry)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de ciudaes ok"))
                .doOnError(throwable -> System.out.println("ERROR AL DESCARGAR EL LISTADO -> " + throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(country -> listCities.add(country));
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

        Stage actualStage = (Stage) this.lbNumberOfHabitants.getScene().getWindow();
        actualStage.close();
    }
}
