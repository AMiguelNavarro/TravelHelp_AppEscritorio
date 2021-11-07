package com.travelhelp.controller;

import com.travelhelp.domain.Country;
import com.travelhelp.service.CountryService;
import com.travelhelp.utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AppController implements Initializable {

    public Button btSearchCountry;
    public TextField tfSearchCountry;
    public ListView lvCountries;
    public Label lbName, lbContinent, lbAcronym, lbDrinkingWater, lbPublicHealthcare, lbNumberOfHabitants, lbPrefix;

    private CountryService apiService;
    private ObservableList<Country> listAllCountries;
    private Country countrySelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO actualizar datos lista
        apiService = new CountryService();

        listAllCountries = FXCollections.observableArrayList();
        lvCountries.setItems(listAllCountries);
        getAllCountries();
    }

    private void getAllCountries() {
        lvCountries.getItems().clear();
        apiService.getAllCountries()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de paises descargado correctamente"))
                .doOnError(throwable -> System.out.println("ERROR AL DESCARGAR EL LISTADO -> " + throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(country -> listAllCountries.add(country));
    }

    @FXML
    public void searchCountry(Event event){
        loadDataOfCountry(tfSearchCountry.getText());
    }

    @FXML
    public void getCountryFromListView(Event event) {
        countrySelected = (Country) lvCountries.getSelectionModel().getSelectedItem();
        if (!validateSelectedCountry()) {
            return;
        }
        tfSearchCountry.setText(countrySelected.getName());
    }

    private void loadDataOfCountry(String name) {

        // Se aprovecha la consulta inicial que recoge todos los paises y se filtra por el nombre introducido en el text field
        /* INFORMACION SACADA DE AQUI https://www.baeldung.com/find-list-element-java */
        Country countrySearched = listAllCountries.stream()
                .filter(country -> country.getName().equals(name))
                .findAny()
                .orElse(null);

        if (countrySearched == null) {
            Alerts.showErrorAlert("El país " + name + " no existe");
            tfSearchCountry.setText("");
            cleanCountryInformationLabels();
            return;
        }

        lbName.setText(countrySearched.getName());
        lbAcronym.setText(countrySearched.getAcronym());
        lbContinent.setText(countrySearched.getContinent());
        if (countrySearched.isPublicHealthcare()) {
            lbPublicHealthcare.setText("SI");
        } else {
            lbPublicHealthcare.setText("NO");
        }
        if (countrySearched.isDrinkingWater()) {
            lbDrinkingWater.setText("SI");
        } else {
            lbDrinkingWater.setText("NO");
        }
        lbNumberOfHabitants.setText(String.valueOf(countrySearched.getNumberOfHabitants()));
        lbPrefix.setText(countrySearched.getPrefix());

    }

    private void cleanCountryInformationLabels() {
        lbName.setText("");
        lbContinent.setText("");
        lbAcronym.setText("");
        lbDrinkingWater.setText("");
        lbNumberOfHabitants.setText("");
        lbPrefix.setText("");
        lbPublicHealthcare.setText("");
    }

    private boolean validateSelectedCountry() {
        if (countrySelected == null) {
            Alerts.showErrorAlert("No has seleccionado ningún país");
            return false;
        } else {
            return true;
        }
    }


}
