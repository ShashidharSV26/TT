package com.example.tt;

public class HomeModel {

    private String catagory;
    private int image;

    public HomeModel(String catagory, int image) {
        this.catagory = catagory;
        this.image = image;
    }

    public String getCatagory() {
        return catagory;
    }

    public int getImage() {
        return image;
    }

    @Override
    public String toString() {
        return ""+catagory+""+ image ;
    }
}
