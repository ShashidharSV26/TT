package com.example.tt;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Window;
import android.widget.VideoView;

import androidx.fragment.app.DialogFragment;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class AdsPopUp extends DialogFragment {
    private Dialog popupDialog;
    private VideoView videoView;
    private String videoURL;


    public AdsPopUp(Context context, String videoURL) {
        this.videoURL = videoURL;
        popupDialog = new Dialog(context);
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setCancelable(false);
        popupDialog.setContentView(R.layout.popup_layout);

        videoView = popupDialog.findViewById(R.id.video_view);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                popupDialog.dismiss();
            }
        });

        Uri videoUri = Uri.parse(videoURL);
        videoView.setVideoURI(videoUri);
    }

    public void show() {
        if (videoView != null && !videoView.isPlaying()) {
            videoView.start();
        }
        popupDialog.show();
    }

    public void dismiss() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        popupDialog.dismiss();
    }

}
