package com.example.mcresswell.project01;

import android.graphics.drawable.Drawable;

public class FitnessScore {
    private Drawable image;
    private String text;

    public FitnessScore(Drawable image, String text){
        this.image = image;
        this.text = text;
    }

    public Drawable getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
