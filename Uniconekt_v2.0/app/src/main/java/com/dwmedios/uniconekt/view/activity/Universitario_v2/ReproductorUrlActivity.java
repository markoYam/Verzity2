package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.service.broadcast.audioController;
import com.dwmedios.uniconekt.model.Videos;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.internal.Utility.isNullOrEmpty;

public class ReproductorUrlActivity extends AppCompatActivity {
    public static String url = "http://verzity.dwmedios.com/site/Upload//Videos/vnwiz41r.elk.mp4";
    @BindView(R.id.VideoView)
    VideoView mVideoView;
    @BindView(R.id.imageViewPreview)
    ImageView mImageView;
    @BindView(R.id.progresVideo)
    ProgressBar mProgressBar;
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;
    private Videos mVideos;
    private AudioManager audioManager;
    int position = 0;
    audioController mAudioController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setContentView(R.layout.activity_reproductor_url);
        ButterKnife.bind(this);
        cargarPreview();
        mVideos = new Videos();
        mVideos.ruta = url;
        mAudioController = new audioController(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mAudioController);
        initControls();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadVideo();
    }

    @Override
    protected void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(mAudioController);
        super.onDestroy();
    }

    private void initControls() {
        try {

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mSeekBar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            mSeekBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadVideo() {
        if (!isNullOrEmpty(mVideos.ruta)) {
            //Cambiar a url real

            mVideoView.setVideoURI(Uri.parse(url));
            mVideoView.requestFocus();
            MediaController mediaController = new MediaController(this);
            mVideoView.setMediaController(mediaController);
            mVideoView.setOnPreparedListener(mOnPreparedListener);

        }
    }

    private void cargarPreview() {
        if (mImageView.getVisibility() == View.VISIBLE) {
            Glide.with(getApplicationContext())
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mImageView.setImageResource(R.drawable.defaultt);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(mImageView);
        } else {
            mImageView.setImageResource(R.drawable.defaultt);
        }
    }

    MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(final MediaPlayer mp) {
            //Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            in.setDuration(500);
            mImageView.startAnimation(in);
            if (mImageView.getVisibility() == View.VISIBLE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        mImageView.setVisibility(View.GONE);
                        mp.setLooping(true);
                        if (position == 0) {
                            mVideoView.start();
                        } else {
                            mVideoView.pause();
                        }
                    }
                }, 500);
            } else {
                mp.setLooping(true);
                if (position == 0) {
                    mVideoView.start();
                } else {
                    mVideoView.pause();
                }
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
        if (getSupportActionBar().isShowing()) {
            switchTitleBar(true);
        } else {
            switchTitleBar(false);
        }
    }

    private void switchTitleBar(boolean show) {
        try {
            ActionBar mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                if (show) {
                    mActionBar.show();
                } else {
                    mActionBar.hide();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();

    }
}
