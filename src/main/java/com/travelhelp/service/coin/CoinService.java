package com.travelhelp.service.coin;

import com.travelhelp.domain.Coin;
import com.travelhelp.domain.Plug;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import java.util.List;
import static com.travelhelp.utils.Constants.URL_BASE;

public class CoinService {

    private ICoinService coinService;

    public CoinService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        coinService = retrofit.create(ICoinService.class);
    }

    public Observable<List<Coin>> getAllCoins() {
        return coinService.getAllCoins();
    }
    public Call<Coin> getCoinFromCountry(long idCountry) {
        return coinService.getCoinFromCountry(idCountry);
    }
    public Call<Coin> addNewCoin(Coin newCoin) {
        return coinService.addNewCoin(newCoin);
    }
    public Call<ResponseBody> deleteCoin(long id) {
        return coinService.deleteCoin(id);
    }
    public Call<Coin> modifyCoin(long id, Coin newCoin) {
        return coinService.modifyCoin(id, newCoin);
    }

}
