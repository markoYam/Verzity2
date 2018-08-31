package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.NotificacionesResponse;
import com.dwmedios.uniconekt.model.Favoritos;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.viewmodel.NotificacionesViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class NotificacionesPresenter {
    private ClientService mClientService;
    private NotificacionesViewController mNotificacionesViewController;
    private AllController mAllController;
    private Context mContext;

    public NotificacionesPresenter(NotificacionesViewController mNotificacionesViewController, Context mContext) {
        this.mNotificacionesViewController = mNotificacionesViewController;
        this.mClientService = new ClientService(mContext);
        this.mContext = mContext;
        mAllController = new AllController(this.mContext);
    }

  /*  public void ConsultarNotificaciones() {
        mNotificacionesViewController.Onloading(true);
        Persona persona = mAllController.getDatosPersona();
        if (persona != null) {
            String json = ConvertModelToStringGson(persona);
            if (json != null) {
                Log.e("json not consultar",json);
                mClientService
                        .getAPI()
                        .ConsultarNotificaciones(json)
                        .enqueue(new Callback<NotificacionesResponse>() {
                            @Override
                            public void onResponse(Call<NotificacionesResponse> call, Response<NotificacionesResponse> response) {
                                mNotificacionesViewController.Onloading(false);
                                NotificacionesResponse res = response.body();
                                if (res != null) {
                                    if (res.status == 1) {
                                        mNotificacionesViewController.Onsucces(res.mNotificacionesList);
                                    } else {
                                        mNotificacionesViewController.OnFailed(res.mensaje);
                                    }
                                } else {
                                    mNotificacionesViewController.OnFailed(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<NotificacionesResponse> call, Throwable t) {
                                mNotificacionesViewController.Onloading(false);
                                mNotificacionesViewController.OnFailed(ERROR_CONECTION);
                            }
                        });
            } else {
                mNotificacionesViewController.Onloading(false);
                mNotificacionesViewController.OnFailed(ERROR_CONECTION);
            }

        } else {
            mNotificacionesViewController.Onloading(false);
            mNotificacionesViewController.OnFailed(ERROR_CONECTION);
        }
    }*/
}
