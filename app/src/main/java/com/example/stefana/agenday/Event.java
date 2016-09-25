package com.example.stefana.agenday;

import android.media.Image;

import com.yelp.clientlib.entities.Business;

/**
 * Created by Stefana on 9/24/2016.
 */

public class Event {
    private String title;
    private String description;
    //private Image image;

    public Event(Business business){
        this.title = business.name();
        this.description = business.snippetText();
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }





}
