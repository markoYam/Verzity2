package com.dwmedios.uniconekt.view.activity.Universitario;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Videos;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.Transitions.Transisciones.TRANSITION_VIDEO;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class VideoViewActivity extends YouTubeBaseActivity implements AppCompatCallback {
    public static String KEY_VIDEO_VIEWER = "key_video_viewer";
    @BindView(R.id.imageviewPlay)
    ImageView imageViewPlay;
    @BindView(R.id.imageviewFrmme)
    ImageView mImageViewFrame;
  //  @BindView(R.id.toolbar)
   // Toolbar mToolbar;
    //@BindView(R.id.app_bar_layout)
    // android.support.design.widget.AppBarLayout mActionBar;
    @BindView(R.id.VideoView)
    VideoView mVideoView;
    // @BindView(R.id.VideoYoutube)
    YouTubePlayerView mYouTubePlayerView;
    @BindView(R.id.contenedorView)
    RelativeLayout mRelativeLayout;
    private Videos mVideos;

    private AppCompatDelegate delegate;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(this);
        //para la transicion
        //delegate=AppCompatDelegate.create(VideoViewActivity.this,this);
        //  getWindow().supportPostponeEnterTransition();
        mVideos = getIntent().getExtras().getParcelable(KEY_VIDEO_VIEWER);

        //mToolbar.setBackgroundColor(Color.BLACK);
       // mToolbar.setTitle(mVideos.nombre);

        // mActionBar.setTitle(mVideos.nombre);
        //mActionBar.setDisplayHomeAsUpEnabled(true);
        //delegate.setSupportActionBar(mToolbar);
        //delegate.getSupportActionBar().setTitle(mVideos.nombre);
        //delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //transicion

    }

    @Override
    protected void onStart() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadVideo();
        super.onStart();
    }

    private void loadVideo() {
        if (mVideos != null) {
            try {
                if (mVideos.ruta != null && !mVideos.ruta.isEmpty()) {
                    String url = getUrlImage(mVideos.ruta, getApplicationContext());
                    String transaction = getIntent().getExtras().getString(TRANSITION_VIDEO);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mVideoView.setTransitionName(transaction);
                    }
                    // supportStartPostponedEnterTransition();
                    mVideoView.setVideoURI(Uri.parse(url));
                    mVideoView.requestFocus();
                    MediaController mediaController = new MediaController(this);
                    mVideoView.setMediaController(mediaController);
                    mVideoView.setOnPreparedListener(mOnPreparedListener);
                } else if (mVideos.url != null && !mVideos.url.isEmpty()) {

                    loadVideoYoutube(mVideos.url);
                }

            } catch (Exception ex) {
                ex.toString();
            }
        }
    }

    public void loadVideoYoutube(final String url) {
        mYouTubePlayerView = findViewById(R.id.VideoYoutube);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mVideoView.setVisibility(View.GONE);
        mYouTubePlayerView.setVisibility(View.VISIBLE);
        imageViewPlay.setVisibility(View.GONE);
        String transaction = getIntent().getExtras().getString(TRANSITION_VIDEO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mYouTubePlayerView.setTransitionName(transaction);
        }
        // supportStartPostponedEnterTransition();
        String api = getString(R.string.google_api_key);
        mYouTubePlayerView.initialize(api, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(), "Ocurrió un error al tratar de reproducir el video.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isFullscreen = false;
    private int cachedHeight;
    ViewGroup.LayoutParams temp;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
     /*   if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switchTitleBar(false);

            if (mWebView.getVisibility() == View.VISIBLE) {
                temp = mWebView.getLayoutParams();
                DisplayMetrics metrics = this.getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                mWebView.setLayoutParams(params);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            switchTitleBar(true);

            if (mWebView.getVisibility() == View.VISIBLE) {
                if (temp != null) {
                    mWebView.setLayoutParams(temp);

                }
            }
        }*/
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

    private int position = 0;
    MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            imageViewPlay.setVisibility(View.GONE);
            mp.setLooping(true);
            if (position == 0) {
                mVideoView.start();
            } else {
                mVideoView.pause();
            }

        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Position",
                mVideoView.getCurrentPosition());
        mVideoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        mVideoView.seekTo(position);
        if (delegate.getSupportActionBar().isShowing()) {
            switchTitleBar(true);
        } else {
            switchTitleBar(false);
        }
    }

    private void switchTitleBar(boolean show) {
        try {
            android.support.v7.app.ActionBar supportActionBar = delegate.getSupportActionBar();
            if (supportActionBar != null) {
                if (show) {
                    supportActionBar.show();
                } else {
                    supportActionBar.hide();
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        switchTitleBar(true);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        switchTitleBar(true);
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
