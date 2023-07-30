package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoPlayer_Act extends AppCompatActivity {

    PlayerView playerView,popupPlayerView;
    SimpleExoPlayer exoPlayer;


    String catagory,language,movieName;
    String selectedPlayList;
    ArrayList<String> listOfMovies=new ArrayList<>();
    ArrayList<String> videosURLs=new ArrayList<>();

    ArrayList<String> songs=new ArrayList<>();
    TextView videoName;
    String currentvideoName;


    String startTime="null";
    String endTime=null;

//    SharedPreferences pref=getSharedPreferences("AllVideos",MODE_PRIVATE);
//    SharedPreferences.Editor editor = pref.edit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.videoplayer);
        videoName=findViewById(R.id.videoNameTextView);
        popupPlayerView = findViewById(R.id.popup_player_view);  // Replace with your actual popup player view ID

        Intent intent=getIntent();
        catagory=intent.getStringExtra("catagory");
        language=intent.getStringExtra("selectedsong");
        movieName=intent.getStringExtra("moviename");
        selectedPlayList=intent.getStringExtra("selectedPlayList");



        try {
            JSONObject jsonObject=new JSONObject(selectedPlayList);
            JSONArray array=jsonObject.getJSONArray(movieName);
            for(int i=0;i<array.length(); i++) {
              String song= array.getString(i);
              songs.add(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("VideoplayerAct","catagory=========="+catagory);
        Log.d("VideoplayerAct","language=========="+language);
        Log.d("videoplayerAct","playlistname=========="+movieName);
        Log.d("videoplayerAct","No.of.songs=========="+songs);

        for(int i=0; i<songs.size(); i++) {
            if(catagory.equals("videos")) {

                videosURLs.add("http://192.168.2.254/TT/efe/" + catagory + "ongs/" + language + "/" + movieName + "/" + songs.get(i).replace(" ", "%20"));
            }else{
                videosURLs.add("http://192.168.2.254/TT/efe/" + catagory + "/" + language + "/" + movieName + "/" + songs.get(i).replace(" ", "%20"));
            }
            }

        Log.d("listofmovies","urls=========="+videosURLs);

        JSONArray jsonArray = new JSONArray(listOfMovies);
        Log.d("Profilepage","=========="+jsonArray);

        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        List<MediaSource> mediaSources=new ArrayList<>();
        for(String urll:videosURLs){
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(this, Util.getUserAgent(this, "YourApplicationName")))
                    .createMediaSource(MediaItem.fromUri(Uri.parse(urll)));
            mediaSources.add(mediaSource);
        }
        ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource();
        concatenatedSource.addMediaSources(mediaSources);

        exoPlayer.setMediaSource(concatenatedSource);


//        showPopupScreen();
        Log.d("VideoPlayer------","calling showPopupScreen");
        playerView.setVisibility(View.GONE);
        playVideo();

    }

    private void playVideo() {


        exoPlayer.prepare();
        Log.d("videoPlayer/playVideo","Playing main video");

        exoPlayer.play();
        exoPlayer.next();
        exoPlayer.previous();



        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) {
                    // Video playback completed
                    Log.d("exoplayer", "videoended");
                    goToNextActivity();
                }
            }
        });



        exoPlayer.addListener(new Player.Listener(){
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    Log.d("videoPlayer/playVideo", "videoended");
                    if(startTime.equals("null")){
                        Log.d("VideoPlayer","Playing next ad");
                        exoPlayer.pause();
                        playerView.setVisibility(View.GONE);
                        popupPlayerView.setVisibility(View.VISIBLE);
                        showPopupScreen();
                    }else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                    endTime = dateFormat.format(calendar.getTime());
                    callDB();
                    recordDuration();
                        exoPlayer.pause();
                        playerView.setVisibility(View.GONE);
                        popupPlayerView.setVisibility(View.VISIBLE);
                        showPopupScreen();
                    }
                }
            }
        });

    }



    int i=-1;
    private void showPopupScreen() {
        int[] adsArray = {R.raw.ad1, R.raw.ad2, R.raw.ad4, R.raw.ad5, R.raw.ad6, R.raw.ad7, R.raw.ad8, R.raw.ad9, R.raw.ad2, R.raw.ad3, R.raw.ad4, R.raw.ad5, R.raw.ad6, R.raw.ad7, R.raw.ad8, R.raw.ad9};

        ++i;
        // Create a new ExoPlayer instance for the popup screen video
        SimpleExoPlayer popupPlayer = new SimpleExoPlayer.Builder(this).build();
        popupPlayerView.setPlayer(popupPlayer);

        // Set the media item for the popup screen video
        Uri rawVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + adsArray[i]);
        MediaItem popupMediaItem = MediaItem.fromUri(rawVideoUri);
        popupPlayer.setMediaItem(popupMediaItem);
        popupPlayer.prepare();
        Log.d("VideoPlayer","Playing add for*********"+i);
        popupPlayer.play();

        // Add a listener to detect when the popup screen video completes
        popupPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    Log.d("VideoPlayer----",i+"st  ad completed");
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                    startTime = dateFormat.format(calendar.getTime());
                    popupPlayerView.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    exoPlayer.play();
                    int currentWindowIndex = exoPlayer.getCurrentWindowIndex();
                    currentvideoName = songs.get(currentWindowIndex);
                    videoName.setText(currentvideoName);
                    // The popup screen video has completed
                    Log.d("VideoPlayer----","Calling playVideo");


//                    int currentWindowIndex = exoPlayer.getCurrentWindowIndex();
//                    if(currentWindowIndex>=0) {
//                        playVideo();
//                    }else{
//                        Log.d("exoplayer", "All videos ended");
//                        goToNextActivity();
//                    }


                    // Release the resources of the popupPlayer
                    popupPlayer.release();
                }
            }
        });

    }



    private void goToNextActivity() {
        // Start your next activity here
        Intent intent = new Intent(this, MovieList_Act.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        endTime = dateFormat.format(calendar.getTime());
        callDB();
        recordDuration();
        Log.d("videoPlayer", "video stoped by back button");

        exoPlayer.pause();
    }

    public void callDB(){
        UserData uDB=new UserData(this);
        uDB.addData(catagory,language,movieName,currentvideoName,startTime,endTime);
//        editor.putLong();
    }
    public void recordDuration() {
        StartFrom recordDB=new StartFrom(this);
        String duration= String.valueOf(exoPlayer.getCurrentPosition());
//        recordDB.addDuration(movieName,duration);
        recordDB.saveOrUpdateDuration(movieName,duration);
    }

}