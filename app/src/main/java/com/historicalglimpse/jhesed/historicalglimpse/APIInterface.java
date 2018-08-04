package com.historicalglimpse.jhesed.historicalglimpse;

import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseDetailsResource;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseListResource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Jhesed Tacadena 2018-07-21
 */

public interface APIInterface {

    @GET("/cms-glimpse/api/getDataSingle.php")
    Call<GlimpseDetailsResource> getGlimpseToday(@Query("title") String title);

    @GET("/cms-glimpse/api/getData.php")
    Call<GlimpseListResource> getGlimpseList(@Query("date_range") String date_range);

}
