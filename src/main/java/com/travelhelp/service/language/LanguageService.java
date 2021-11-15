package com.travelhelp.service.language;

import com.google.gson.Gson;
import com.travelhelp.domain.Language;
import com.travelhelp.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

import static com.travelhelp.utils.Constants.URL_BASE;

public class LanguageService {

    private ILanguageService languageService;

    public LanguageService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        languageService = retrofit.create(ILanguageService.class);
    }

    public Observable<List<Language>> getAllLanguages() {
        return languageService.getAllLanguages();
    }
    public Call<Language> addNewLanguage(Language newLanguage) {
        return languageService.addNewLanguage(newLanguage);
    }
    public Call<ResponseBody> deleteLanguage(long id){
        return languageService.deleteLanguage(id);
    }
    public Call<Language> modifyLanguage(long id, Language newLanguage){
        return languageService.modifyLanguage(id, newLanguage);
    }

}
