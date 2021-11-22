package com.travelhelp.controller;

import com.travelhelp.domain.Country;
import com.travelhelp.service.country.CountryService;
import com.travelhelp.utils.Alerts;
import com.travelhelp.utils.R;
import javafx.application.Platform;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    public WebView wbFlagImage;

    private CountryService apiService;
    private ObservableList<Country> listAllCountries;
    private Country countrySelected;
    private long idCountry;

    public AppController() {
    }

    public AppController(long idCountry) {
        this.idCountry = idCountry;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apiService = new CountryService();

        listAllCountries = FXCollections.observableArrayList();
        lvCountries.setItems(listAllCountries);
        getAllCountries();

        if (idCountry != 0) {
            Call<Country> countryCall = apiService.getCountryById(this.idCountry);
            countryCall.enqueue(new Callback<Country>() {
                @Override
                public void onResponse(Call<Country> call, Response<Country> response) {
                    Platform.runLater(() -> {
                        lbName.setText(response.body().getName());
                        lbAcronym.setText(response.body().getAcronym());
                        lbContinent.setText(response.body().getContinent());
                        if (response.body().isPublicHealthcare()) {
                            lbPublicHealthcare.setText("SI");
                        } else {
                            lbPublicHealthcare.setText("NO");
                        }
                        if (response.body().isDrinkingWater()) {
                            lbDrinkingWater.setText("SI");
                        } else {
                            lbDrinkingWater.setText("NO");
                        }
                        lbNumberOfHabitants.setText(String.valueOf(response.body().getNumberOfHabitants()));
                        lbPrefix.setText(response.body().getPrefix());
                        wbFlagImage.getEngine().load(response.body().getFlagImage());
                    });
                }

                @Override
                public void onFailure(Call<Country> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alerts.showErrorAlert("ERROR al cargar los datos del país con id " + idCountry);
                    });
                }
            });
        }

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
        loadDataOfCountry();
        idCountry = countrySelected.getId();
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
    public void showCoinView(Event event) throws IOException {
        if (idCountry == 0) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        CoinViewController controller = new CoinViewController(this.idCountry);
        loader.setLocation(R.getUI("vista_monedas"));
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

    @FXML
    public void showPlugView(Event event) throws IOException {
        if (idCountry == 0) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        PlugViewController controller = new PlugViewController(this.idCountry);
        loader.setLocation(R.getUI("vista_enchufes"));
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

    @FXML
    public void showVaccineView(Event event) throws IOException {
        if (idCountry == 0) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        VaccineViewController controller = new VaccineViewController(this.idCountry);
        loader.setLocation(R.getUI("vista_vacunas"));
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

    @FXML
    public void showElectricityView(Event event) throws IOException {
        if (idCountry == 0) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        ElectricityViewController controller = new ElectricityViewController(this.idCountry);
        loader.setLocation(R.getUI("vista_electricidades"));
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

    @FXML
    public void showLanguageView(Event event) throws IOException {
        if (idCountry == 0) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        LanguageViewController controller = new LanguageViewController(this.idCountry);
        loader.setLocation(R.getUI("vista_lenguajes"));
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

    @FXML
    public void showEmergencyPhoneView(Event event) throws IOException {
        if (idCountry == 0) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        EmergencyPhoneViewController controller = new EmergencyPhoneViewController(this.idCountry);
        loader.setLocation(R.getUI("vista_telefonos"));
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

    @FXML
    public void showCityView(Event event) throws IOException {

        if (idCountry == 0) {
            Alerts.showErrorAlert("Debes seleccionar un país de la lista");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        CitiesViewController controller = new CitiesViewController(this.idCountry);
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




    private void loadDataOfCountry() {
        lbName.setText(countrySelected.getName());
        lbAcronym.setText(countrySelected.getAcronym());
        lbContinent.setText(countrySelected.getContinent());
        if (countrySelected.isPublicHealthcare()) {
            lbPublicHealthcare.setText("SI");
        } else {
            lbPublicHealthcare.setText("NO");
        }
        if (countrySelected.isDrinkingWater()) {
            lbDrinkingWater.setText("SI");
        } else {
            lbDrinkingWater.setText("NO");
        }
        lbNumberOfHabitants.setText(String.valueOf(countrySelected.getNumberOfHabitants()));
        lbPrefix.setText(countrySelected.getPrefix());
        wbFlagImage.getEngine().load(countrySelected.getFlagImage());
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
