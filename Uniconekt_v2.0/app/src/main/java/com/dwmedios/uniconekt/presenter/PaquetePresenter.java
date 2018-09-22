package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.PaquetesResponse;
import com.dwmedios.uniconekt.data.service.response.VentasPaquetesResponse;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.view.viewmodel.PaquetesViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class PaquetePresenter {
    private ClientService mClientService;
    private PaquetesViewController mPaquetesViewController;
    private AllController mAllController;
    private Context mContext;

    public PaquetePresenter(PaquetesViewController mPaquetesViewController, Context mContext) {
        this.mPaquetesViewController = mPaquetesViewController;
        this.mClientService = new ClientService(mContext);
        this.mContext = mContext;
        mAllController = new AllController(mContext);
    }

    public void GetPaquetes() {
        mPaquetesViewController.Onloading(true);
        mClientService
                .getAPI()
                .GetPaquetesDisponibles()
                .enqueue(new Callback<PaquetesResponse>() {
                    @Override
                    public void onResponse(Call<PaquetesResponse> call, Response<PaquetesResponse> response) {
                        PaquetesResponse res = response.body();
                        mPaquetesViewController.Onloading(false);
                        if (res != null) {
                            if (res.status == 1) {
                                mPaquetesViewController.OnSucces(res.mPaquetesList);
                            } else {
                                mPaquetesViewController.OnFailed(res.mensaje);
                            }
                        } else {
                            mPaquetesViewController.OnFailed(ERROR_CONECTION);
                        }
                    }

                    @Override
                    public void onFailure(Call<PaquetesResponse> call, Throwable t) {
                        mPaquetesViewController.Onloading(false);
                        mPaquetesViewController.OnFailed(ERROR_CONECTION);
                    }
                });
    }

    public void SaveVentapaquete(VentasPaquetes mVentasPaquetes) {
        mPaquetesViewController.Onloading(true);
        String json = ConvertModelToStringGson(mVentasPaquetes);
        if (json != null) {
            mClientService
                    .getAPI()
                    .RegistrarVentaPaquete(json)
                    .enqueue(new Callback<VentasPaquetesResponse>() {
                        @Override
                        public void onResponse(Call<VentasPaquetesResponse> call, Response<VentasPaquetesResponse> response) {
                            VentasPaquetesResponse res = response.body();
                            mPaquetesViewController.Onloading(false);
                            if (res != null) {

                                if (res.status == 1) {
                                    mPaquetesViewController.OnSuccesPaquete(res.mVentasPaquetes);
                                } else {
                                    mPaquetesViewController.OnFailed(res.mensaje);
                                }
                            } else {
                                mPaquetesViewController.OnFailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<VentasPaquetesResponse> call, Throwable t) {
                            mPaquetesViewController.Onloading(false);
                            mPaquetesViewController.OnFailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mPaquetesViewController.Onloading(false);
            mPaquetesViewController.OnFailed(ERROR_CONECTION);
        }
    }

    public void GuardarPaquete(VentasPaquetes mVentasPaquetes) {
        if (mAllController.deletePaquetes()) {
            mAllController.SavePaquete(mVentasPaquetes);
        } else {
            if (mAllController.getVentaPaquete() == null) {
                mAllController.SavePaquete(mVentasPaquetes);
            }
        }
    }

    public VentasPaquetes getPaqueteActual() {
        return mAllController.getVentaPaquete();
    }

    public Universidad getUniversidad() {
        Universidad mUniversidad = mAllController.getuniversidadPersona();
        if (mUniversidad != null) {
            Direccion mDireccion = mAllController.getDireccionUniversidad(mUniversidad.id_direccion);
            if (mDireccion != null) mUniversidad.mDireccion = mDireccion;
        }
        return mUniversidad;
    }

    public boolean borrarTodo() {
        return mAllController.clearAllTables();
    }
}
