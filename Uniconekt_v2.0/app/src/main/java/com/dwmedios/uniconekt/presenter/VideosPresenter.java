package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.provider.MediaStore;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.VideoResponse;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Videos;
import com.dwmedios.uniconekt.view.viewmodel.VideoViewController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class VideosPresenter {
    private ClientService mClientService;
    private VideoViewController mVideoViewController;

    public VideosPresenter(VideoViewController mVideoViewController, Context mContext) {
        this.mVideoViewController = mVideoViewController;
        this.mClientService = new ClientService(mContext);
    }

    public void GetVideos(Universidad mUniversidad) {
        mVideoViewController.Onloading(true);
        String json = ConvertModelToStringGson(mUniversidad);
        if (json != null) {
            mClientService
                    .getAPI()
                    .GetVideos(json)
                    .enqueue(new Callback<VideoResponse>() {
                        @Override
                        public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                            VideoResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mVideoViewController.Onloading(false);
                                    mVideoViewController.Onsucces(res.mvideosList,0);

                                } else {
                                    mVideoViewController.Onloading(false);
                                    mVideoViewController.Onfailed(res.mensaje);
                                }
                            } else {
                                mVideoViewController.Onloading(false);
                                mVideoViewController.Onfailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<VideoResponse> call, Throwable t) {
                            mVideoViewController.Onloading(false);
                            mVideoViewController.Onfailed(ERROR_CONECTION);
                        }
                    });

        } else {
            mVideoViewController.Onloading(false);
            mVideoViewController.Onfailed(ERROR_CONECTION);

        }
    }

    public void searcVideos(List<Videos> mVideosList, String criterio) {
        if (mVideosList != null && mVideosList.size() > 0) {
            List<Videos> temp = new ArrayList<>();
            for (int i = 0; i < mVideosList.size(); i++) {
                if (mVideosList.get(i).nombre.toLowerCase().contains(criterio.toLowerCase())) {
                    temp.add(mVideosList.get(i));
                }
            }
            if (temp != null && temp.size() > 0) {
                mVideoViewController.Onsucces(temp,1);
            } else {
                mVideoViewController.EmptyRecyclerView();
            }
        } else {
            mVideoViewController.EmptyRecyclerView();
        }
    }
}
