package com.example.tt;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainPage_Act extends AppCompatActivity {

    CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        cardView1=findViewById(R.id.moviecard);
        cardView2=findViewById(R.id.videoscard);
        cardView3=findViewById(R.id.comedycard);
        cardView4=findViewById(R.id.sportscard);
        cardView5=findViewById(R.id.aboutcard);
        cardView6=findViewById(R.id.safetycard);

        getSupportActionBar().setTitle("HOME");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home);

    }
    public void onMovieCard(View view) {
        Intent intent = new Intent(this, DetailPage_Act.class);
        intent.putExtra("maintitle","movies");
        startActivity(intent);
    }

    public void onVideosCard(View view) {
        Intent intent = new Intent(this, DetailPage_Act.class);
        intent.putExtra("maintitle","videos");
        startActivity(intent);
    }

    public void onComedyCard(View view) {
        Intent intent = new Intent(this, DetailPage_Act.class);
        intent.putExtra("maintitle","comedy");
        startActivity(intent);
    }

    public void onSportsCard(View view) {
        Intent intent = new Intent(this, DetailPage_Act.class);
        intent.putExtra("maintitle","trailers");
        startActivity(intent);
    }

    public void onAboutCard(View view) {
        Intent intent = new Intent(this, ProfilePage_Act.class);
        startActivity(intent);
    }

    public void onSafetyCard(View view) {
        Intent intent = new Intent(this, ProfilePage_Act.class);
        startActivity(intent);
    }
}