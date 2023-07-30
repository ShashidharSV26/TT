package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfilePage_Act extends AppCompatActivity implements Player.Listener {

    PlayerView playerView,popupPlayerView;
    SimpleExoPlayer exoPlayer;

    String videoURL = null;
    Handler handler;
    Runnable progressUpdateRunnable;

   String videoLink="http://192.168.2.254/efe/system32/generalMessages/welcomeImage.mp4";

    boolean flag=true;
    long startingPosition=0;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
    String startTime=null;
    String endTime=null;
    long durationMS=0;
    private boolean seekToExecuted = false;

    private String video = null;

//    SharedPreferences pref=getSharedPreferences("AllMovies",MODE_PRIVATE);
//    SharedPreferences.Editor editor = pref.edit();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        playerView = findViewById(R.id.introplayer);
        popupPlayerView = findViewById(R.id.popup_player_view);
        popupPlayerView.setVisibility(View.GONE);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent=getIntent();
        videoURL= intent.getStringExtra("movieLink");


        if(videoURL==null){

            videoURL="http://192.168.2.254/efe/system32/generalMessages/welcomeImage.mp4";
//                playVideo(videoURL);
           video="profilvideo";
            playByProgressBar(videoURL,0);
        }else{
            //if you don't want any ads then uncomment this method
//            playVideo(videoURL);
            startTime = dateFormat.format(calendar.getTime());
            video="movie";
            Log.d("PPA/video","video==="+video);
            if(video.equals("movie")) {
                SharedPreferences pref=getSharedPreferences("AllMovies",MODE_PRIVATE);
                Boolean check= pref.contains(MovieListAdapter.moviename);
                if(check){
                    alertMessage();
                }else{
                    playByProgressBar(videoURL,0);
                }

               /* //It will check selected movie is already watched or not
                StartFrom sf = new StartFrom(getApplicationContext());
                String getData = sf.isMovieNamePresent(MovieListAdapter.moviename);
                Log.d("ProfilePage/DATABASE", "getDATA" + getData);

                //Yes if watched, no if not watched
                if (getData.equals("yes")) {
                    //if watched it will ask for continue watching or not
                    alertMessage();
                } else if (getData.equals("no")) {
                    //if not watched video seek from 0
                    playByProgressBar(videoURL,0);
                }*/
            }
        }
    }


    private void playByProgressBar(String url,long duration) {


        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

// Add periodic position update listener
        handler = new Handler();
        progressUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                long currentPosition = exoPlayer.getCurrentPosition();
                Log.d("player","currentPosition"+currentPosition);
                Log.d("player","startingPosition"+startingPosition);

                if(Math.abs(currentPosition-startingPosition)>=10*60*1000){
                    // Pause the main video
                    exoPlayer.pause();
                    playerView.setVisibility(View.GONE);

                    // Show the popup screen with another video
                    showPopupScreen();
                    popupPlayerView.setVisibility(View.VISIBLE);
                    startingPosition=currentPosition;
                } else {
                    // Schedule the next position check after  minutes
                    handler.postDelayed(this, 10000);
                    Log.d("Player****","second**");
                }
            }
        };
            Log.d("Player****","first*");
            handler.postDelayed(progressUpdateRunnable, 1000);

        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        Log.d("player","videoDuration"+exoPlayer.getDuration());
        Log.d("PPA/seek","seekToExecute======"+seekToExecuted);
        Log.d("PPA/seek","seekToDuration======"+durationMS);
        if (!seekToExecuted) {
            exoPlayer.seekTo(duration);
            seekToExecuted = true;
        }
        exoPlayer.play();


        exoPlayer.addListener(new Player.Listener() {
            private long lastPlaybackPosition = 0;
            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                    // Video was skipped by seeking
                    long currentPosition = newPosition.contentPositionMs;
                    long timeElapsed = Math.abs(currentPosition - lastPlaybackPosition);

                    // Check if the video was skipped by more than 10 minutes (600,000 milliseconds)
                    long tenMinutesInMillis = 10 * 60 * 1000;
                    if (timeElapsed > tenMinutesInMillis) {
                        Log.d("ExoPlayerListener", "Video was skipped by ***************************more than 10 minutes");
                        // Pause the main video
                        exoPlayer.pause();
                        playerView.setVisibility(View.GONE);
                        // Show the popup screen with another video
                        showPopupScreen();
                        popupPlayerView.setVisibility(View.VISIBLE);
                    }
                }

                // Update lastPlaybackPosition with the current position
                lastPlaybackPosition = newPosition.contentPositionMs;
            }
        });



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

    }
    int i=-1;
    private void showPopupScreen() {

        int[] adsArray = {R.raw.ad1, R.raw.ad2, R.raw.ad4, R.raw.ad5, R.raw.ad6, R.raw.ad7, R.raw.ad8, R.raw.ad9, R.raw.ad2, R.raw.ad3, R.raw.ad4, R.raw.ad5, R.raw.ad6, R.raw.ad7, R.raw.ad8, R.raw.ad9,R.raw.ad1, R.raw.ad2, R.raw.ad4, R.raw.ad5};

          ++i;
        // Create a new ExoPlayer instance for the popup screen video
        SimpleExoPlayer popupPlayer = new SimpleExoPlayer.Builder(this).build();
        popupPlayerView.setPlayer(popupPlayer);

        // Set the media item for the popup screen video
        Uri rawVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + adsArray[i]);
        MediaItem popupMediaItem = MediaItem.fromUri(rawVideoUri);
        popupPlayer.setMediaItem(popupMediaItem);
        popupPlayer.prepare();
        popupPlayer.play();

        // Add a listener to detect when the popup screen video completes
        popupPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    // The popup screen video has completed

                    // Resume the main video
                    handler.postDelayed(progressUpdateRunnable, 10000);
                    startingPosition=exoPlayer.getCurrentPosition();
                    Log.d("Player****","3rd***");
                    popupPlayerView.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);

                    exoPlayer.play();

                    // Release the resources of the popupPlayer
                    popupPlayer.release();
                }
            }
        });

    }


    public void playVideo(String url) {
        //creating the instance of exoplayer using ExoPlayer.Builder, which provides a range of customization options
        exoPlayer = new SimpleExoPlayer.Builder(this).build();

        // Bind the player to the view.
        playerView.setPlayer(exoPlayer);

        //To play a piece of media you need to build a corresponding MediaItem
        MediaItem mediaItem = MediaItem.fromUri(videoURL);
        String media = mediaItem.toString();
        Log.d("media", "==========" + media);

        // Set the media item to be played
        exoPlayer.addMediaItem(mediaItem);

        // Prepare the player.
        exoPlayer.prepare();

        // Start the playback.
        exoPlayer.play();
        exoPlayer.next();
        exoPlayer.previous();
    }

    private void goToNextActivity() {
        // Start your next activity here
        if(video.equals("profilvideo")) {
            handler.removeCallbacks(progressUpdateRunnable);
            Log.d("VideoPlayer","Stoped Watching Video");
            finish();
            Intent intent = new Intent(this, Home_Act.class);
            startActivity(intent);
        }else if(video.equals("movie")){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            endTime = dateFormat.format(calendar.getTime());
            storeWatchedDetails();

            recordMovieDuration();
            handler.removeCallbacks(progressUpdateRunnable);
            Log.d("VideoPlayer","Stoped Watching Video");
            finish();

        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(video.equals("movie")) {
            Log.d("ProfilePage", "video stoped by back button");
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            endTime = dateFormat.format(calendar.getTime());
            //it will store video watched details
            storeWatchedDetails();
            //it will store videoName and Duration for next time when again watching-->stored in SharedPreferences and SQLite DB
            recordMovieDuration();
            exoPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(progressUpdateRunnable);
    }

    public void recordMovieDuration() {
        SharedPreferences pref=getSharedPreferences("AllMovies",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(MovieListAdapter.moviename,exoPlayer.getCurrentPosition());
        editor.apply();
       /* StartFrom recordDB=new StartFrom(this);
        String duration= String.valueOf(exoPlayer.getCurrentPosition());
        recordDB.saveOrUpdateDuration(MovieListAdapter.moviename,duration);*/
    }

    private void storeWatchedDetails() {
        UserData uDB=new UserData(this);
        uDB.addData(MovieList_Act.catagory,MovieList_Act.maintitle,MovieListAdapter.moviename,null,startTime,endTime);
    }

    public void alertMessage() {
        Log.d("ProfilePage/alert()", "getting alert message");
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfilePage_Act.this);
        alert.setTitle("Make Your Choice");
        alert.setMessage("Continue from where you stopped")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pref=getSharedPreferences("AllMovies",MODE_PRIVATE);
                        Long duration= pref.getLong(MovieListAdapter.moviename,0);
                        playByProgressBar(videoURL,duration);

                       /* StartFrom stDb=new StartFrom(getApplicationContext());
                     String durationString=stDb.getDuration(MovieListAdapter.moviename);
                        Log.d("PPA/==","durationString--"+durationString);
                        durationMS = Long.parseLong(durationString);
                        Log.d("PPA/runn","WatchedDuration=="+durationMS);
                        Toast.makeText(ProfilePage_Act.this, "yes", Toast.LENGTH_SHORT).show();
                        playByProgressBar(videoURL,durationMS);*/
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag = false;
                        Toast.makeText(ProfilePage_Act.this, "NO", Toast.LENGTH_SHORT).show();
                        playByProgressBar(videoURL,0);
                        dialog.cancel();

                    }
                });

        AlertDialog alert1 = alert.create();
        Log.d("PPA/alert", "SHOWING ALERT");
        alert1.show();
    }


}