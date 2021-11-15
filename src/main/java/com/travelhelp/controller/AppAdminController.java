package com.travelhelp.controller;


import com.google.gson.Gson;
import com.travelhelp.domain.Coin;
import com.travelhelp.domain.Electricity;
import com.travelhelp.service.coin.CoinService;
import com.travelhelp.service.electricity.ElectricityService;
import com.travelhelp.utils.Action;
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

import static com.travelhelp.utils.Constants.COIN;
import static com.travelhelp.utils.Constants.ELECTRICITY;

public class AppAdminController implements Initializable {

    public TabPane tpGeneral;
    public Tab tabCountries,tabCities,tabPlugs,tabElectricity,tabCoins,tabLanguages,tabVaccines,tabEmergencyPhones;
    public TextField
            tfFrecuency, tfVoltage,
            tfCodeISOCoin,tfSymbolCoin,tfMonetaryUnitCoin ;
    public ListView lvElectricity,lvCoins;
    public Button
            btNewElectricity,btSaveNewElectricity,btModifyElectricity,btCancelElectricity, btDeleteElectricity,
            btNewCoin,btSaveNewCoin,btModifyCoin,btCancelCoin,btDeleteCoin;



    private Action action;

    /** Items selected from List View */
    private Electricity electricitySelected;
    private Coin coinSelected;

    /** Services */
    private ElectricityService electricityService;
    private CoinService coinService;

    /** Observables List */
    private ObservableList<Electricity> listElectricities;
    private ObservableList<Coin> listCoins;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        electricityService = new ElectricityService();
        coinService = new CoinService();

        listElectricities = FXCollections.observableArrayList();
        lvElectricity.setItems(listElectricities);
        getAllElectricities();

        listCoins = FXCollections.observableArrayList();
        lvCoins.setItems(listCoins);
        getAllCoins();

        initialViewMode(true); // Botones Modificar, cancelar y eliminar desabilitados así como listViews
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
    @FXML
    public void addNewCoin(Event event) {

    }

    @FXML
    public void getCoinFromListView(Event event) {}

    @FXML
    public void activateNewCoin(Event event) {}

    @FXML
    public void activateModifyCoin(Event event) {}

    @FXML
    public void deleteCoin(Event event) {}

    @FXML
    public void cancelActionCoin(Event event) {}


    private void getAllCoins() {
        lvCoins.getItems().clear();
        coinService.getAllCoins()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de monedas cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de monedas"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(coin -> {
                    Platform.runLater(() -> {
                        listCoins.add(coin);
                    });
                });
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /** PAISES -------------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** ELECTRICIDAD -------------------------------------------------------------------------------------------------*/
    @FXML
    public void addNewElectricity(Event event) {

        if (action.equals(Action.NEW)) {

            if (!validateTextFields(ELECTRICITY)) {
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
                        activateNewElectricityMode(false);
                        resetTextfieldsElectricity();
                    });

                }

                @Override
                public void onFailure(Call<Electricity> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewElectricityMode(false);
                        resetTextfieldsElectricity();
                    });
                }
            });

        } else { // entonces sera modificar

            if (!validateTextFields(ELECTRICITY)) {
                return;
            }

            Electricity newElectricity = new Electricity();
            newElectricity.setFrecuency(Integer.parseInt(tfFrecuency.getText()));
            newElectricity.setVoltage(Integer.parseInt(tfVoltage.getText()));
            newElectricity.setId(electricitySelected.getId());

            Call<Electricity> electricityCall = electricityService.modifyElectricity(newElectricity.getId(), newElectricity);
            electricityCall.enqueue(new Callback<Electricity>() {
                @Override
                public void onResponse(Call<Electricity> call, Response<Electricity> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Electricidad modificada correctamente: " + new Gson().toJson(response.body()));
                        getAllElectricities();
                        activateEditMode(false);
                        resetTextfieldsElectricity();
                    });
                }

                @Override
                public void onFailure(Call<Electricity> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditMode(false);
                        resetTextfieldsElectricity();
                    });
                }
            });
        }

    }

    @FXML
    public void getElectricityFromListView(Event event) {
        electricitySelected = (Electricity) lvElectricity.getSelectionModel().getSelectedItem();
        if (!validateItemSelectedFromListView(electricitySelected)) {
            return;
        }
        tfVoltage.setText(String.valueOf(electricitySelected.getVoltage()));
        tfFrecuency.setText(String.valueOf(electricitySelected.getFrecuency()));
    }

    @FXML
    public void activateNewElectricity(Event event) {
        action = Action.NEW;
        activateNewElectricityMode(true);
    }

    @FXML
    public void activateModifyElectricity(Event event) {
        action = Action.MODIFY;
        activateEditMode(true);
    }

    @FXML
    public void deleteElectricity(Event event) {
        if (lvElectricity.isDisable()) {
            lvElectricity.setDisable(false);
            tfVoltage.setDisable(true);
            tfFrecuency.setDisable(true);
            activateDeleteMode(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }
        if (!validateTextFields(ELECTRICITY)) {
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
        activateEditMode(false);
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


    private void activateNewElectricityMode(boolean mode) {
        btCancelElectricity.setDisable(!mode);
        btModifyElectricity.setDisable(mode);
        btSaveNewElectricity.setDisable(!mode);
        btNewElectricity.setDisable(mode);
        btDeleteElectricity.setDisable(mode);
        tfFrecuency.setDisable(!mode);
        tfVoltage.setDisable(!mode);
    }

    private void activateDeleteMode(boolean mode) {
        btCancelElectricity.setDisable(!mode);
        btModifyElectricity.setDisable(mode);
        btSaveNewElectricity.setDisable(mode);
        btNewElectricity.setDisable(mode);
    }


    private void resetTextfieldsElectricity() {
        tfFrecuency.setText("");
        tfVoltage.setText("");
    }

    private void activateEditMode(boolean mode) {
        activateNewElectricityMode(mode);
        lvElectricity.setDisable(!mode);
        btDeleteElectricity.setDisable(mode);
        tfFrecuency.setDisable(!mode);
        tfVoltage.setDisable(!mode);
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

    private void initialViewMode(boolean mode) {
        btSaveNewElectricity.setDisable(mode);
        btCancelElectricity.setDisable(mode);
        btSaveNewCoin.setDisable(mode);
        btCancelCoin.setDisable(mode);

        lvElectricity.setDisable(mode);
        lvCoins.setDisable(mode);

        tfFrecuency.setDisable(mode);
        tfVoltage.setDisable(mode);
        tfCodeISOCoin.setDisable(mode);
        tfSymbolCoin.setDisable(mode);
        tfMonetaryUnitCoin.setDisable(mode);
    }

    private boolean validateTextFields(String type) {
        boolean value = false;

        switch (type) {

            case ELECTRICITY:

                if (tfVoltage.getText().isBlank() || tfFrecuency.getText().isBlank()) {
                    Alerts.showErrorAlert("No puedes dejar los valores en blanco");
                    value = false;
                }
                // Comprueba si son números, sacado de aquí https://www.delftstack.com/es/howto/java/how-to-check-if-a-string-is-a-number-in-java/
                else if (!tfVoltage.getText().chars().allMatch(Character::isDigit) || !tfFrecuency.getText().chars().allMatch(Character::isDigit)) {
                    Alerts.showErrorAlert("Los valores introducidos deben de ser números");
                    value = false;
                } else {
                    value = true;
                }

                break;

            case COIN:

                if(tfCodeISOCoin.getText().isBlank() || tfMonetaryUnitCoin.getText().isBlank() || tfSymbolCoin.getText().isBlank()) {
                    Alerts.showErrorAlert("No puedes dejar los valores en blanco");
                    value = false;
                } else {
                    value = true;
                }

                break;

        }

        return value;
    }

}
