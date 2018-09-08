package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.PaqueteAsesorResponse;
import com.dwmedios.uniconekt.data.service.response.VentaPaqueteAsesorResponse;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.VentaPaqueteAsesor;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.PaquetesAsesorViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaquetesAsesorPresenter {
    private PaquetesAsesorViewController mPaquetesAsesorViewController;
    private ClientService mClientService;
    private Context mContext;
    private AllController mAllController;


    public PaquetesAsesorPresenter(PaquetesAsesorViewController mPaquetesAsesorViewController, Context mContext) {
        this.mPaquetesAsesorViewController = mPaquetesAsesorViewController;
        this.mContext = mContext;
        this.mClientService = new ClientService(this.mContext);
        this.mAllController = new AllController(this.mContext);
    }

    public void getPaquetes() {
        mPaquetesAsesorViewController.OnLoading(true);
        mClientService.getAPI().getPaquetesAsesores().enqueue(new Callback<PaqueteAsesorResponse>() {
            @Override
            public void onResponse(Call<PaqueteAsesorResponse> call, Response<PaqueteAsesorResponse> response) {
                mPaquetesAsesorViewController.OnLoading(false);
                PaqueteAsesorResponse res = response.body();
                switch (res.status) {
                    case 1:
                        mPaquetesAsesorViewController.Onsucces(res.mPaqueteAsesors);
                        break;
                    case 0:
                        mPaquetesAsesorViewController.Onfailed(res.mensaje);
                        break;
                    case -1:
                        mPaquetesAsesorViewController.Onfailed(error);
                        break;
                }
            }

            @Override
            public void onFailure(Call<PaqueteAsesorResponse> call, Throwable t) {
                mPaquetesAsesorViewController.OnLoading(true);
                mPaquetesAsesorViewController.Onfailed(error);
            }
        });
    }

    public void saveVenta(VentaPaqueteAsesor mVentaPaqueteAsesor) {
        mPaquetesAsesorViewController.OnLoading(true);
        String json = Utils.ConvertModelToStringGson(mVentaPaqueteAsesor);
        if (json != null) {
            mClientService.getAPI().saveVentaPaqueteAsesor(json).enqueue(new Callback<VentaPaqueteAsesorResponse>() {
                @Override
                public void onResponse(Call<VentaPaqueteAsesorResponse> call, Response<VentaPaqueteAsesorResponse> response) {
                    mPaquetesAsesorViewController.OnLoading(false);
                    VentaPaqueteAsesorResponse res = response.body();
                    switch (res.status) {
                        case 1:
                            mPaquetesAsesorViewController.OnsuccesVenta(res.mVentaPaqueteAsesor);
                            mPaquetesAsesorViewController.onfailedVenta(res.mensaje);
                            break;
                        case 0:
                            mPaquetesAsesorViewController.onfailedVenta(res.mensaje);
                            break;
                        case -1:
                            mPaquetesAsesorViewController.onfailedVenta(error2);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<VentaPaqueteAsesorResponse> call, Throwable t) {
                    mPaquetesAsesorViewController.OnLoading(false);
                    mPaquetesAsesorViewController.onfailedVenta(error2);
                }
            });
        } else {
            mPaquetesAsesorViewController.OnLoading(false);
            mPaquetesAsesorViewController.onfailedVenta(error2);
        }
    }

    public VentaPaqueteAsesor getPaqueteAsesor() {
        return mAllController.getPaqueteAsesor();
    }

    public Persona getDatosPersona() {
        return mAllController.getDatosPersona();
    }

    public boolean saveVentaPaquete(VentaPaqueteAsesor mVentaPaqueteAsesor) {
        return mAllController.SaveVentaPaqueteAsesor(mVentaPaqueteAsesor);
    }

    private String error = "No se encontraron paquetes disponibles";
    private String error2 = "Ocurrió un error al procesar el pago";
}
