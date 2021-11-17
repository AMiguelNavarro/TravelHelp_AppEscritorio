package com.travelhelp.service.plug;

import com.travelhelp.domain.Plug;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

import static com.travelhelp.utils.Constants.URL_BASE;

public class PlugService {

    private IPlugService plugService;

    public PlugService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        plugService = retrofit.create(IPlugService.class);
    }

    public Observable<List<Plug>> getAllPlugs() {
        return plugService.getAllPlugs();
    }
    public Call<Plug> addNewPlug(Plug newPlug) {
        return plugService.addNewPlug(newPlug);
    }
    public Call<ResponseBody> deletePlug(long id) {
        return plugService.deletePlug(id);
    }
    public Call<Plug> modifyPlug(long id, Plug newPlug) {
        return plugService.modifyPlug(id, newPlug);
    }

}
