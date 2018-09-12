package com.dwmedios.uniconekt.view.activity.Universitario;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Videos;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoViewActivity extends YouTubeBaseActivity implements AppCompatCallback {
    public static String KEY_VIDEO_VIEWER = "key_video_viewer";
    @BindView(R.id.imageviewPlay)
    ImageView imageViewPlay;
    @BindView(R.id.imageviewFrmme)
    ImageView mImageViewFrame;
    @BindView(R.id.VideoView)
    VideoView mVideoView;

    YouTubePlayerView mYouTubePlayerView;
    @BindView(R.id.contenedorView)
    RelativeLayout mRelativeLayout;
    private Videos mVideos;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVideos = getIntent().getExtras().getParcelable(KEY_VIDEO_VIEWER);
        loadVideoYoutube();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void loadVideoYoutube() {
        mYouTubePlayerView = findViewById(R.id.VideoYoutube);
        mVideoView.setVisibility(View.GONE);
        mYouTubePlayerView.setVisibility(View.VISIBLE);
        imageViewPlay.setVisibility(View.GONE);
        String api = getString(R.string.google_api_key);
        mYouTubePlayerView.initialize(api, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(mVideos.url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(), "Ocurrió un error al tratar de reproducir el video.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
