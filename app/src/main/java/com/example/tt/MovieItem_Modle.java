package com.example.tt;

public class MovieItem_Modle {

    private String videoname;
    private String videoImg;

    private int videoIcon;

    public MovieItem_Modle(String videoname,String videoImg) {
        this.videoname = videoname;
        this.videoImg=videoImg;
    }

    public MovieItem_Modle(String videoname,int videoIcon) {
        this.videoname = videoname;
        this.videoIcon=videoIcon;
    }

    public String getVideoname() {
        return videoname;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public int getVideoIcon() {
        return videoIcon;
    }

    @Override
    public String toString() {
        return ""+videoname;
    }
}
