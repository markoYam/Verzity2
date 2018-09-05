package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.AsesoresResponse;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.AsesoresViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsesorPresenter {
    private AsesoresViewController mAsesoresViewController;
    private Context mContext;
    private ClientService mClientService;
    private AllController mAllController;

    public AsesorPresenter(AsesoresViewController mAsesoresViewController, Context mContext) {
        this.mAsesoresViewController = mAsesoresViewController;
        this.mContext = mContext;
        this.mClientService = new ClientService(this.mContext);
        this.mAllController = new AllController(this.mContext);
    }

    public void getAsesores() {
        mAsesoresViewController.OnLoading(true);
        mClientService.getAPI().getAsesores().enqueue(new Callback<AsesoresResponse>() {
            @Override
            public void onResponse(Call<AsesoresResponse> call, Response<AsesoresResponse> response) {
                mAsesoresViewController.OnLoading(false);
                AsesoresResponse res = response.body();
                switch (res.status) {
                    case 1:
                        mAsesoresViewController.Onsucces(res.mPersonaList);
                        break;
                    case 0:
                        mAsesoresViewController.Onfailed(res.mensaje);
                        break;
                    case -1:
                        mAsesoresViewController.Onfailed(error);
                        break;
                }
            }

            @Override
            public void onFailure(Call<AsesoresResponse> call, Throwable t) {
                mAsesoresViewController.OnLoading(false);
                mAsesoresViewController.Onfailed(error);
            }
        });
    }

    public void getMisAsesores() {
        mAsesoresViewController.OnLoading(true);
        Persona mPersona = mAllController.getDatosPersona();
        if (mPersona != null) {
            String json = Utils.ConvertModelToStringGson(mPersona);
            if (json != null) {
                mClientService.getAPI().getMisAsesores(json).enqueue(new Callback<AsesoresResponse>() {
                    @Override
                    public void onResponse(Call<AsesoresResponse> call, Response<AsesoresResponse> response) {
                        mAsesoresViewController.OnLoading(false);
                        AsesoresResponse res = response.body();
                        switch (res.status) {
                            case 1:
                                mAsesoresViewController.Onsucces(res.mPersonaList);
                                break;
                            case 0:
                                mAsesoresViewController.Onfailed(res.mensaje);
                                break;
                            case -1:
                                mAsesoresViewController.Onfailed(error);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<AsesoresResponse> call, Throwable t) {
                        mAsesoresViewController.OnLoading(false);
                        mAsesoresViewController.Onfailed(error);
                    }
                });
            } else {
                mAsesoresViewController.OnLoading(false);
                mAsesoresViewController.Onfailed(error);
            }
        } else {
            mAsesoresViewController.OnLoading(false);
            mAsesoresViewController.Onfailed(error);
        }
    }


    private String error = "No se encontraron asesores";
}
