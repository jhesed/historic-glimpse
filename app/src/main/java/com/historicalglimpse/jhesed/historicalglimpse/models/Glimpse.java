package com.historicalglimpse.jhesed.historicalglimpse.models;

/**
 * Created by jhesed on 7/28/2018.
 */

public class Glimpse {

    public final String glimpseDate;

    public final String headingWorld;
    public final String headingPhil;

    public Glimpse(String glimpseDate, String headingWorld, String headingPhil) {
        this.glimpseDate = glimpseDate;
        this.headingWorld = headingWorld;
        this.headingPhil = headingPhil;
    }
}
