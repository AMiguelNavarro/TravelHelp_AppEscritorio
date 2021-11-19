package com.travelhelp.controller;

import com.travelhelp.domain.EmergencyPhone;
import com.travelhelp.domain.dto.EmergencyPhoneDTO;
import com.travelhelp.service.emergencyPhone.EmergencyPhoneService;
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

public class EmergencyPhoneViewController implements Initializable {

    public ListView lvEmergencyPhones;
    public Label lbPhoneNumber,lbService;

    private long idCountry;
    private EmergencyPhoneService emergencyPhoneService;
    private EmergencyPhoneDTO emergencyPhoneSelected;
    private ObservableList<EmergencyPhoneDTO> listEmergencyPhones;

    public EmergencyPhoneViewController(long idCountry) {
        this.idCountry = idCountry;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emergencyPhoneService = new EmergencyPhoneService();

        listEmergencyPhones = FXCollections.observableArrayList();
        lvEmergencyPhones.setItems(listEmergencyPhones);
        getEmergencyPhonesFromCountry();
    }


    @FXML
    public void getEmergencyPhoneSelected(Event event) {
        emergencyPhoneSelected = (EmergencyPhoneDTO) lvEmergencyPhones.getSelectionModel().getSelectedItem();
        if (emergencyPhoneSelected == null){
            Alerts.showErrorAlert("No has seleccionado ningún item del listado");
            return;
        }
        lbPhoneNumber.setText(emergencyPhoneSelected.getPhoneNumber());
        lbService.setText(emergencyPhoneSelected.getService());
    }

    private void getEmergencyPhonesFromCountry() {
        lvEmergencyPhones.getItems().clear();
        emergencyPhoneService.getEmergencyPhonesFromCountry(this.idCountry)
                .flatMap(Observable::from)
                .doOnCompleted(() -> System.out.println("Listado de teléfonos ok"))
                .doOnError(throwable -> System.out.println("ERROR AL DESCARGAR EL LISTADO -> " + throwable.getMessage()))
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(emergencyPhoneDTO -> listEmergencyPhones.add(emergencyPhoneDTO));
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

        Stage actualStage = (Stage) this.lbService.getScene().getWindow();
        actualStage.close();
    }
}
