package com.travelhelp.controller;


import com.google.gson.Gson;
import com.travelhelp.domain.Coin;
import com.travelhelp.domain.Electricity;
import com.travelhelp.domain.Language;
import com.travelhelp.service.coin.CoinService;
import com.travelhelp.service.electricity.ElectricityService;
import com.travelhelp.service.language.LanguageService;
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

import static com.travelhelp.utils.Constants.*;

public class AppAdminController implements Initializable {

    public TabPane tpGeneral;
    public Tab tabCountries,tabCities,tabPlugs,tabElectricity,tabCoins,tabLanguages,tabVaccines,tabEmergencyPhones;
    public TextField
            tfFrecuency, tfVoltage,
            tfCodeISOCoin,tfSymbolCoin,tfMonetaryUnitCoin,
            tfLanguageName;
    public ListView lvElectricity,lvCoins,lvLanguages;
    public Button
            btNewElectricity,btSaveNewElectricity,btModifyElectricity,btCancelElectricity, btDeleteElectricity,
            btNewCoin,btSaveNewCoin,btModifyCoin,btCancelCoin,btDeleteCoin,
            btNewLanguage,btSaveNewLanguage,btModifyLanguage,btCancelLanguage,btDeleteLanguage;



    private Action action;

    /** Items selected from List View */
    private Electricity electricitySelected;
    private Coin coinSelected;
    private Language languageSelected;

    /** Services */
    private ElectricityService electricityService;
    private CoinService coinService;
    private LanguageService languageService;

    /** Observables List */
    private ObservableList<Electricity> listElectricities;
    private ObservableList<Coin> listCoins;
    private ObservableList<Language> listLanguages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        electricityService = new ElectricityService();
        coinService = new CoinService();
        languageService = new LanguageService();

        listElectricities = FXCollections.observableArrayList();
        lvElectricity.setItems(listElectricities);
        getAllElectricities();

        listCoins = FXCollections.observableArrayList();
        lvCoins.setItems(listCoins);
        getAllCoins();

        listLanguages = FXCollections.observableArrayList();
        lvLanguages.setItems(listLanguages);
        getAllLanguages();

        initialViewMode(true); // Botones Modificar, cancelar y eliminar desabilitados así como listViews
    }
    /** ENCHUFES -----------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** CIUDADES -----------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** IDIOMAS ------------------------------------------------------------------------------------------------------*/

    @FXML
    public void addNewLanguage(Event event) {

        if(action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFields(LANGUAGE)) {
                return;
            }

            Language newLanguage = new Language();
            newLanguage.setName(tfLanguageName.getText());

            Call<Language> languageCall = languageService.addNewLanguage(newLanguage);

            languageCall.enqueue(new Callback<Language>() {
                @Override
                public void onResponse(Call<Language> call, Response<Language> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nuevo idioma creado correctamente: " + new Gson().toJson(response.body()));
                        getAllLanguages();
                        activateNewLanguageMode(false);
                        resetTextFields(LANGUAGE);
                    });
                }

                @Override
                public void onFailure(Call<Language> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewLanguageMode(false);
                        resetTextFields(LANGUAGE);
                    });
                }
            });

        } else { // Modificar

            if (!validateTextFields(LANGUAGE)) {
                return;
            }

            if (languageSelected.getId() == 0) {
                Alerts.showErrorAlert("Debes seleccionar un item de la lista");
                return;
            }

            Language newLanguage =  new Language();
            newLanguage.setId(languageSelected.getId());
            newLanguage.setName(tfLanguageName.getText());

            Call<Language> languageCall = languageService.modifyLanguage(newLanguage.getId(), newLanguage);

            languageCall.enqueue(new Callback<Language>() {
                @Override
                public void onResponse(Call<Language> call, Response<Language> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Idioma monificado correctamente: " + new Gson().toJson(response.body()));
                        getAllLanguages();
                        activateEditLanguageMode(false);
                        resetTextFields(LANGUAGE);
                    });
                }

                @Override
                public void onFailure(Call<Language> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditLanguageMode(false);
                        resetTextFields(LANGUAGE);
                    });
                }
            });

        }

    }

    @FXML
    public void getLanguageFromListView(Event event) {
        languageSelected = (Language) lvLanguages.getSelectionModel().getSelectedItem();
        System.out.println(languageSelected.getName());
        if(!validateItemSelectedFromListView(languageSelected)) {
            return;
        }
        tfLanguageName.setText(languageSelected.getName());
    }

    @FXML
    public void activateNewLanguage(Event event) {
        action = Action.NEW;
        activateNewLanguageMode(true);
    }

    @FXML
    public void activateModifyLanguage(Event event) {
        action = Action.MODIFY;
        activateEditLanguageMode(true);
    }

    @FXML
    public void deleteLanguage(Event event) {

        if(lvLanguages.isDisable()) {
            activateDeleteLanguageMode(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }

        if (!validateTextFields(LANGUAGE)) {
            return;
        }

        if (languageSelected.getId() == 0) {
            Alerts.showErrorAlert("Debes seleccionar un item de la lista");
            return;
        }

        Call<ResponseBody> callDelete = languageService.deleteLanguage(languageSelected.getId());

        callDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    Alerts.showInfoAlert("Idioma eliminado correctamente");
                    getAllLanguages();
                    resetTextFields(LANGUAGE);
                    activateEditLanguageMode(false);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    resetTextFields(LANGUAGE);
                    activateEditLanguageMode(false);
                });
            }
        });

    }

    @FXML
    public void cancelActionLanguage(Event event) {
        activateEditLanguageMode(false);
        resetTextFields(LANGUAGE);
    }

    private void getAllLanguages() {
        lvLanguages.getItems().clear();
        languageService.getAllLanguages()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de idiomaas cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de idiomas"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(language -> {
                    Platform.runLater(() -> {
                        listLanguages.add(language);
                    });
                });
    }

    private void activateNewLanguageMode(boolean mode) {
        btModifyLanguage.setDisable(mode);
        btDeleteLanguage.setDisable(mode);
        btNewLanguage.setDisable(mode);

        btCancelLanguage.setDisable(!mode);
        btSaveNewLanguage.setDisable(!mode);

        tfLanguageName.setDisable(!mode);
    }

    private void activateEditLanguageMode(boolean mode) {
        btModifyLanguage.setDisable(mode);
        btDeleteLanguage.setDisable(mode);
        btNewLanguage.setDisable(mode);

        btCancelLanguage.setDisable(!mode);
        btSaveNewLanguage.setDisable(!mode);

        tfLanguageName.setDisable(!mode);

        lvLanguages.setDisable(!mode);
    }

    private void activateDeleteLanguageMode(boolean mode) {
        btModifyLanguage.setDisable(mode);
        btNewLanguage.setDisable(mode);
        btSaveNewLanguage.setDisable(mode);

        btCancelLanguage.setDisable(!mode);
        btDeleteLanguage.setDisable(!mode);

        tfLanguageName.setDisable(mode);


        lvLanguages.setDisable(!mode);
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /** TELEFONOS DE EMERGENCIA --------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** VACUNAS ------------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** MONEDAS ------------------------------------------------------------------------------------------------------*/
    @FXML
    public void addNewCoin(Event event) {
        if (action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFields(COIN)) {
                return;
            }

            Coin newCoin = new Coin();
            newCoin.setIsoCode(tfCodeISOCoin.getText());
            newCoin.setMonetaryUnit(tfMonetaryUnitCoin.getText());
            newCoin.setSymbol(tfSymbolCoin.getText());

            Call<Coin> coinCall = coinService.addNewCoin(newCoin);
            coinCall.enqueue(new Callback<Coin>() {
                @Override
                public void onResponse(Call<Coin> call, Response<Coin> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nueva moneda añadida correctamente: " + new Gson().toJson(response.body()));
                        getAllCoins();
                        activateNewCoinMode(false);
                        resetTextFields(COIN);
                    });
                }

                @Override
                public void onFailure(Call<Coin> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewCoinMode(false);
                        resetTextFields(COIN);
                    });
                }
            });

        } else { // Modificar

            if(!validateTextFields(COIN)) {
                return;
            }

            if (coinSelected.getId() == 0) {
                Alerts.showErrorAlert("Debes seleccionar un item de la lista");
                return;
            }

            Coin newCoin = new Coin();
            newCoin.setId(coinSelected.getId());
            newCoin.setIsoCode(tfCodeISOCoin.getText());
            newCoin.setMonetaryUnit(tfMonetaryUnitCoin.getText());
            newCoin.setSymbol(tfSymbolCoin.getText());

            Call<Coin> coinCall = coinService.modifyCoin(newCoin.getId(), newCoin);
            coinCall.enqueue(new Callback<Coin>() {
                @Override
                public void onResponse(Call<Coin> call, Response<Coin> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Moneda monificada correctamente: " + new Gson().toJson(response.body()));
                        getAllCoins();
                        activateEditCoinMode(false);
                        resetTextFields(COIN);
                    });
                }

                @Override
                public void onFailure(Call<Coin> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditCoinMode(false);
                        resetTextFields(COIN);
                    });
                }
            });

        }
    }

    @FXML
    public void getCoinFromListView(Event event) {
        coinSelected = (Coin) lvCoins.getSelectionModel().getSelectedItem();
        if (!validateItemSelectedFromListView(coinSelected)) {
            return;
        }
        tfCodeISOCoin.setText(coinSelected.getIsoCode());
        tfMonetaryUnitCoin.setText(coinSelected.getMonetaryUnit());
        tfSymbolCoin.setText(coinSelected.getSymbol());
    }

    @FXML
    public void activateNewCoin(Event event) {
        action = Action.NEW;
        activateNewCoinMode(true);
    }

    @FXML
    public void activateModifyCoin(Event event) {
        action = Action.MODIFY;
        activateEditCoinMode(true);
    }

    @FXML
    public void deleteCoin(Event event) {

        if(lvCoins.isDisable()) {
            activateDeleteCoinMode(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }

        if (!validateTextFields(COIN)) {
            return;
        }

        if (coinSelected.getId() == 0) {
            Alerts.showErrorAlert("Debes seleccionar un item de la lista");
            return;
        }

        Call<ResponseBody> callDelete = coinService.deleteCoin(coinSelected.getId());
        callDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    Alerts.showInfoAlert("Moneda eliminada correctamente");
                    getAllCoins();
                    resetTextFields(COIN);
                    activateEditCoinMode(false);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    getAllCoins();
                    resetTextFields(COIN);
                    activateEditCoinMode(false);
                });
            }
        });

    }

    @FXML
    public void cancelActionCoin(Event event) {
        activateEditCoinMode(false);
        resetTextFields(COIN);
    }

    /** Devuelve todas las monedas de la base de datos */
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

    private void activateNewCoinMode(boolean mode) {
        btModifyCoin.setDisable(mode);
        btDeleteCoin.setDisable(mode);
        btNewCoin.setDisable(mode);

        btCancelCoin.setDisable(!mode);
        btSaveNewCoin.setDisable(!mode);

        tfSymbolCoin.setDisable(!mode);
        tfMonetaryUnitCoin.setDisable(!mode);
        tfCodeISOCoin.setDisable(!mode);

    }

    private void activateEditCoinMode(boolean mode) {
        btModifyCoin.setDisable(mode);
        btDeleteCoin.setDisable(mode);
        btNewCoin.setDisable(mode);

        btCancelCoin.setDisable(!mode);
        btSaveNewCoin.setDisable(!mode);

        tfSymbolCoin.setDisable(!mode);
        tfMonetaryUnitCoin.setDisable(!mode);
        tfCodeISOCoin.setDisable(!mode);

        lvCoins.setDisable(!mode);
    }

    private void activateDeleteCoinMode(boolean mode) {
        btModifyCoin.setDisable(mode);
        btNewCoin.setDisable(mode);
        btSaveNewCoin.setDisable(mode);

        btCancelCoin.setDisable(!mode);
        btDeleteCoin.setDisable(!mode);

        tfSymbolCoin.setDisable(mode);
        tfMonetaryUnitCoin.setDisable(mode);
        tfCodeISOCoin.setDisable(mode);

        lvCoins.setDisable(!mode);
    }


    /** --------------------------------------------------------------------------------------------------------------*/

    /** PAISES -------------------------------------------------------------------------------------------------------*/

    /** --------------------------------------------------------------------------------------------------------------*/

    /** ELECTRICIDAD -------------------------------------------------------------------------------------------------*/
    @FXML
    public void addNewElectricity(Event event) {

        if (action.equals(Action.NEW)) { // Nuevo

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
                        resetTextFields(ELECTRICITY);
                    });

                }

                @Override
                public void onFailure(Call<Electricity> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewElectricityMode(false);
                        resetTextFields(ELECTRICITY);
                    });
                }
            });

        } else { // Modificar

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
                        activateEditElectricityMode(false);
                        resetTextFields(ELECTRICITY);
                    });
                }

                @Override
                public void onFailure(Call<Electricity> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditElectricityMode(false);
                        resetTextFields(ELECTRICITY);
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
        activateEditElectricityMode(true);
    }

    @FXML
    public void deleteElectricity(Event event) {
        if (lvElectricity.isDisable()) {
            lvElectricity.setDisable(false);
            tfVoltage.setDisable(true);
            tfFrecuency.setDisable(true);
            activateDeleteElectricityMode(true);
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
                    resetTextFields(ELECTRICITY);
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
                    resetTextFields(ELECTRICITY);
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
        activateEditElectricityMode(false);
        resetTextFields(ELECTRICITY);
    }

    /** Devuelve todas las electricidades de la base de datos */
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

    private void activateDeleteElectricityMode(boolean mode) {
        btCancelElectricity.setDisable(!mode);
        btModifyElectricity.setDisable(mode);
        btSaveNewElectricity.setDisable(mode);
        btNewElectricity.setDisable(mode);
    }


    private void activateEditElectricityMode(boolean mode) {
        activateNewElectricityMode(mode);
        lvElectricity.setDisable(!mode);
        btDeleteElectricity.setDisable(mode);
        tfFrecuency.setDisable(!mode);
        tfVoltage.setDisable(!mode);
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /**
     * Método general que valida si se ha seleccionado un item del list view
     * @param item
     * @return
     */
    private boolean validateItemSelectedFromListView(Object item) {
        if (item == null) {
            Alerts.showErrorAlert("No has seleccionado ningún elemento del listado");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Metodo que establece la vista inicial de botones, listas y cajas de texto
     * @param mode
     */
    private void initialViewMode(boolean mode) {
        btSaveNewElectricity.setDisable(mode);
        btCancelElectricity.setDisable(mode);
        btSaveNewCoin.setDisable(mode);
        btCancelCoin.setDisable(mode);
        btSaveNewLanguage.setDisable(mode);
        btCancelLanguage.setDisable(mode);

        lvElectricity.setDisable(mode);
        lvCoins.setDisable(mode);
        lvLanguages.setDisable(mode);

        tfFrecuency.setDisable(mode);
        tfVoltage.setDisable(mode);
        tfCodeISOCoin.setDisable(mode);
        tfSymbolCoin.setDisable(mode);
        tfMonetaryUnitCoin.setDisable(mode);
        tfLanguageName.setDisable(mode);
    }

    /**
     * Metodo general que comprueba los datos de las cajas de texto
     * @param type
     * @return
     */
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

            case LANGUAGE:

                if(tfLanguageName.getText().isBlank()) {
                    Alerts.showErrorAlert("No puedes dejar el nombre del lenguaje en blanco");
                    value = false;
                } else {
                    value = true;
                }

                break;

        }

        return value;
    }


    /**
     * Metodo general que resetea las cajas de texto
     * @param type
     */
    private void resetTextFields(String type) {
        switch (type) {

            case ELECTRICITY:
                tfFrecuency.setText("");
                tfVoltage.setText("");
                break;

            case COIN:
                tfCodeISOCoin.setText("");
                tfMonetaryUnitCoin.setText("");
                tfSymbolCoin.setText("");
                break;

            case LANGUAGE:
                tfLanguageName.setText("");
                break;
        }
    }

}
