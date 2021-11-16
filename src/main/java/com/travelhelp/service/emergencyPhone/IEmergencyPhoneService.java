package com.travelhelp.service.emergencyPhone;

import com.travelhelp.domain.EmergencyPhone;
import com.travelhelp.domain.Language;
import com.travelhelp.domain.dto.EmergencyPhoneDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;

public interface IEmergencyPhoneService {

    @GET("/emergencyphone")
    Observable<List<EmergencyPhone>> getAllEmergencyPhones();

    // Se debe devolver una Call
    @POST("/emergencyphone") @Headers("Content-type: application/json")
    Call<EmergencyPhone> addNewEmergencyPhone(@Body EmergencyPhoneDTO newEmergencyPhoneDTO);

    // Se debe devolver una respuesta
    @DELETE("/emergencyphone/{id}")
    Call<ResponseBody> deleteEmergencyPhone(@Path("id")long id);

    @PUT("/emergencyphone/{id}")
    Call<EmergencyPhone> modifyEmergencyPhone(@Path("id") long id , @Body EmergencyPhoneDTO newEmergencyPhoneDTO);

}
