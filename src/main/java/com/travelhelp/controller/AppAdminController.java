package com.travelhelp.controller;


import com.google.gson.Gson;
import com.travelhelp.domain.*;
import com.travelhelp.domain.dto.CityDTO;
import com.travelhelp.domain.dto.EmergencyPhoneDTO;
import com.travelhelp.service.city.CityService;
import com.travelhelp.service.coin.CoinService;
import com.travelhelp.service.country.CountryService;
import com.travelhelp.service.electricity.ElectricityService;
import com.travelhelp.service.emergencyPhone.EmergencyPhoneService;
import com.travelhelp.service.language.LanguageService;
import com.travelhelp.service.plug.PlugService;
import com.travelhelp.service.vaccine.VaccineService;
import com.travelhelp.utils.Action;
import com.travelhelp.utils.Alerts;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event; // NO USAR ACTION EVENT, DA ERROR Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: argument type mismatch
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
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
            tfLanguageName,
            tfNameVaccine,tfEffectivityVaccine,tfDurabilityVaccine,
            tfPhoneNumberEmergencyPhone,tfServiceEmergencyPhone,
            tfImageUrlPlug,
            tfNameCity,tfExtensionCity,tfNumberOfHabitantsCity;
    public ListView lvElectricity,lvCoins,lvLanguages,lvVaccines,lvEmergencyPhones,lvPlug,lvCities;
    public Button
            btNewElectricity,btSaveNewElectricity,btModifyElectricity,btCancelElectricity, btDeleteElectricity,
            btNewCoin,btSaveNewCoin,btModifyCoin,btCancelCoin,btDeleteCoin,
            btNewLanguage,btSaveNewLanguage,btModifyLanguage,btCancelLanguage,btDeleteLanguage,
            btNewVaccine,btSaveNewVaccine,btModifyVaccine,btCancelVaccine,btDeleteVaccine,
            btNewEmergencyPhone,btSaveNewEmergencyPhone,btModifyEmergencyPhone,btCancelEmergencyPhone,btDeleteEmergencyPhone,
            btNewPlug,btSaveNewPlug,btModifyPlug,btCancelPlug,btDeletePlug,
            btNewCity,btSaveNewCity,btModifyCity,btCancelCity,btDeleteCity;
    public ComboBox<Country> cbCountry,cbCountriesCity;
    public ComboBox<Character> cbPlugType;
    public WebView wbTypePlugImage;



    private Action action;

    /** Items selected from List View */
    private Electricity electricitySelected;
    private Coin coinSelected;
    private Language languageSelected;
    private Vaccine vaccineSelected;
    private EmergencyPhone emergencyPhoneSelected;
    private Country countrySelected;
    private Plug plugSelected;
    private City citySelected;

    /** Services */
    private ElectricityService electricityService;
    private CoinService coinService;
    private LanguageService languageService;
    private VaccineService vaccineService;
    private EmergencyPhoneService emergencyPhoneService;
    private CountryService countryService;
    private PlugService plugService;
    private CityService cityService;


    /** Observables List */
    private ObservableList<Electricity> listElectricities;
    private ObservableList<Coin> listCoins;
    private ObservableList<Language> listLanguages;
    private ObservableList<Vaccine> listVaccines;
    private ObservableList<EmergencyPhone> listEmergencyPhones;
    private ObservableList<Plug> listPlugs;
    private ObservableList<City> listCities;
    private ObservableList<Country> listComboBoxCountries;
    private ObservableList<Character> listComboBoxPlugTypes;
    private ObservableList<Country> listComboBoxCountriesCity;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        electricityService = new ElectricityService();
        coinService = new CoinService();
        languageService = new LanguageService();
        vaccineService = new VaccineService();
        emergencyPhoneService = new EmergencyPhoneService();
        countryService = new CountryService();
        plugService = new PlugService();
        cityService = new CityService();

        listElectricities = FXCollections.observableArrayList();
        lvElectricity.setItems(listElectricities);
        getAllElectricities();

        listCoins = FXCollections.observableArrayList();
        lvCoins.setItems(listCoins);
        getAllCoins();

        listLanguages = FXCollections.observableArrayList();
        lvLanguages.setItems(listLanguages);
        getAllLanguages();

        listVaccines = FXCollections.observableArrayList();
        lvVaccines.setItems(listVaccines);
        getAllVaccines();

        listEmergencyPhones = FXCollections.observableArrayList();
        lvEmergencyPhones.setItems(listEmergencyPhones);
        getAllEmergencyPhones();

        listPlugs = FXCollections.observableArrayList();
        lvPlug.setItems(listPlugs);
        getAllPlugs();

        listCities = FXCollections.observableArrayList();
        lvCities.setItems(listCities);
        getAllCities();

        listComboBoxCountries = FXCollections.observableArrayList();
        listComboBoxCountriesCity = FXCollections.observableArrayList();
        cbCountry.setItems(listComboBoxCountries);
        cbCountriesCity.setItems(listComboBoxCountriesCity);
        loadComboBoxCountries();

        listComboBoxPlugTypes = FXCollections.observableArrayList();
        for(Character c : PLUG_LIST) {
            listComboBoxPlugTypes.add(c);
        }
        cbPlugType.setItems(listComboBoxPlugTypes);

        initialViewMode(true); // Botones Modificar, cancelar y eliminar desabilitados así como listViews
    }
    /** ENCHUFES -----------------------------------------------------------------------------------------------------*/

    @FXML
    public void addNewPlug(Event event) {

        if(action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFieldsAndComboBox(PLUG)) {
                return;
            }

            Plug newPlug = new Plug();
            newPlug.setImage(tfImageUrlPlug.getText());
            newPlug.setType(cbPlugType.getValue());

            Call<Plug> plugCall = plugService.addNewPlug(newPlug);

            plugCall.enqueue(new Callback<Plug>() {
                @Override
                public void onResponse(Call<Plug> call, Response<Plug> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Enchufe añadido correctamente: " + new Gson().toJson(response.body()));
                        getAllPlugs();
                        activateNewPlugMode(false);
                        resetTextFieldsComboBoxAndWebViews(PLUG);
                    });
                }

                @Override
                public void onFailure(Call<Plug> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewPlugMode(false);
                        resetTextFieldsComboBoxAndWebViews(PLUG);
                    });
                }
            });

        } else { // Modificar

            if (!validateTextFieldsAndComboBox(PLUG)) {
                return;
            }

            if (plugSelected.getId() == 0) {
                Alerts.showErrorAlert("Debes seleccionar un item de la lista");
                return;
            }

            Plug newPlug = new Plug();
            newPlug.setId(plugSelected.getId());
            newPlug.setImage(tfImageUrlPlug.getText());
            newPlug.setType(cbPlugType.getValue());

            Call<Plug> plugCall = plugService.modifyPlug(newPlug.getId(), newPlug);

            plugCall.enqueue(new Callback<Plug>() {
                @Override
                public void onResponse(Call<Plug> call, Response<Plug> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Enchufe modificado correctamente: " + new Gson().toJson(response.body()));
                        getAllPlugs();
                        activateEditPlugMode(false);
                        resetTextFieldsComboBoxAndWebViews(PLUG);
                    });
                }

                @Override
                public void onFailure(Call<Plug> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditPlugMode(false);
                        resetTextFieldsComboBoxAndWebViews(PLUG);
                    });
                }
            });

        }

    }

    @FXML
    public void getPlugFromListView(Event event) {
        plugSelected = (Plug) lvPlug.getSelectionModel().getSelectedItem();
        if(!validateItemSelectedFromListView(plugSelected)) {
            return;
        }
        tfImageUrlPlug.setText(plugSelected.getImage());
        cbPlugType.setValue(plugSelected.getType());
        wbTypePlugImage.getEngine().load(plugSelected.getImage());
    }

    @FXML
    public void activateNewPlug(Event event) {
        action = Action.NEW;
        activateNewPlugMode(true);
    }

    @FXML
    public void activateModifyPlug(Event event) {
        action = Action.MODIFY;
        activateEditPlugMode(true);
    }

    @FXML
    public void deletePlug(Event event) {

        if(lvPlug.isDisable()) {
            activateDeletePlugMode(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }

        if (!validateTextFieldsAndComboBox(PLUG)) {
            return;
        }

        if (plugSelected.getId() == 0) {
            Alerts.showErrorAlert("Debes seleccionar un item de la lista");
            return;
        }

        Call<ResponseBody> callDelete = plugService.deletePlug(plugSelected.getId());

        callDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    Alerts.showInfoAlert("Enchufe eliminado correctamente");
                    getAllPlugs();
                    resetTextFieldsComboBoxAndWebViews(PLUG);
                    activateEditPlugMode(false);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    resetTextFieldsComboBoxAndWebViews(PLUG);
                    activateEditPlugMode(false);
                });
            }
        });

    }

    @FXML
    public void cancelActionPlug(Event event) {
        activateEditPlugMode(false);
        resetTextFieldsComboBoxAndWebViews(PLUG);
    }

    private void getAllPlugs() {
        lvPlug.getItems().clear();
        plugService.getAllPlugs()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de enchufes cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de enchufes"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(plug -> {
                    Platform.runLater(() -> {
                        listPlugs.add(plug);
                    });
                });
    }

    private void activateNewPlugMode(boolean mode) {
        btModifyPlug.setDisable(mode);
        btDeletePlug.setDisable(mode);
        btNewPlug.setDisable(mode);

        btCancelPlug.setDisable(!mode);
        btSaveNewPlug.setDisable(!mode);

        tfImageUrlPlug.setDisable(!mode);
        cbPlugType.setDisable(!mode);

        wbTypePlugImage.setVisible(mode);

    }

    private void activateEditPlugMode(boolean mode) {
        btModifyPlug.setDisable(mode);
        btDeletePlug.setDisable(mode);
        btNewPlug.setDisable(mode);

        btCancelPlug.setDisable(!mode);
        btSaveNewPlug.setDisable(!mode);

        tfImageUrlPlug.setDisable(!mode);
        cbPlugType.setDisable(!mode);

        wbTypePlugImage.setVisible(mode);

        lvPlug.setDisable(!mode);
    }

    private void activateDeletePlugMode(boolean mode) {
        btModifyPlug.setDisable(mode);
        btNewPlug.setDisable(mode);
        btSaveNewPlug.setDisable(mode);

        btCancelPlug.setDisable(!mode);
        btDeletePlug.setDisable(!mode);

        tfImageUrlPlug.setDisable(mode);
        cbPlugType.setDisable(mode);

        wbTypePlugImage.setVisible(mode);

        lvPlug.setDisable(!mode);
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /** CIUDADES -----------------------------------------------------------------------------------------------------*/

    @FXML
    public void addNewCity(Event event) {

        if(action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFieldsAndComboBox(CITY)) {
                return;
            }

            Country countryComboBox = (Country) cbCountriesCity.getSelectionModel().getSelectedItem();
            CityDTO newCityDTO = new CityDTO();
            newCityDTO.setIdCountry(countryComboBox.getId());
            newCityDTO.setName(tfNameCity.getText());
            newCityDTO.setNumberOfHabitants(Integer.parseInt(tfNumberOfHabitantsCity.getText()));
            newCityDTO.setExtension(Float.parseFloat(tfExtensionCity.getText()));

            Call<City> cityCall = cityService.addNewCity(newCityDTO);

            cityCall.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nueva ciudad creada correctamente: " + new Gson().toJson(response.body()));
                        getAllCities();
                        activateNewCityMode(false);
                        resetTextFieldsComboBoxAndWebViews(CITY);
                    });
                }

                @Override
                public void onFailure(Call<City> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewCityMode(false);
                        resetTextFieldsComboBoxAndWebViews(CITY);
                    });
                }
            });


        } else { // Modificar

            if (!validateTextFieldsAndComboBox(CITY)) {
                return;
            }

            if (citySelected.getId() == 0) {
                Alerts.showErrorAlert("Debes seleccionar un item de la lista");
                return;
            }

            Country countryComboBox = (Country) cbCountriesCity.getSelectionModel().getSelectedItem();
            CityDTO newCityDTO = new CityDTO();
            newCityDTO.setIdCountry(countryComboBox.getId());
            newCityDTO.setId(citySelected.getId());
            newCityDTO.setName(tfNameCity.getText());
            newCityDTO.setNumberOfHabitants(Integer.parseInt(tfNumberOfHabitantsCity.getText()));
            newCityDTO.setExtension(Float.parseFloat(tfExtensionCity.getText()));

            Call<City> cityCall = cityService.modifyCity(newCityDTO.getId(), newCityDTO);

            cityCall.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nueva ciudad modificada correctamente: " + new Gson().toJson(response.body()));
                        getAllCities();
                        activateEditCityMode(false);
                        resetTextFieldsComboBoxAndWebViews(CITY);
                    });
                }

                @Override
                public void onFailure(Call<City> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditCityMode(false);
                        resetTextFieldsComboBoxAndWebViews(CITY);
                    });
                }
            });

        }

    }

    @FXML
    public void getCityFromListView(Event event) {
        citySelected = (City) lvCities.getSelectionModel().getSelectedItem();
        if(!validateItemSelectedFromListView(citySelected)) {
            return;
        }
        tfExtensionCity.setText(String.valueOf(citySelected.getExtension()));
        tfNameCity.setText(citySelected.getName());
        tfNumberOfHabitantsCity.setText(String.valueOf(citySelected.getNumberOfHabitants()));
        cbCountriesCity.setValue(citySelected.getCountry());
    }

    @FXML
    public void activateNewCity(Event event) {
        action = Action.NEW;
        activateNewCityMode(true);
    }

    @FXML
    public void activateModifyCity(Event event) {
        action = Action.MODIFY;
        activateEditCityMode(true);
    }

    @FXML
    public void deleteCity(Event event) {

        if(lvCities.isDisable()) {
            activateDeleteCityMode(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }

        if (!validateTextFieldsAndComboBox(CITY)) {
            return;
        }

        if (citySelected.getId() == 0) {
            Alerts.showErrorAlert("Debes seleccionar un item de la lista");
            return;
        }

        Call<ResponseBody> cityCallDelete = cityService.deleteCity(citySelected.getId());

        cityCallDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    Alerts.showInfoAlert("Ciudad eliminada correctamente");
                    getAllCities();
                    activateDeleteCityMode(false);
                    resetTextFieldsComboBoxAndWebViews(CITY);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    activateDeleteCityMode(false);
                    resetTextFieldsComboBoxAndWebViews(CITY);
                });
            }
        });
    }

    @FXML
    public void cancelActionCity(Event event) {
        activateEditCityMode(false);
        resetTextFieldsComboBoxAndWebViews(CITY);
    }

    private void getAllCities() {
        lvCities.getItems().clear();
        cityService.getAllCities()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de ciudades cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de ciudades"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(city -> {
                    Platform.runLater(() -> {
                        listCities.add(city);
                    });
                });
    }

    private void activateNewCityMode(boolean mode) {
        btModifyCity.setDisable(mode);
        btDeleteCity.setDisable(mode);
        btNewCity.setDisable(mode);

        btCancelCity.setDisable(!mode);
        btSaveNewCity.setDisable(!mode);

        tfNameCity.setDisable(!mode);
        tfExtensionCity.setDisable(!mode);
        tfNumberOfHabitantsCity.setDisable(!mode);

        cbCountriesCity.setDisable(!mode);

    }

    private void activateEditCityMode(boolean mode) {
        btModifyCity.setDisable(mode);
        btDeleteCity.setDisable(mode);
        btNewCity.setDisable(mode);

        btCancelCity.setDisable(!mode);
        btSaveNewCity.setDisable(!mode);

        tfNameCity.setDisable(!mode);
        tfExtensionCity.setDisable(!mode);
        tfNumberOfHabitantsCity.setDisable(!mode);

        cbCountriesCity.setDisable(!mode);

        lvCities.setDisable(!mode);
    }

    private void activateDeleteCityMode(boolean mode) {
        btModifyCity.setDisable(mode);
        btNewCity.setDisable(mode);
        btSaveNewCity.setDisable(mode);

        btCancelCity.setDisable(!mode);
        btDeleteCity.setDisable(!mode);

        tfExtensionCity.setDisable(mode);
        tfNameCity.setDisable(mode);
        tfNumberOfHabitantsCity.setDisable(mode);

        cbCountriesCity.setDisable(mode);

        lvCities.setDisable(!mode);
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /** IDIOMAS ------------------------------------------------------------------------------------------------------*/

    @FXML
    public void addNewLanguage(Event event) {

        if(action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFieldsAndComboBox(LANGUAGE)) {
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
                        resetTextFieldsComboBoxAndWebViews(LANGUAGE);
                    });
                }

                @Override
                public void onFailure(Call<Language> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewLanguageMode(false);
                        resetTextFieldsComboBoxAndWebViews(LANGUAGE);
                    });
                }
            });

        } else { // Modificar

            if (!validateTextFieldsAndComboBox(LANGUAGE)) {
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
                        resetTextFieldsComboBoxAndWebViews(LANGUAGE);
                    });
                }

                @Override
                public void onFailure(Call<Language> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditLanguageMode(false);
                        resetTextFieldsComboBoxAndWebViews(LANGUAGE);
                    });
                }
            });

        }

    }

    @FXML
    public void getLanguageFromListView(Event event) {
        languageSelected = (Language) lvLanguages.getSelectionModel().getSelectedItem();
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

        if (!validateTextFieldsAndComboBox(LANGUAGE)) {
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
                    resetTextFieldsComboBoxAndWebViews(LANGUAGE);
                    activateEditLanguageMode(false);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    resetTextFieldsComboBoxAndWebViews(LANGUAGE);
                    activateEditLanguageMode(false);
                });
            }
        });

    }

    @FXML
    public void cancelActionLanguage(Event event) {
        activateEditLanguageMode(false);
        resetTextFieldsComboBoxAndWebViews(LANGUAGE);
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

    @FXML
    public void addNewEmergencyPhone(Event event) {

        if(action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFieldsAndComboBox(EMERGENCYPHONE)) {
                return;
            }

            Country countryComboBox = (Country) cbCountry.getSelectionModel().getSelectedItem();
            countryComboBox.setId(countryComboBox.getId());

            EmergencyPhoneDTO newEmergencyPhoneDTO = new EmergencyPhoneDTO();
            newEmergencyPhoneDTO.setIdCountry(countryComboBox.getId());
            newEmergencyPhoneDTO.setPhoneNumber(tfPhoneNumberEmergencyPhone.getText());
            newEmergencyPhoneDTO.setService(tfServiceEmergencyPhone.getText());

            Call<EmergencyPhone> emergencyPhoneCall = emergencyPhoneService.addNewEmergencyPhone(newEmergencyPhoneDTO);

            emergencyPhoneCall.enqueue(new Callback<EmergencyPhone>() {
                @Override
                public void onResponse(Call<EmergencyPhone> call, Response<EmergencyPhone> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nuevo teléfono de emergencia creado correctamente: " + new Gson().toJson(response.body()));
                        getAllEmergencyPhones();
                        activateNewEmergencyPhoneMode(false);
                        resetTextFieldsComboBoxAndWebViews(EMERGENCYPHONE);
                    });
                }

                @Override
                public void onFailure(Call<EmergencyPhone> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewEmergencyPhoneMode(false);
                        resetTextFieldsComboBoxAndWebViews(EMERGENCYPHONE);
                    });
                }
            });

        } else { // Modificar

            if (!validateTextFieldsAndComboBox(EMERGENCYPHONE)) {
                return;
            }

            if (emergencyPhoneSelected.getId() == 0) {
                Alerts.showErrorAlert("Debes seleccionar un item de la lista");
                return;
            }

            Country countryComboBox = (Country) cbCountry.getSelectionModel().getSelectedItem();
            countryComboBox.setId(countryComboBox.getId());

            EmergencyPhoneDTO newEmergencyPhoneDTO = new EmergencyPhoneDTO();
            newEmergencyPhoneDTO.setIdCountry(countryComboBox.getId());
            newEmergencyPhoneDTO.setId(emergencyPhoneSelected.getId());
            newEmergencyPhoneDTO.setPhoneNumber(tfPhoneNumberEmergencyPhone.getText());
            newEmergencyPhoneDTO.setService(tfServiceEmergencyPhone.getText());

            Call<EmergencyPhone> emergencyPhoneCall = emergencyPhoneService.modifyEmergencyPhone(newEmergencyPhoneDTO.getId(), newEmergencyPhoneDTO);

            emergencyPhoneCall.enqueue(new Callback<EmergencyPhone>() {
                @Override
                public void onResponse(Call<EmergencyPhone> call, Response<EmergencyPhone> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nuevo teléfono de emergencia modificado correctamente: " + new Gson().toJson(response.body()));
                        getAllEmergencyPhones();
                        activateNewEmergencyPhoneMode(false);
                        resetTextFieldsComboBoxAndWebViews(EMERGENCYPHONE);
                    });
                }

                @Override
                public void onFailure(Call<EmergencyPhone> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewEmergencyPhoneMode(false);
                        resetTextFieldsComboBoxAndWebViews(EMERGENCYPHONE);
                    });
                }
            });

        }

    }

    @FXML
    public void getEmergencyPhoneFromListView(Event event) {
        emergencyPhoneSelected = (EmergencyPhone) lvEmergencyPhones.getSelectionModel().getSelectedItem();
        if(!validateItemSelectedFromListView(emergencyPhoneSelected)) {
            return;
        }
        tfPhoneNumberEmergencyPhone.setText(emergencyPhoneSelected.getPhoneNumber());
        tfServiceEmergencyPhone.setText(emergencyPhoneSelected.getService());
        cbCountry.setValue(emergencyPhoneSelected.getCountry());
    }

    @FXML
    public void activateNewEmergencyPhone(Event event) {
        action = Action.NEW;
        activateNewEmergencyPhoneMode(true);
    }

    @FXML
    public void activateModifyEmergencyPhone(Event event) {
        action = Action.MODIFY;
        activateEditEmergencyPhoneMode(true);
    }

    @FXML
    public void deleteEmergencyPhone(Event event) {

        if(lvEmergencyPhones.isDisable()) {
            activateDeleteEmergencyPhoneMode(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }

        if (!validateTextFieldsAndComboBox(EMERGENCYPHONE)) {
            return;
        }

        if (emergencyPhoneSelected.getId() == 0) {
            Alerts.showErrorAlert("Debes seleccionar un item de la lista");
            return;
        }

        Call<ResponseBody> callDelete = emergencyPhoneService.deleteEmergencyPhone(emergencyPhoneSelected.getId());

        callDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    Alerts.showInfoAlert("Teléfono de emergencia eliminado correctamente");
                    getAllEmergencyPhones();
                    resetTextFieldsComboBoxAndWebViews(EMERGENCYPHONE);
                    activateEditEmergencyPhoneMode(false);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    resetTextFieldsComboBoxAndWebViews(EMERGENCYPHONE);
                    activateEditEmergencyPhoneMode(false);
                });
            }
        });

    }

    @FXML
    public void cancelActionEmergencyPhone(Event event) {
        activateEditEmergencyPhoneMode(false);
        resetTextFieldsComboBoxAndWebViews(EMERGENCYPHONE);
    }

    private void getAllEmergencyPhones() {
        lvEmergencyPhones.getItems().clear();
        emergencyPhoneService.getAllEmergencyPhones()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de teléfonos de emergencia cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de teléfonos de emergencia"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(emergencyPhone -> {
                    Platform.runLater(() -> {
                        listEmergencyPhones.add(emergencyPhone);
                    });
                });
    }

    private void activateNewEmergencyPhoneMode(boolean mode) {
        btModifyEmergencyPhone.setDisable(mode);
        btDeleteEmergencyPhone.setDisable(mode);
        btNewEmergencyPhone.setDisable(mode);

        btCancelEmergencyPhone.setDisable(!mode);
        btSaveNewEmergencyPhone.setDisable(!mode);
        cbCountry.setDisable(!mode);

        tfPhoneNumberEmergencyPhone.setDisable(!mode);
        tfServiceEmergencyPhone.setDisable(!mode);
    }

    private void activateEditEmergencyPhoneMode(boolean mode) {
        btModifyEmergencyPhone.setDisable(mode);
        btDeleteEmergencyPhone.setDisable(mode);
        btNewEmergencyPhone.setDisable(mode);

        btCancelEmergencyPhone.setDisable(!mode);
        btSaveNewEmergencyPhone.setDisable(!mode);
        cbCountry.setDisable(!mode);

        tfPhoneNumberEmergencyPhone.setDisable(!mode);
        tfServiceEmergencyPhone.setDisable(!mode);

        lvEmergencyPhones.setDisable(!mode);
    }

    private void activateDeleteEmergencyPhoneMode(boolean mode) {
        btModifyEmergencyPhone.setDisable(mode);
        btNewEmergencyPhone.setDisable(mode);
        btSaveNewEmergencyPhone.setDisable(mode);

        btCancelEmergencyPhone.setDisable(!mode);
        btDeleteEmergencyPhone.setDisable(!mode);
        cbCountry.setDisable(!mode);

        tfPhoneNumberEmergencyPhone.setDisable(mode);
        tfServiceEmergencyPhone.setDisable(mode);


        lvEmergencyPhones.setDisable(!mode);
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /** VACUNAS ------------------------------------------------------------------------------------------------------*/

    @FXML
    public void addNewVaccine(Event event) {

        if (action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFieldsAndComboBox(VACCINE)) {
                return;
            }

            Vaccine newVaccine = new Vaccine();
            newVaccine.setDurability(Integer.parseInt(tfDurabilityVaccine.getText()));
            newVaccine.setEffectivity(Double.parseDouble(tfEffectivityVaccine.getText()));
            newVaccine.setName(tfNameVaccine.getText());

            Call<Vaccine> vaccineCall = vaccineService.addNewVaccine(newVaccine);

            vaccineCall.enqueue(new Callback<Vaccine>() {
                @Override
                public void onResponse(Call<Vaccine> call, Response<Vaccine> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Nueva vacuna añadida correctamente: " + new Gson().toJson(response.body()));
                        getAllVaccines();
                        activateNewVaccineMode(false);
                        resetTextFieldsComboBoxAndWebViews(VACCINE);
                    });
                }

                @Override
                public void onFailure(Call<Vaccine> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewVaccineMode(false);
                        resetTextFieldsComboBoxAndWebViews(VACCINE);
                    });
                }
            });

        } else { // Modificar

            if(!validateTextFieldsAndComboBox(VACCINE)) {
                return;
            }

            if (vaccineSelected.getId() == 0) {
                Alerts.showErrorAlert("Debes seleccionar un item de la lista");
                return;
            }

            Vaccine newVaccine = new Vaccine();
            newVaccine.setId(vaccineSelected.getId());
            newVaccine.setEffectivity(Double.parseDouble(tfEffectivityVaccine.getText()));
            newVaccine.setDurability(Integer.parseInt(tfDurabilityVaccine.getText()));
            newVaccine.setName(tfNameVaccine.getText());

            Call<Vaccine> vaccineCall = vaccineService.modifyVaccine(newVaccine.getId(), newVaccine);

            vaccineCall.enqueue(new Callback<Vaccine>() {
                @Override
                public void onResponse(Call<Vaccine> call, Response<Vaccine> response) {
                    Platform.runLater(() -> {
                        Alerts.showInfoAlert("Vacuna monificada correctamente: " + new Gson().toJson(response.body()));
                        getAllVaccines();
                        activateEditVaccineMode(false);
                        resetTextFieldsComboBoxAndWebViews(VACCINE);
                    });
                }

                @Override
                public void onFailure(Call<Vaccine> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditVaccineMode(false);
                        resetTextFieldsComboBoxAndWebViews(VACCINE);
                    });
                }
            });

        }

    }

    @FXML
    public void getVaccineFromListView(Event event) {
        vaccineSelected = (Vaccine) lvVaccines.getSelectionModel().getSelectedItem();
        if(!validateItemSelectedFromListView(vaccineSelected)) {
            return;
        }
        tfNameVaccine.setText(vaccineSelected.getName());
        tfDurabilityVaccine.setText(String.valueOf(vaccineSelected.getDurability()));
        tfEffectivityVaccine.setText(String.valueOf(vaccineSelected.getEffectivity()));
    }

    @FXML
    public void activateNewVaccine(Event event) {
        action = Action.NEW;
        activateNewVaccineMode(true);
    }

    @FXML
    public void activateModifyVaccine(Event event) {
        action = Action.MODIFY;
        activateEditVaccineMode(true);
    }

    @FXML
    public void deleteVaccine(Event event) {

        if(lvVaccines.isDisable()) {
            activateDeleteVaccineMode(true);
            Alerts.showInfoAlert("Selecciona un item de la lista y después pulsa BORRAR");
            return;
        }

        if (!validateTextFieldsAndComboBox(VACCINE)) {
            return;
        }

        if (vaccineSelected.getId() == 0) {
            Alerts.showErrorAlert("Debes seleccionar un item de la lista");
            return;
        }

        Call<ResponseBody> callDelete = vaccineService.deleteVaccine(vaccineSelected.getId());

        callDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Platform.runLater(() -> {
                    Alerts.showInfoAlert("Vacuna eliminada correctamente");
                    getAllVaccines();
                    resetTextFieldsComboBoxAndWebViews(VACCINE);
                    activateEditVaccineMode(false);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    resetTextFieldsComboBoxAndWebViews(VACCINE);
                    activateEditVaccineMode(false);
                });
            }
        });
    }

    @FXML
    public void cancelActionVaccine(Event event) {
        activateEditVaccineMode(false);
        resetTextFieldsComboBoxAndWebViews(VACCINE);
    }

    private void getAllVaccines() {
        lvVaccines.getItems().clear();
        vaccineService.getAllVaccines()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de vacunas cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de vacunas"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(vaccine -> {
                    Platform.runLater(() -> {
                        listVaccines.add(vaccine);
                    });
                });
    }

    private void activateNewVaccineMode(boolean mode) {
        btModifyVaccine.setDisable(mode);
        btDeleteVaccine.setDisable(mode);
        btNewVaccine.setDisable(mode);

        btCancelVaccine.setDisable(!mode);
        btSaveNewVaccine.setDisable(!mode);

        tfDurabilityVaccine.setDisable(!mode);
        tfEffectivityVaccine.setDisable(!mode);
        tfNameVaccine.setDisable(!mode);
    }

    private void activateEditVaccineMode(boolean mode) {
        btModifyVaccine.setDisable(mode);
        btDeleteVaccine.setDisable(mode);
        btNewVaccine.setDisable(mode);

        btCancelVaccine.setDisable(!mode);
        btSaveNewVaccine.setDisable(!mode);

        tfDurabilityVaccine.setDisable(!mode);
        tfEffectivityVaccine.setDisable(!mode);
        tfNameVaccine.setDisable(!mode);

        lvVaccines.setDisable(!mode);
    }

    private void activateDeleteVaccineMode(boolean mode) {
        btModifyVaccine.setDisable(mode);
        btNewVaccine.setDisable(mode);
        btSaveNewVaccine.setDisable(mode);

        btCancelVaccine.setDisable(!mode);
        btDeleteVaccine.setDisable(!mode);

        tfDurabilityVaccine.setDisable(mode);
        tfEffectivityVaccine.setDisable(mode);
        tfNameVaccine.setDisable(mode);


        lvVaccines.setDisable(!mode);
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /** MONEDAS ------------------------------------------------------------------------------------------------------*/
    @FXML
    public void addNewCoin(Event event) {
        if (action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFieldsAndComboBox(COIN)) {
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
                        resetTextFieldsComboBoxAndWebViews(COIN);
                    });
                }

                @Override
                public void onFailure(Call<Coin> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewCoinMode(false);
                        resetTextFieldsComboBoxAndWebViews(COIN);
                    });
                }
            });

        } else { // Modificar

            if(!validateTextFieldsAndComboBox(COIN)) {
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
                        resetTextFieldsComboBoxAndWebViews(COIN);
                    });
                }

                @Override
                public void onFailure(Call<Coin> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditCoinMode(false);
                        resetTextFieldsComboBoxAndWebViews(COIN);
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

        if (!validateTextFieldsAndComboBox(COIN)) {
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
                    resetTextFieldsComboBoxAndWebViews(COIN);
                    activateEditCoinMode(false);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alerts.showErrorAlert(throwable.getMessage());
                    getAllCoins();
                    resetTextFieldsComboBoxAndWebViews(COIN);
                    activateEditCoinMode(false);
                });
            }
        });

    }

    @FXML
    public void cancelActionCoin(Event event) {
        activateEditCoinMode(false);
        resetTextFieldsComboBoxAndWebViews(COIN);
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

    private void loadComboBoxCountries() {
        countryService.getAllCountries()
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de paises COMBOBOX cargado correctamente"))
                .doOnError(throwable -> Alerts.showErrorAlert("Error al mostrar el listado de idiomas"))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(country -> {
                    Platform.runLater(() -> {
                        listComboBoxCountries.add(country);
                        listComboBoxCountriesCity.add(country);
                    });
                });
    }

    /** --------------------------------------------------------------------------------------------------------------*/

    /** ELECTRICIDAD -------------------------------------------------------------------------------------------------*/
    @FXML
    public void addNewElectricity(Event event) {

        if (action.equals(Action.NEW)) { // Nuevo

            if (!validateTextFieldsAndComboBox(ELECTRICITY)) {
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
                        resetTextFieldsComboBoxAndWebViews(ELECTRICITY);
                    });

                }

                @Override
                public void onFailure(Call<Electricity> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateNewElectricityMode(false);
                        resetTextFieldsComboBoxAndWebViews(ELECTRICITY);
                    });
                }
            });

        } else { // Modificar

            if (!validateTextFieldsAndComboBox(ELECTRICITY)) {
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
                        resetTextFieldsComboBoxAndWebViews(ELECTRICITY);
                    });
                }

                @Override
                public void onFailure(Call<Electricity> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert(throwable.getMessage());
                        activateEditElectricityMode(false);
                        resetTextFieldsComboBoxAndWebViews(ELECTRICITY);
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
        if (!validateTextFieldsAndComboBox(ELECTRICITY)) {
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
                    resetTextFieldsComboBoxAndWebViews(ELECTRICITY);
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
                    resetTextFieldsComboBoxAndWebViews(ELECTRICITY);
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
        resetTextFieldsComboBoxAndWebViews(ELECTRICITY);
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
        btSaveNewVaccine.setDisable(mode);
        btCancelVaccine.setDisable(mode);
        btSaveNewEmergencyPhone.setDisable(mode);
        btCancelEmergencyPhone.setDisable(mode);
        btSaveNewPlug.setDisable(mode);
        btCancelPlug.setDisable(mode);
        btSaveNewCity.setDisable(mode);
        btCancelCity.setDisable(mode);

        lvElectricity.setDisable(mode);
        lvCoins.setDisable(mode);
        lvLanguages.setDisable(mode);
        lvVaccines.setDisable(mode);
        lvEmergencyPhones.setDisable(mode);
        lvPlug.setDisable(mode);
        lvCities.setDisable(mode);

        tfFrecuency.setDisable(mode);
        tfVoltage.setDisable(mode);
        tfCodeISOCoin.setDisable(mode);
        tfSymbolCoin.setDisable(mode);
        tfMonetaryUnitCoin.setDisable(mode);
        tfLanguageName.setDisable(mode);
        tfNameVaccine.setDisable(mode);
        tfDurabilityVaccine.setDisable(mode);
        tfEffectivityVaccine.setDisable(mode);
        tfPhoneNumberEmergencyPhone.setDisable(mode);
        tfServiceEmergencyPhone.setDisable(mode);
        tfImageUrlPlug.setDisable(mode);
        tfNameCity.setDisable(mode);
        tfExtensionCity.setDisable(mode);
        tfNumberOfHabitantsCity.setDisable(mode);

        cbCountry.setDisable(mode);
        cbPlugType.setDisable(mode);
        cbCountriesCity.setDisable(mode);

        wbTypePlugImage.setVisible(!mode);
    }

    /**
     * Metodo general que comprueba los datos de las cajas de texto
     * @param type
     * @return
     */
    private boolean validateTextFieldsAndComboBox(String type) {
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

            case VACCINE:

                if (tfDurabilityVaccine.getText().isBlank() || tfEffectivityVaccine.getText().isBlank() || tfNameVaccine.getText().isBlank()) {
                    Alerts.showErrorAlert("No puedes dejar los valores en blanco");
                    value = false;
                }
                else {
                    value = true;
                }

                break;

            case EMERGENCYPHONE:

                if (tfServiceEmergencyPhone.getText().isBlank() || tfPhoneNumberEmergencyPhone.getText().isBlank()) {
                    Alerts.showErrorAlert("No puedes dejar los valores en blanco");
                    value = false;
                }
                else if(cbCountry.getSelectionModel().getSelectedItem() == null) {
                    Alerts.showErrorAlert("Debes seleccionar un país del combo box");
                    value = false;
                }
                else {
                    value = true;
                }

                break;

            case PLUG:

                if (tfImageUrlPlug.getText().isBlank()) {
                    Alerts.showErrorAlert("No puedes dejar los valores en blanco");
                    value = false;
                }
                else if(cbPlugType.getSelectionModel().getSelectedItem() == null) {
                    Alerts.showErrorAlert("Debes seleccionar un tipo del combo box");
                    value = false;
                }
                else {
                    value = true;
                }

                break;

            case CITY:

                if(tfExtensionCity.getText().isBlank() || tfNameCity.getText().isBlank() || tfNumberOfHabitantsCity.getText().isBlank()) {
                    Alerts.showErrorAlert("No puedes dejar los valores en blanco");
                    value = false;
                }
                else if(cbCountriesCity.getSelectionModel().getSelectedItem() == null) {
                    Alerts.showErrorAlert("Debes vincular la ciudad a un país del combo box");
                    value = false;
                }
                else {
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
    private void resetTextFieldsComboBoxAndWebViews(String type) {
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

            case VACCINE:
                tfEffectivityVaccine.setText("");
                tfDurabilityVaccine.setText("");
                tfNameVaccine.setText("");
                break;

            case EMERGENCYPHONE:
                tfServiceEmergencyPhone.setText("");
                tfPhoneNumberEmergencyPhone.setText("");
                cbCountry.valueProperty().set(null);
                break;

            case PLUG:
                tfImageUrlPlug.setText("");
                cbPlugType.valueProperty().set(null);
                wbTypePlugImage.getEngine().loadContent("");
                break;

            case CITY:
                tfExtensionCity.setText("");
                tfNameCity.setText("");
                tfNumberOfHabitantsCity.setText("");
                cbCountriesCity.valueProperty().set(null);
                break;

        }
    }

}
