package com.historicalglimpse.jhesed.historicalglimpse.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlimpseDetailsResource {

    @SerializedName("data")
    @Expose
    private List<DatumDetails> data = null;

    public List<DatumDetails> getData() {
        return data;
    }

    public void setData(List<DatumDetails> data) {
        this.data = data;
    }

}