package com.travelhelp.service.city;

import com.travelhelp.domain.City;
import com.travelhelp.domain.Plug;
import com.travelhelp.domain.dto.CityDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;

public interface ICityService {
    @GET("/city")
    Observable<List<City>> getAllCities();

    // Se debe devolver una Call
    @POST("/city") @Headers("Content-type: application/json")
    Call<City> addNewCity(@Body CityDTO newCityDTO);

    // Se debe devolver una respuesta
    @DELETE("/city/{idOldCity}")
    Call<ResponseBody> deletePlug(@Path("idOldCity")long idOldCity);

    @PUT("/city/{idOldCity}")
    Call<City> modifyPlug(@Path("idOldCity") long idOldCity , @Body CityDTO newCityDTO);
}
