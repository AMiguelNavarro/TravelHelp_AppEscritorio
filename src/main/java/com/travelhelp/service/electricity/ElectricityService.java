package com.travelhelp.service.electricity;

import com.travelhelp.domain.Electricity;
import com.travelhelp.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    public Call<Electricity>  addNewElectrivity(Electricity newElectricity) {
        return electricityService.addNewElectricity(newElectricity);
    }
    public Call<ResponseBody> deleteElectricity(long id) {
        return electricityService.deleteElectricity(id);
    }
    public Call<Electricity> modifyElectricity(long id, Electricity newElectricity) {
        return electricityService.modifyElectricity(id, newElectricity);
    }
}
