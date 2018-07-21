package com.historicalglimpse.jhesed.historicalglimpse;

import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseResource;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Jhesed Tacadena 2018-07-21
 */

interface APIInterface {

    @GET("/cms-glimpse/api/getDataSingle.php")
    Call<GlimpseResource> getGlimpseToday();

//    @POST("/api/users")
//    Call<User> createUser(@Body User user);

//    @GET("/api/users?")
//    Call<UserList> doGetUserList(@Query("page") String page);

//    @FormUrlEncoded
//    @POST("/api/users?")
//    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}
