package com.travelhelp.service.coin;

import com.travelhelp.domain.Coin;
import com.travelhelp.domain.Plug;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;
import java.util.List;

public interface ICoinService {

    @GET("/coin")
    Observable<List<Coin>> getAllCoins();

    @GET("/coin/idCountry/{idCountry}")
    Call<Coin> getCoinFromCountry(@Path("idCountry") long idCountry);

    // Se debe devolver una Call
    @POST("/coin") @Headers("Content-type: application/json")
    Call<Coin> addNewCoin(@Body Coin newCoin);

    // Se debe devolver una respuesta
    @DELETE("/coin/{id}")
    Call<ResponseBody> deleteCoin(@Path("id")long id);

    @PUT("/coin/{id}")
    Call<Coin> modifyCoin(@Path("id") long id , @Body Coin newCoin);

}
