package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.NotificacionesResponse;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.NotificacionUniversitarioViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionUniversitarioPresenter {
    private ClientService mClientService;
    private Context mContext;
    private AllController mAllController;
    private NotificacionUniversitarioViewController mUniversitarioViewController;

    public NotificacionUniversitarioPresenter(Context mContext, NotificacionUniversitarioViewController mUniversitarioViewController) {
        this.mContext = mContext;
        this.mUniversitarioViewController = mUniversitarioViewController;
        this.mClientService = new ClientService(this.mContext);
        this.mAllController = new AllController(this.mContext);
    }

    public void getNotificaciones() {
        mUniversitarioViewController.OnLoading(true);
        Persona mPersona = mAllController.getDatosPersona();
        mPersona.dispositivosList=mAllController.getDispositivo();
        String json = Utils.ConvertModelToStringGson(mPersona);
        if (json != null) {
            mClientService.getAPI().getNotificacionesUniversitario(json).enqueue(new Callback<NotificacionesResponse>() {
                @Override
                public void onResponse(Call<NotificacionesResponse> call, Response<NotificacionesResponse> response) {
                    mUniversitarioViewController.OnLoading(false);
                    NotificacionesResponse res = response.body();
                    switch (res.status) {
                        case 1:
                            mUniversitarioViewController.Onsucces(res.mNotificacionesList);
                            break;
                        case 0:
                            mUniversitarioViewController.OnFailed(res.mensaje);
                            break;
                        case -1:
                            mUniversitarioViewController.OnFailed(error);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<NotificacionesResponse> call, Throwable t) {
                    mUniversitarioViewController.OnLoading(false);
                    mUniversitarioViewController.OnFailed(error);
                }
            });
        } else {
            mUniversitarioViewController.OnLoading(false);
            mUniversitarioViewController.OnFailed(error);
        }
    }

    private String error = "No se encontraron elementos";
}
