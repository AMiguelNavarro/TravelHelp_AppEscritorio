package com.travelhelp.service.user;

import com.travelhelp.domain.User;
import com.travelhelp.service.plug.IPlugService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import static com.travelhelp.utils.Constants.URL_BASE;

public class UserService {

    private IUserService userService;

    public UserService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        userService = retrofit.create(IUserService.class);
    }

    public Call<User> checkUser(String username, String password) {
        return userService.checkUser(username, password);
    }
    public Call<User> addNewUser(User user) {
        return userService.addNewUser(user);
    }

}
