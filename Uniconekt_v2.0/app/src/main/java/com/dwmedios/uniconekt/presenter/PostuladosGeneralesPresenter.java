package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.PostuladosGeneralesResponse;
import com.dwmedios.uniconekt.model.PostuladosGeneral;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.viewmodel.PostuladosGeneralesViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class PostuladosGeneralesPresenter {
    private ClientService mClientService;
    private PostuladosGeneralesViewController mPostuladosGeneralesViewController;
    private AllController mAllController;
    private Context mContext;

    public PostuladosGeneralesPresenter(PostuladosGeneralesViewController mPostuladosGeneralesViewController, Context mContext) {
        this.mPostuladosGeneralesViewController = mPostuladosGeneralesViewController;
        mClientService = new ClientService(mContext);
        this.mContext = mContext;
        mAllController = new AllController(this.mContext);
    }

    public void Getpostulados() {
        mPostuladosGeneralesViewController.Onloading(true);
        String json = ConvertModelToStringGson(mAllController.getuniversidadPersona());
        if (json != null) {
            mClientService
                    .getAPI()
                    .GetPostulados(json)
                    .enqueue(new Callback<PostuladosGeneralesResponse>() {
                        @Override
                        public void onResponse(Call<PostuladosGeneralesResponse> call, Response<PostuladosGeneralesResponse> response) {
                            PostuladosGeneralesResponse res = response.body();
                            mPostuladosGeneralesViewController.Onloading(false);
                            if (res != null) {
                                if (res.status == 1) {
                                    mPostuladosGeneralesViewController.Onsuccess(res.mPostuladosGenerals);
                                } else {
                                    mPostuladosGeneralesViewController.OnFailed(res.mensaje);
                                }
                            } else {
                                mPostuladosGeneralesViewController.OnFailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostuladosGeneralesResponse> call, Throwable t) {
                            mPostuladosGeneralesViewController.Onloading(false);
                            mPostuladosGeneralesViewController.OnFailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mPostuladosGeneralesViewController.Onloading(false);
            mPostuladosGeneralesViewController.OnFailed(ERROR_CONECTION);
        }
    }
}
