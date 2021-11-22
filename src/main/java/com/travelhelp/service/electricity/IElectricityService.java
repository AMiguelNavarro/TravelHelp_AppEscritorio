package com.travelhelp.service.electricity;


import com.travelhelp.domain.City;
import com.travelhelp.domain.Electricity;
import com.travelhelp.domain.Plug;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;
import rx.Observable;
import java.util.List;

public interface IElectricityService {

    @GET("/electricity")
    Observable<List<Electricity>> getAllElectricities();

    @GET("/electricity/idCountry/{idCountry}")
    Call<Electricity> getElectricityFromCountry(@Path("idCountry") long idCountry);

    // Se debe devolver una Call
    @POST("/electricity") @Headers("Content-type: application/json")
    Call<Electricity> addNewElectricity(@Body Electricity newElectricity);

    // Se debe devolver una respuesta
    @DELETE("/electricity/{id}")
    Call<ResponseBody> deleteElectricity(@Path("id")long id);

    @PUT("/electricity/{id}")
    Call<Electricity> modifyElectricity(@Path("id") long id , @Body Electricity newElectricity);
}
