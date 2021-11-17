package com.travelhelp.service.country;

import com.travelhelp.domain.Country;
import com.travelhelp.domain.Plug;
import com.travelhelp.domain.dto.CountryDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;

public interface ICountryService {

    @GET("/country")
    Observable<List<Country>> getAllCountries();

    // Se debe devolver una Call
    @POST("/country") @Headers("Content-type: application/json")
    Call<Country> addNewCountry(@Body CountryDTO newCountryDTO);

    // Se debe devolver una respuesta
    @DELETE("/country/{id}")
    Call<ResponseBody> deleteCountry(@Path("id")long id);

    @PUT("/country/{id}")
    Call<Country> modifyCountry(@Path("id") long id , @Body CountryDTO newCountryDTO);

}
