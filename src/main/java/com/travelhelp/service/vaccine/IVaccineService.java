package com.travelhelp.service.vaccine;


import com.travelhelp.domain.Vaccine;
import com.travelhelp.domain.dto.LanguageDTO;
import com.travelhelp.domain.dto.VaccineDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;
import java.util.List;

public interface IVaccineService {

    @GET("/vaccine")
    Observable<List<Vaccine>> getAllVaccines();

    @GET("/vaccine/idCountry/{idCountry}")
    Observable<List<VaccineDTO>> getVaccinesFromCountry(@Path("idCountry") long idCountry);

    // Se debe devolver una Call
    @POST("/vaccine") @Headers("Content-type: application/json")
    Call<Vaccine> addNewVaccine(@Body Vaccine newVaccine);

    // Se debe devolver una respuesta
    @DELETE("/vaccine/{id}")
    Call<ResponseBody> deleteVaccine(@Path("id")long id);

    @PUT("/vaccine/{id}")
    Call<Vaccine> modifyVaccine(@Path("id") long id , @Body Vaccine newVaccine);

}
