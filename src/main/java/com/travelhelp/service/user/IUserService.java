package com.travelhelp.service.user;

import com.travelhelp.domain.User;
import retrofit2.Call;
import retrofit2.http.*;
import rx.Observable;

public interface IUserService {

    @GET("/user/{username}/{password}")
    Call<User> checkUser(@Path("username") String username, @Path("password") String password);

    @POST("/user") @Headers("Content-type: application/json")
    Call<User> addNewUser(@Body User user);

}
