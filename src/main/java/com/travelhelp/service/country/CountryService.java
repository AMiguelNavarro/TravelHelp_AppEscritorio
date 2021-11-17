package com.travelhelp.service.country;

import com.travelhelp.domain.Country;
import com.travelhelp.domain.dto.CountryDTO;
import com.travelhelp.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

public class CountryService {

    private ICountryService countryService;

    public CountryService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        countryService = retrofit.create(ICountryService.class);
    }

    public Observable<List<Country>> getAllCountries() {
        return countryService.getAllCountries();
    }
    public Call<Country> addNewCountry(CountryDTO newCountryDTO) {
        return  countryService.addNewCountry(newCountryDTO);
    }
    public Call<ResponseBody> deleteCountry(long id) {
        return countryService.deleteCountry(id);
    }
    public Call<Country> modifyCountry(long id, CountryDTO newCountryDTO) {
        return countryService.modifyCountry(id, newCountryDTO);
    }

}
