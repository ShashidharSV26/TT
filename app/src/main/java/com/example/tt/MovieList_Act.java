package com.example.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MovieList_Act extends AppCompatActivity {
    private static final String JSON_URL = "http://192.168.2.254/efe/getAllContent.do";
    int recived_lang;

   static String maintitle;

    String languageObjectString;
    JSONArray jsonselectedlanguage=null;

    TextView main_Title;
    RecyclerView recyclerView;
//    ListView listView;
    ArrayList<MovieItem_Modle> movielist=new ArrayList<>();
    ArrayList<String> listofmovies=new ArrayList<>();

    List<DetailsModel> videoLangListAllObj= new ArrayList<>();
    MovieListAdapter movieadapter;
    DetailsAdapter detailsAdapter;
   static String catagory;

    JSONObject videoobj;
    JSONArray  moviesList;
    String playListTitle;
    String startTime;
    String endTime;
    String callDB=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        ActionBar actionBar = getSupportActionBar();
         //to set back press on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

       recyclerView=findViewById(R.id.recycleview);
//       listView=findViewById(R.id.langList);
       main_Title=findViewById(R.id.maintitle);


        Intent intent = getIntent();
       catagory= intent.getStringExtra("selectedMainItem");
        actionBar.setTitle(catagory.toUpperCase());
        maintitle=intent.getStringExtra("selectedLanguage");
        main_Title.setText(maintitle.toUpperCase());
        languageObjectString=intent.getStringExtra("languageArray");
        recived_lang = intent.getIntExtra("language",0);
          startTime=intent.getStringExtra("startTime");
          endTime=intent.getStringExtra("endTime");
          callDB=intent.getStringExtra("callDB");

//if("CALLING".equals(callDB)){
//    UserData uDB=new UserData(this);
//    uDB.addData(catagory,maintitle,MovieListAdapter.moviename,null,startTime,endTime);
//}

           playAll();



    }



    private void playAll() {

        try {
            jsonselectedlanguage=new JSONArray(languageObjectString);
            JSONObject selectedLangVideosList=jsonselectedlanguage.getJSONObject(recived_lang);
            moviesList=selectedLangVideosList.getJSONArray(maintitle);


            if(catagory.equals("movies") || catagory.equals("trailers")){
                for (int i = 0; i < moviesList.length(); i++) {
                    String movie = moviesList.getString(i);
                    listofmovies.add(movie);
                    movielist.add(new MovieItem_Modle(movie,"http://192.168.2.254/TT/efe/images/"+movie.substring(0,movie.length()-4)+".jpg"));
                }
                movieadapter=new MovieListAdapter(this,movielist,catagory,maintitle,listofmovies);
            } else if (catagory.equals("videos") || catagory.equals("comedy")) {

            for (int i = 0; i < moviesList.length(); i++) {
                videoobj=moviesList.getJSONObject(i);

                playListTitle = videoobj.keys().next();
                if(catagory.equals("videos")) {
                    movielist.add(new MovieItem_Modle(playListTitle, R.drawable.songs));
                   Log.d("movielistACT","playlisttitle=="+playListTitle);
                } else if (catagory.equals("comedy")) {
                    movielist.add(new MovieItem_Modle(playListTitle, R.drawable.comedy));
                }
//                movielist.add(new MovieItem_Modle(playListTitle, null));
            }
                Log.d("MovieList_Act","videos/comedy jsonobject======="+videoobj);
                Log.d("MovieList_Act","videos/comedy jsonobject======="+moviesList);
                movieadapter=new MovieListAdapter(this,moviesList,movielist,catagory,maintitle,listofmovies);
            }
            Log.d("MovieList","Videos and comedy==="+movielist.toString());
//            movieadapter=new MovieListAdapter(this,movielist,catagory,maintitle,listofmovies);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(movieadapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MovieList_Act.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    onBackPressed();
    return super.onOptionsItemSelected(item);
}

}


