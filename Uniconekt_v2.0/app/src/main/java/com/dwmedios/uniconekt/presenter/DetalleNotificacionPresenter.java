package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.NotificacionesResponse;
import com.dwmedios.uniconekt.data.service.response.PostuladoGeneralResponse;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.NotificacionStatus;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.view.viewmodel.DetalleNotificacionViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class DetalleNotificacionPresenter {
    private ClientService mClientService;
    private DetalleNotificacionViewController mDetalleNotificacionViewController;
    private AllController mAllController;

    public DetalleNotificacionPresenter(DetalleNotificacionViewController mDetalleNotificacionViewController, Context mContext) {
        this.mDetalleNotificacionViewController = mDetalleNotificacionViewController;
        this.mClientService = new ClientService(mContext);
        this.mAllController = new AllController(mContext);
    }

    public void getDetalleNotificacion(Notificaciones mNotificaciones) {
        mDetalleNotificacionViewController.Onloading(true);
        Dispositivo tempDis = mAllController.getDispositivoPersona();
        if (tempDis != null) {
            NotificacionStatus temp = new NotificacionStatus();
            temp.id_notificacion = mNotificaciones.id;
            temp.id_dispositivo = tempDis.id;

            String json = ConvertModelToStringGson(temp);
            Log.e("Ver not",json);
            if (json != null) {
                mClientService
                        .getAPI()
                        .GetDetalleNotificacion(json)
                        .enqueue(new Callback<PostuladoGeneralResponse>() {
                            @Override
                            public void onResponse(Call<PostuladoGeneralResponse> call, Response<PostuladoGeneralResponse> response) {
                                mDetalleNotificacionViewController.Onloading(false);
                                PostuladoGeneralResponse res= response.body();
                                if(res!=null)
                                {
                                    if(res.status==1)
                                    {
                                        mDetalleNotificacionViewController.Onsucces(res.mPostuladosGeneral);
                                    }else
                                    {
                                        mDetalleNotificacionViewController.Onfailed(res.mensaje);
                                    }
                                }else
                                {
                                    mDetalleNotificacionViewController.Onfailed(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<PostuladoGeneralResponse> call, Throwable t) {
                                mDetalleNotificacionViewController.Onloading(false);
                                mDetalleNotificacionViewController.Onfailed(ERROR_CONECTION);
                            }
                        });
            } else {
                mDetalleNotificacionViewController.Onloading(false);
                mDetalleNotificacionViewController.Onfailed(ERROR_CONECTION);
            }
        } else {
            mDetalleNotificacionViewController.Onloading(false);
            mDetalleNotificacionViewController.Onfailed(ERROR_CONECTION);
        }
    }
}
