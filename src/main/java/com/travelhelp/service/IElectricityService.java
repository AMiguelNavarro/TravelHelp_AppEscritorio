package com.travelhelp.service;


import com.travelhelp.domain.Electricity;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;
import java.util.List;

public interface IElectricityService {

    @GET("/electricity")
    Observable<List<Electricity>> getAllElectricities();

    // ERROR AL AÑADIR COMPROBAR COMO SE DEBE HACER CON RETROFIT Y RX JAVA
    @POST("/electricity")
    Electricity addNewElectricity(@Body Electricity newElectricity);
}
