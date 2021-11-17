package com.travelhelp.service.plug;


import com.travelhelp.domain.Plug;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;
import java.util.List;

public interface IPlugService {
    @GET("/plug")
    Observable<List<Plug>> getAllPlugs();

    // Se debe devolver una Call
    @POST("/plug") @Headers("Content-type: application/json")
    Call<Plug> addNewPlug(@Body Plug newPlug);

    // Se debe devolver una respuesta
    @DELETE("/plug/{id}")
    Call<ResponseBody> deletePlug(@Path("id")long id);

    @PUT("/plug/{id}")
    Call<Plug> modifyPlug(@Path("id") long id , @Body Plug newPlug);
}
