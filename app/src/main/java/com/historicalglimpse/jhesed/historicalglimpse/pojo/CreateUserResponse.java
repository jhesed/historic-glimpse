package com.historicalglimpse.jhesed.historicalglimpse.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhesed Tacadena 2018-07-21
 */

public class CreateUserResponse {

    @SerializedName("name")
    public String name;
    @SerializedName("job")
    public String job;
    @SerializedName("id")
    public String id;
    @SerializedName("createdAt")
    public String createdAt;
}
