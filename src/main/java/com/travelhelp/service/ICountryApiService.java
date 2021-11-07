package com.travelhelp.service;

import com.travelhelp.domain.Country;
import retrofit2.http.GET;
import rx.Observable;

import java.util.List;

public interface ICountryApiService {

    @GET("/country")
    Observable<List<Country>> getAllCountries();

}
