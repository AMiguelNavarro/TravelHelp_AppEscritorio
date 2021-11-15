package com.travelhelp.service.country;

import com.travelhelp.domain.Country;
import com.travelhelp.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

public class CountryService {

    private ICountryApiService apiService;

    public CountryService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ICountryApiService.class);
    }

    public Observable<List<Country>> getAllCountries() {
        return apiService.getAllCountries();
    }

}
