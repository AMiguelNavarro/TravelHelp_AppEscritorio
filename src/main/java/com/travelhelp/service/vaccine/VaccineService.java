package com.travelhelp.service.vaccine;

import com.travelhelp.domain.Vaccine;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

import static com.travelhelp.utils.Constants.URL_BASE;

public class VaccineService {

    private IVaccineService vaccineService;

    public VaccineService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        vaccineService = retrofit.create(IVaccineService.class);
    }

    public Observable<List<Vaccine>>getAllVaccines() {
        return vaccineService.getAllVaccines();
    }
    public Call<Vaccine> addNewVaccine(Vaccine newVaccine) {
        return vaccineService.addNewVaccine(newVaccine);
    }
    public Call<ResponseBody> deleteVaccine(long id) {
        return vaccineService.deleteVaccine(id);
    }
    public Call<Vaccine> modifyVaccine(long id, Vaccine newVaccine) {
        return vaccineService.modifyVaccine(id, newVaccine);
    }

}
