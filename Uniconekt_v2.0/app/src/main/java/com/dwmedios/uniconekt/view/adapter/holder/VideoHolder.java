package com.dwmedios.uniconekt.view.adapter.holder;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.service.api.apiConfiguraciones;
import com.dwmedios.uniconekt.data.service.response.YoutubeResponse;
import com.dwmedios.uniconekt.model.Videos;
import com.dwmedios.uniconekt.view.adapter.VideoAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class VideoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cardviewVideo)
    CardView mCardView;
    @BindView(R.id.texviewNombreVideo)
    TextView mTextViewNombre;
    @BindView(R.id.textViewDescripcionVideo)
    TextView mTextViewDescripcion;
    @BindView(R.id.imageviewPlay)
    ImageView mImageViewPlay;

    @BindView(R.id.imageviewFrmme)
    ImageView InframeVideo;

    public VideoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setIsRecyclable(false);
    }

    public VideoAdapter.onclick mOnclick;
    public Videos mVideos;

    public void configureVideo(final Videos mVideos, final VideoAdapter.onclick mOnclick) {
        this.mOnclick = mOnclick;
        this.mVideos = mVideos;

        //agregado trasicion Video
        // Dw_setTransaction(InframeVideo, mVideos.nombre);
        ViewCompat.setTransitionName(InframeVideo, mVideos.nombre);


        if (mVideos.nombre != null) mTextViewNombre.setText(mVideos.nombre);
        if (mVideos.descripcion != null) mTextViewDescripcion.setText(mVideos.descripcion);else mTextViewDescripcion.setText("");
        if (mVideos.ruta != null && !mVideos.ruta.isEmpty()) {
            String url = getUrlImage(mVideos.ruta,itemView.getContext());
            Glide.with(itemView)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            InframeVideo.setImageResource(R.drawable.defaultt);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(InframeVideo);

        } else if (mVideos.url != null && !mVideos.url.isEmpty()) {
            taskVideoYutube(mVideos.url,InframeVideo);
        }
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnclick != null) {
                    mOnclick.onclick(mVideos, InframeVideo);
                }
            }
        });
    }

    public void taskVideoYutube(final String codeYoutube, final ImageView mImageView) {

        Retrofit mRetrofit;

        String url = "https://www.youtube.com/";

        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiConfiguraciones mApiConfiguraciones = mRetrofit.create(apiConfiguraciones.class);
        mApiConfiguraciones.getConfiguraciones("http://www.youtube.com/watch?v=" + codeYoutube + "&format=json").enqueue(new Callback<YoutubeResponse>() {
            @Override
            public void onResponse(Call<YoutubeResponse> call, Response<YoutubeResponse> response) {
                YoutubeResponse mYoutubeResponse = response.body();
                if (mYoutubeResponse != null) {
                    ImageLoader.getInstance().displayImage(mYoutubeResponse.preview, mImageView, OptionsImageLoaderItems);
                }
            }

            @Override
            public void onFailure(Call<YoutubeResponse> call, Throwable t) {

            }
        });
    }
}
