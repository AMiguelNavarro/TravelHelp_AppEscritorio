package com.travelhelp.controller;

import com.travelhelp.domain.Vaccine;
import com.travelhelp.domain.dto.LanguageDTO;
import com.travelhelp.domain.dto.VaccineDTO;
import com.travelhelp.service.vaccine.VaccineService;
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

public class VaccineViewController implements Initializable {

    public ListView lvVaccines;
    public Label lbObligatory,lbEffectivity,lbDurability;

    private long idCountry;
    private ObservableList<VaccineDTO> listVaccines;
    private VaccineService vaccineService;
    private VaccineDTO vaccineSelected;

    public VaccineViewController(long idCountry) {
        this.idCountry = idCountry;
    }

    @FXML
    public void getVaccinesFromListView(Event event) {
        vaccineSelected = (VaccineDTO) lvVaccines.getSelectionModel().getSelectedItem();
        if (vaccineSelected == null) {
            Alerts.showErrorAlert("No has seleccionado ningún item del listado");
            return;
        }
        lbDurability.setText(vaccineSelected.getDurability() + " días");
        lbEffectivity.setText(vaccineSelected.getEffectivity() + "%");
        if (vaccineSelected.isObligatory()) {
            lbObligatory.setText("SI");
        } else {
            lbObligatory.setText("NO");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vaccineService = new VaccineService();
        listVaccines = FXCollections.observableArrayList();
        lvVaccines.setItems(listVaccines);
        getVaccinesFromCountry(this.idCountry);
    }

    private void getVaccinesFromCountry(long idCountry) {
        lvVaccines.getItems().clear();
        vaccineService.getVaccinesFromCountry(this.idCountry)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de vacunas ok"))
                .doOnError(throwable -> System.out.println("ERROR AL DESCARGAR EL LISTADO -> " + throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(vaccine -> listVaccines.add(vaccine));
    }

    public void closeWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        AppController controller = new AppController();
        loader.setLocation(R.getUI("interfaz"));
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage actualStage = (Stage) this.lbEffectivity.getScene().getWindow();
        actualStage.close();
    }
}
