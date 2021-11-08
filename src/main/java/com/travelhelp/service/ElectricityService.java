package com.travelhelp.service;

import com.travelhelp.domain.Country;
import com.travelhelp.domain.Electricity;
import com.travelhelp.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

public class ElectricityService {

    private IElectricityService electricityService;

    public ElectricityService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        electricityService = retrofit.create(IElectricityService.class);
    }

    public Observable<List<Electricity>> getAllElectricities() {
        return electricityService.getAllElectricities();
    }
    public Electricity  addNewElectrivity(Electricity newElectricity) {
        return electricityService.addNewElectricity(newElectricity);
    }
}
