package com.travelhelp.controller;


import com.travelhelp.domain.Electricity;
import com.travelhelp.service.ElectricityService;
import com.travelhelp.utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event; // NO USAR ACTION EVENT, DA ERROR Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: argument type mismatch
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import rx.Observable;
import rx.schedulers.Schedulers;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class AppAdminController implements Initializable {

    public TabPane tpGeneral;
    public Tab tabCountries,tabCities,tabPlugs,tabElectricity,tabCoins,tabLanguages,tabVaccines,tabEmergencyPhones;
    public TextField tfFrecuency, tfVoltage;
    public ListView lvElectricity;
    public Button btNewElectricity,btModifyElectricity,btCancelElectricity;

    /** Items selected from List View */
    private Electricity electricitySelected;

    /** Services */
    private ElectricityService electricityService;

    /** Observables List */
    private ObservableList<Electricity> listElectricities;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        electricityService = new ElectricityService();

        listElectricities = FXCollections.observableArrayList();
        lvElectricity.setItems(listElectricities);
        getAllElectricities();
    }
    /** ENCHUFES -----------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** CIUDADES -----------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** IDIOMAS ------------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** TELEFONOS DE EMERGENCIA --------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** VACUNAS ------------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** MONEDAS ------------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** PAISES -------------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** ELECTRICIDAD -------------------------------------------------------------------------------------------------*/
    @FXML
    public void addNewElectricity(Event event) {

        if (!validateElectricityTextFields()) {
            return;
        }

        Electricity newElectricity = new Electricity();
        newElectricity.setFrecuency(Integer.parseInt(tfFrecuency.getText()));
        newElectricity.setVoltage(Integer.parseInt(tfVoltage.getText()));

        Electricity elec = electricityService.addNewElectrivity(newElectricity);

        if (elec == null) {
            Alerts.showErrorAlert("Error al guardar la electricidad");
            return;
        } else {
            Alerts.showInfoAlert("Electricidad guardada correctamente");
        }


    }

    @FXML
    public void getElectricityFromListView(Event event) {
        electricitySelected = (Electricity) lvElectricity.getSelectionModel().getSelectedItem();
        if (!validateItemSelectedFromListView(electricitySelected)) {
            return;
        }
        tfVoltage.setText(String.valueOf(electricitySelected.getFrecuency()));
        tfFrecuency.setText(String.valueOf(electricitySelected.getFrecuency()));
    }

    private void getAllElectricities() {
        lvElectricity.getItems().clear();
        electricityService.getAllElectricities()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de electricidades cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de electricidades -> " + throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(electricity -> listElectricities.add(electricity));
    }

    private boolean validateElectricityTextFields() {
        if (tfVoltage.getText().isBlank() || tfFrecuency.getText().isBlank()) {
            Alerts.showErrorAlert("No puedes dejar los valores en blanco");
            return false;
        }
        // Comprueba si son números, sacado de aquí https://www.delftstack.com/es/howto/java/how-to-check-if-a-string-is-a-number-in-java/
        else if (!tfVoltage.getText().chars().allMatch(Character::isDigit) || !tfFrecuency.getText().chars().allMatch(Character::isDigit)) {
            Alerts.showErrorAlert("Los valores introducidos deben de ser números");
            return false;
        } else {
            return true;
        }
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    private boolean validateItemSelectedFromListView(Object item) {
        if (item == null) {
            Alerts.showErrorAlert("No has seleccionado ningún elemento del listado");
            return false;
        } else {
            return true;
        }
    }

}
