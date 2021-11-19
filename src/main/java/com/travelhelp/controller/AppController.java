package com.travelhelp.controller;

import com.travelhelp.domain.Country;
import com.travelhelp.service.country.CountryService;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class AppController implements Initializable {

    public Button btHome,btShowCoinView,btShowPlugView,btShowVaccineView,btShowElectricityView,btShowLanguageView,btShowEmergencyPhoneView,btShowCityView;
    public ListView lvCountries;
    public Label lbName, lbContinent, lbAcronym, lbDrinkingWater, lbPublicHealthcare, lbNumberOfHabitants, lbPrefix;

    private CountryService apiService;
    private ObservableList<Country> listAllCountries;
    private Country countrySelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    public void getCountryFromListView(Event event) {
        countrySelected = (Country) lvCountries.getSelectionModel().getSelectedItem();
        if (!validateSelectedCountry()) {
            return;
        }
        loadDataOfCountry(countrySelected.getName());
    }

    @FXML
    public void goHome(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        HomeController controller = new HomeController();
        loader.setLocation(R.getUI("interfaz_home"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.btHome.getScene().getWindow();
        actualStage.close();
    }

    @FXML
    public void showCoinView(Event event) { }

    @FXML
    public void showPlugView(Event event) { }

    @FXML
    public void showVaccineView(Event event) { }

    @FXML
    public void showElectricityView(Event event) { }

    @FXML
    public void showLanguageView(Event event) { }

    @FXML
    public void showEmergencyPhoneView(Event event) { }

    @FXML
    public void showCityView(Event event) throws IOException {

        if (countrySelected == null) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        CitiesViewController controller = new CitiesViewController(countrySelected.getId());
        loader.setLocation(R.getUI("vista_ciudades"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            try {
                controller.closeWindow();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Stage actualStage = (Stage) this.btHome.getScene().getWindow();
        actualStage.close();
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
