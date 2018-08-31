package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.PostuladoFinanciamientoResponse;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosFinanciamientos;
import com.dwmedios.uniconekt.view.viewmodel.FinaciamientoDetalleView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class FinanciamientoDetallePresenter {
    private ClientService mClientService;
    private FinaciamientoDetalleView mFinaciamientoDetalleView;
    private AllController mAllController;

    public FinanciamientoDetallePresenter(FinaciamientoDetalleView mFinaciamientoDetalleView, Context mContext) {
        this.mFinaciamientoDetalleView = mFinaciamientoDetalleView;
        mClientService = new ClientService(mContext);
        mAllController = new AllController(mContext);
    }

    public void LoadInfo() {
        mFinaciamientoDetalleView.ConfigureLoad();
    }

    public void updatePersona(Persona mPersona) {
        mAllController.updatePersona(mPersona);
        if (mPersona.direccion != null) {
            try {
                List<Direccion> temp = mAllController.getDireccion();
                if (temp != null && temp.size() > 0) {
                    mAllController.updateDireccion(mPersona.direccion);
                } else {
                    mAllController.addDireccion(mPersona.direccion);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void validateUser() {
        if (!mAllController.validateInfoPersona()) {
            mFinaciamientoDetalleView.Postular();
        } else {
            Persona mPersona = mAllController.getDatosPersona();
            mFinaciamientoDetalleView.Postular(mPersona);
        }
    }

    public void solicitarFinanciamiento(PostuladosFinanciamientos mPostuladosFinanciamientos) {
        mFinaciamientoDetalleView.Onloading(true);
        String json = ConvertModelToStringGson(mPostuladosFinanciamientos);
        if (json != null) {
            mClientService
                    .getAPI()
                    .SolicitarFinanciamiento(json)
                    .enqueue(new Callback<PostuladoFinanciamientoResponse>() {
                        @Override
                        public void onResponse(Call<PostuladoFinanciamientoResponse> call, Response<PostuladoFinanciamientoResponse> response) {
                            mFinaciamientoDetalleView.Onloading(false);
                            PostuladoFinanciamientoResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mFinaciamientoDetalleView.Onfailded(res.mensaje);
                                    mFinaciamientoDetalleView.OnSuccesPostular(res.mPostuladosFinanciamientos);
                                } else {
                                    mFinaciamientoDetalleView.Onfailded(res.mensaje);
                                }
                            } else {
                                mFinaciamientoDetalleView.Onfailded(ERROR_CONECTION);
                            }

                        }

                        @Override
                        public void onFailure(Call<PostuladoFinanciamientoResponse> call, Throwable t) {
                            mFinaciamientoDetalleView.Onfailded(ERROR_CONECTION);
                            mFinaciamientoDetalleView.Onloading(false);
                        }
                    });
        } else {
            mFinaciamientoDetalleView.Onfailded(ERROR_CONECTION);
            mFinaciamientoDetalleView.Onloading(false);
        }
    }
}


