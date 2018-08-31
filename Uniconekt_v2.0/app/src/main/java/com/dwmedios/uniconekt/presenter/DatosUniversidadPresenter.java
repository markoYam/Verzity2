package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.UniversidadDetalleResponse;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.viewmodel.DatosUniversidadViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class DatosUniversidadPresenter {
    private AllController mAllController;
    private Context mContext;
    private DatosUniversidadViewController mDatosUniversidadViewController;
    private ClientService mClientService;

    public DatosUniversidadPresenter(Context mContext, DatosUniversidadViewController mDatosUniversidadViewController) {
        this.mContext = mContext;
        this.mDatosUniversidadViewController = mDatosUniversidadViewController;
        this.mAllController = new AllController(this.mContext);
        this.mClientService = new ClientService(mContext);
    }

    public Universidad GetInfoUniversity() {
        Universidad mUniversidad = mAllController.getuniversidadPersona();
        if (mUniversidad != null) {
            Direccion mDireccion = mAllController.getDireccionUniversidad(mUniversidad.id_direccion);
            if (mDireccion != null) mUniversidad.mDireccion = mDireccion;
        }
        return mUniversidad;
    }

    public void RegistrarUniversidad(Universidad mUniversidad) {
        mDatosUniversidadViewController.Onloading(true);
        String json = ConvertModelToStringGson(mUniversidad);
        if (json != null) {
            mClientService
                    .getAPI()
                    .RegistrarUniversidad(json)
                    .enqueue(new Callback<UniversidadDetalleResponse>() {
                        @Override
                        public void onResponse(Call<UniversidadDetalleResponse> call, Response<UniversidadDetalleResponse> response) {
                            mDatosUniversidadViewController.Onloading(false);
                            UniversidadDetalleResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mDatosUniversidadViewController.Onsucces(res.mUniversidad);
                                } else {
                                    mDatosUniversidadViewController.Onfailed(res.mensaje);
                                }
                            } else {
                                mDatosUniversidadViewController.Onfailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<UniversidadDetalleResponse> call, Throwable t) {
                            mDatosUniversidadViewController.Onloading(false);
                            mDatosUniversidadViewController.Onfailed(ERROR_CONECTION);
                        }
                    });
        } else {

            mDatosUniversidadViewController.Onloading(false);
            mDatosUniversidadViewController.Onfailed(ERROR_CONECTION);
        }
    }

    public boolean updateUniversidad(Universidad mUniversidad) {
         return mAllController.updateDatosUniversidad(mUniversidad);
       // mAllController.updateDatosUniversidad(mUniversidad);
       /* if (mUniversidad.mDireccion != null) {
            mAllController.updateDireccion(mUniversidad.mDireccion);
        }*/
    }
}
