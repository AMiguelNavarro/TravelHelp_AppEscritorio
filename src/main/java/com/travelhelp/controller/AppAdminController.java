package com.travelhelp.controller;


import com.google.gson.Gson;
import com.travelhelp.domain.Electricity;
import com.travelhelp.service.ElectricityService;
import com.travelhelp.utils.Alerts;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event; // NO USAR ACTION EVENT, DA ERROR Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: argument type mismatch
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    public Button btNewElectricity,btSaveNewElectricity,btModifyElectricity,btCancelElectricity, btDeleteElectricity;

    // Para controlar si se debe modificar o añadir un nuevo elemento (¿Se puede generalizar sacándolo fuera y que lo usen todas las clases?)
    public enum Action {
        NEW, MODIFY
    }
    private Action action;

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

        initialButtonsMood(true); // Botones Modificar, cancelar y eliminar desabilitados así como listViews
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

        if (action.equals(Action.NEW)) {

            if (!validateElectricityTextFields()) {
                return;
            }

            Electricity newElectricity = new Electricity();
            newElectricity.setFrecuency(Integer.parseInt(tfFrecuency.getText()));
            newElectricity.setVoltage(Integer.parseInt(tfVoltage.getText()));

            Call<Electricity> elec = electricityService.addNewElectrivity(newElectricity);

            // SACADO DE AQUÍ https://github.com/Siddharha/Java-Fx-Retrofit-API-Call/blob/master/src/sample/Controller.java
            elec.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Electricity> call, Response<Electricity> response) {
                    // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nueva electricidad creada correctamente: " + new Gson().toJson(response.body()));
                        getAllElectricities();
                        activateNewElectricityMood(false);
                        resetTextfieldsElectricity();
                    });

                }

                @Override
                public void onFailure(Call<Electricity> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        resetTextfieldsElectricity();
                    });
                }
            });

        } else { // entonces sera modificar

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

    @FXML
    public void activateNewElectricity(Event event) {
        action = Action.NEW;
        activateNewElectricityMood(true);
    }

    @FXML
    public void activateModifyElectricity(Event event) {
        action = Action.MODIFY;
        activateEditMood(true);
    }

    @FXML
    public void deleteElectricity(Event event) {
        if (lvElectricity.isDisable()) {
            lvElectricity.setDisable(false);
            tfVoltage.setDisable(true);
            tfFrecuency.setDisable(true);
            activateDeleteMood(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }
        if (!validateElectricityTextFields()) {
            return;
        }
        if (electricitySelected.getId() == 0) {
            Alerts.showErrorAlert("Debes seleccionar un item de la lista");
            return;
        }
        Call<ResponseBody> callDelete = electricityService.deleteElectricity(electricitySelected.getId());
        callDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    Alerts.showInfoAlert("Electricidad eliminada correctamente");
                    resetTextfieldsElectricity();
                    getAllElectricities();
                    btNewElectricity.setDisable(false);
                    btModifyElectricity.setDisable(false);
                    btDeleteElectricity.setDisable(false);
                    btSaveNewElectricity.setDisable(true);
                    lvElectricity.setDisable(true);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert("Error al intentar eliminar la electricidad " + throwable.getLocalizedMessage());
                    resetTextfieldsElectricity();
                    btNewElectricity.setDisable(false);
                    btModifyElectricity.setDisable(false);
                    btDeleteElectricity.setDisable(false);
                    btSaveNewElectricity.setDisable(true);
                    lvElectricity.setDisable(true);
                });
            }
        });

    }

    @FXML
    public void cancelActionElectricity(Event event) {
        activateEditMood(false);
        resetTextfieldsElectricity();
    }


    private void getAllElectricities() {
        lvElectricity.getItems().clear();
        electricityService.getAllElectricities()
            .flatMap(Observable::from)
            .doOnCompleted(() -> System.out.println("Listado de electricidades cargado correctamente"))
            .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de electricidades -> " + throwable.getLocalizedMessage()))
            .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
            .subscribe(electricity -> {
                // Para evitar el IllegalStateException por actualizar la vista en un hilo distinto de JAVAFX
                Platform.runLater(() -> {
                    listElectricities.add(electricity);
                });
            });

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

    private void activateNewElectricityMood(boolean mood) {
        btCancelElectricity.setDisable(!mood);
        btModifyElectricity.setDisable(mood);
        btSaveNewElectricity.setDisable(!mood);
        btNewElectricity.setDisable(mood);
        btDeleteElectricity.setDisable(mood);
        tfFrecuency.setDisable(!mood);
        tfVoltage.setDisable(!mood);
    }

    private void activateDeleteMood(boolean mood) {
        btCancelElectricity.setDisable(!mood);
        btModifyElectricity.setDisable(mood);
        btSaveNewElectricity.setDisable(mood);
        btNewElectricity.setDisable(mood);
    }


    private void resetTextfieldsElectricity() {
        tfFrecuency.setText("");
        tfVoltage.setText("");
    }

    private void activateEditMood(boolean mood) {
        activateNewElectricityMood(mood);
        lvElectricity.setDisable(!mood);
        btDeleteElectricity.setDisable(mood);
        tfFrecuency.setDisable(!mood);
        tfVoltage.setDisable(!mood);
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

    private void initialButtonsMood(boolean mood) {
        btSaveNewElectricity.setDisable(mood);
        btCancelElectricity.setDisable(mood);
        btDeleteElectricity.setDisable(!mood);

        lvElectricity.setDisable(mood);
    }

}
