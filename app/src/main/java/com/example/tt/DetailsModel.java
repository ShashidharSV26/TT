package com.example.tt;

public class DetailsModel {

    String language;

    public DetailsModel(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return ""+language;
    }
}
