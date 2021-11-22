package com.travelhelp.controller;

import com.travelhelp.domain.City;
import com.travelhelp.domain.Language;
import com.travelhelp.domain.dto.LanguageDTO;
import com.travelhelp.service.language.LanguageService;
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

public class LanguageViewController implements Initializable {

    public ListView lvLanguages;
    public Label lbOfficial;

    private long idCountry;
    private ObservableList<LanguageDTO> listLanguages;
    private LanguageService languageService;
    private LanguageDTO languageSelected;

    public LanguageViewController(long idCountry) {
        this.idCountry = idCountry;
    }

    @FXML
    public void getLanguageFromListView(Event event) {
        languageSelected = (LanguageDTO) lvLanguages.getSelectionModel().getSelectedItem();
        if (languageSelected == null) {
            Alerts.showErrorAlert("No has seleccionado ningún item del listado");
            return;
        }
        if (languageSelected.isOfficial()) {
            lbOfficial.setText("SI");
        } else {
            lbOfficial.setText("NO");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageService = new LanguageService();
        listLanguages = FXCollections.observableArrayList();
        lvLanguages.setItems(listLanguages);
        getLanguagesFromCountry(this.idCountry);
    }

    private void getLanguagesFromCountry(long idCountry) {
        lvLanguages.getItems().clear();
        languageService.getLanguagesFromCountry(this.idCountry)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de idiomas ok"))
                .doOnError(throwable -> System.out.println("ERROR AL DESCARGAR EL LISTADO -> " + throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(language -> listLanguages.add(language));
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

        Stage actualStage = (Stage) this.lbOfficial.getScene().getWindow();
        actualStage.close();
    }
}
