package com.example.tt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    Context context;

    ArrayList<MovieItem_Modle> videolist;

    ArrayList<String> listofmovies;
    private Bitmap bitmap;

    String catagory;
    String language;

    JSONArray moviesList;
   static String moviename;


    JSONObject obj;

    public MovieListAdapter(Context context, JSONArray moviesList, ArrayList<MovieItem_Modle> videolist, String catagory, String language, ArrayList<String> listofmovies) {
        this.context = context;
        this.videolist = videolist;
        this.catagory=catagory;
        this.language=language;
        this.listofmovies=listofmovies;
        this.moviesList=moviesList;
        Log.d("MovieListAdapter","catagory=="+catagory+"listofvideos==="+videolist);
        Log.d("MovieListAdapter","catagory=="+catagory+"movieslist==="+moviesList);
    }

    public MovieListAdapter(Context context, ArrayList<MovieItem_Modle> videolist,String catagory, String language,ArrayList<String> listofmovies) {
        this.context = context;
        this.videolist = videolist;
        this.catagory=catagory;
        this.language=language;
        this.listofmovies=listofmovies;
        Log.d("MovieListAdapter","catagory=="+catagory+"listofvideos==="+videolist);
    }



    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View listitem=layoutInflater.inflate(R.layout.movie_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(listitem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int position) {

        MovieItem_Modle item_modle=(MovieItem_Modle) videolist.get(position);
        moviename=item_modle.getVideoname();
        Log.d("MovieListAdapter","onBind=videolist.size"+videolist.size());
//        holder.textView.setText(item_modle.getVideoname());
//        holder.textView.setText(item_modle.getVideoname().substring(0,item_modle.getVideoname().length()-4));
        if(catagory.equals("videos") || catagory.equals("comedy")){
            ArrayList<String> songs=new ArrayList<>();
            try {
                obj=moviesList.getJSONObject(position);
                JSONArray array = obj.getJSONArray(moviename);

                for(int i=0;i<array.length(); i++) {
                    String song= array.getString(i);
                    songs.add(song);
                }
                Log.d("MovieListAdapter","No.Of>Songs=="+songs);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.textView.setText(item_modle.getVideoname().toUpperCase()+"("+songs.size()+")");
            holder.imageView.setImageResource(item_modle.getVideoIcon());
        }else {
            String image = item_modle.getVideoImg();
            if (holder.imageView != null) {
                new ImageDownlaaderTask(holder.imageView).execute(image);
            }
            holder.textView.setText(item_modle.getVideoname().toUpperCase().substring(0,item_modle.getVideoname().length()-4));
            holder.imageView.setImageBitmap(bitmap);
        }



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<String> songs=new ArrayList<>();
Log.d("MovieListAdapter","Click on Relative layout");
                if(catagory.equals("videos") || catagory.equals("comedy")){
                    moviename=item_modle.getVideoname();
                    try {
                        JSONObject obj=moviesList.getJSONObject(position);


                        Log.d("MovieListAdapter/when cata==videos or comedy","jsonobject******"+obj);
                        Intent intent=new Intent(context, VideoPlayer_Act.class);
                        intent.putExtra("catagory",catagory);
                        intent.putExtra("selectedsong",language);
                        intent.putExtra("moviename",moviename);
                        Log.d("MovieListAdapter","selectedPlayList==="+moviename);
                        intent.putExtra("selectedPlayList",obj.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Log.d("MovieListAdapter/when cata==videos or comedy","videoname"+songs);
                }else{

                    Log.d("MovieListAdapter","catagory=="+catagory+"listofvideos==="+videolist);
                    moviename=item_modle.getVideoname();
                    Toast.makeText(context, "clicked on " +moviename, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, ProfilePage_Act.class);
                    String movieLink="http://192.168.2.254/TT/efe/"+catagory+"/"+language+"/"+item_modle.getVideoname();

                    String encodedUrl = movieLink.replace(" ", "%20");
                    intent.putExtra("movieLink",encodedUrl);
                    Log.d("movie","movie url========="+movieLink);
                    Log.d("movie","movie url========="+encodedUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    StartFrom sf=new StartFrom(context);
//                    String getData=  sf.isMovieNamePresent(MovieListAdapter.moviename);
//                    Log.d("ProfilePage/DATABASE","getDATA"+getData);
//                    if(getData.equals("yes")){
//                        alert1();
//                    }
                    context.startActivity(intent);

                }

            }
        });



    }

//    public void alert1(){
//        Log.d("ProfilePage/alert()","getting alert message");
//        AlertDialog.Builder  showMessage= new AlertDialog.Builder(context);
//
//        showMessage.setMessage("Countinue from where you stoped")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
////                            finish();
//                        StartFrom stDb=new StartFrom(context);
//                        String durationString=stDb.getDuration(MovieListAdapter.moviename);
//                        long durationMS = Long.parseLong(durationString);
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = showMessage.create();
//
//        alert.setTitle("Make Your Choice");
//        Log.d("PPA/alert","SHOWING ALERT");
//        alert.show();
//    }


    @Override
    public int getItemCount() {
        return videolist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public ImageView imageView;

        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView=itemView.findViewById(R.id.videoname);
            this.imageView=itemView.findViewById(R.id.videoicon);
            this.relativeLayout=itemView.findViewById(R.id.relativelayout);
        }
    }
}
