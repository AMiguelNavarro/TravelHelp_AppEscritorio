package com.travelhelp.service.city;

import com.travelhelp.domain.City;
import com.travelhelp.domain.dto.CityDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

import static com.travelhelp.utils.Constants.URL_BASE;

public class CityService {

    private ICityService cityService;

    public CityService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        cityService = retrofit.create(ICityService.class);
    }

    public Observable<List<City>> getAllCities() {
        return cityService.getAllCities();
    }
    public Observable<List<City>> getCitiesFromCountry(long idCountry) {
        return cityService.getCitiesFromCountry(idCountry);
    }
    public Call<City> addNewCity(CityDTO newCityDTO) {
        return cityService.addNewCity(newCityDTO);
    }
    public Call<ResponseBody> deleteCity(long id) {
        return cityService.deletePlug(id);
    }
    public Call<City> modifyCity(long id, CityDTO newCityDTO) {
        return cityService.modifyPlug(id, newCityDTO);
    }

}
