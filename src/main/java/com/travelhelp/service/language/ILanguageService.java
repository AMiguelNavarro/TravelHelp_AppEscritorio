package com.travelhelp.service.language;

import com.travelhelp.domain.Language;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;
import java.util.List;

public interface ILanguageService {

    @GET("/language")
    Observable<List<Language>> getAllLanguages();

    // Se debe devolver una Call
    @POST("/language") @Headers("Content-type: application/json")
    Call<Language> addNewLanguage(@Body Language newLanguage);

    // Se debe devolver una respuesta
    @DELETE("/language/{id}")
    Call<ResponseBody> deleteLanguage(@Path("id")long id);

    @PUT("/language/{id}")
    Call<Language> modifyLanguage(@Path("id") long id , @Body Language newLanguage);

}
