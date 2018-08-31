package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.NotificacionesUniResponse;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.viewmodel.NotificacionesUniversidadViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;

public class NotificacionesUniPresenter {
    private Context mContext;
    private ClientService mClientService;
    private NotificacionesUniversidadViewController mNotificacionesUniversidadViewController;
    private AllController mAllController;

    public NotificacionesUniPresenter(Context mContext, NotificacionesUniversidadViewController mNotificacionesUniversidadViewController) {
        this.mContext = mContext;
        this.mNotificacionesUniversidadViewController = mNotificacionesUniversidadViewController;
        this.mClientService = new ClientService(this.mContext);
        this.mAllController= new AllController(this.mContext);
    }

    public void mNotificacionesUniPresenter() {
        mNotificacionesUniversidadViewController.Onloading(true);
        Persona mPersona = mAllController.getDatosPersona();
        String json = ConvertModelToStringGson(mPersona);
        if (json != null) {
            mClientService.getAPI().ConsultarNotUniversidad(json).enqueue(new Callback<NotificacionesUniResponse>() {
                @Override
                public void onResponse(Call<NotificacionesUniResponse> call, Response<NotificacionesUniResponse> response) {
                    mNotificacionesUniversidadViewController.Onloading(false);
                    NotificacionesUniResponse res = response.body();
                    switch (res.status) {
                        case 1:
                            mNotificacionesUniversidadViewController.Onsucces(res.mNotificacionUniversidads);
                            break;
                        case 0:
                            mNotificacionesUniversidadViewController.Onfailed(res.mensaje);
                            break;
                        case -1:
                            mNotificacionesUniversidadViewController.Onfailed("No se encontraron elementos");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<NotificacionesUniResponse> call, Throwable t) {
                    mNotificacionesUniversidadViewController.Onloading(false);
                    mNotificacionesUniversidadViewController.Onfailed("No se encontraron elementos");
                }
            });
        } else {
            mNotificacionesUniversidadViewController.Onloading(false);
            mNotificacionesUniversidadViewController.Onfailed("No se encontraron elementos");
        }
    }
}
