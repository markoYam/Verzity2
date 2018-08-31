package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.PostularseBecaResponse;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosBecas;
import com.dwmedios.uniconekt.view.viewmodel.BecasDetalleViewController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class BecasDetallePresenter {
    private BecasDetalleViewController mBecasDetalleViewController;
    private AllController mAllController;
    private ClientService mClientService;

    public BecasDetallePresenter(BecasDetalleViewController mBecasDetalleViewController, Context mContext) {
        this.mBecasDetalleViewController = mBecasDetalleViewController;
        mAllController = new AllController(mContext);
        mClientService = new ClientService(mContext);
    }

    public void loadInfo() {
        mBecasDetalleViewController.configureLoad();
    }

    public void validateUser() {
        if (!mAllController.validateInfoPersona()) {
            mBecasDetalleViewController.PostularDetalle();
        } else {
            Persona mPersona = mAllController.getDatosPersona();
            mBecasDetalleViewController.postular(mPersona);
        }
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

    public void postularBeca(PostuladosBecas mPostuladosBecas) {
        mBecasDetalleViewController.Onloading(true);
        String json = ConvertModelToStringGson(mPostuladosBecas);
        if (json != null) {
            mClientService
                    .getAPI()
                    .PostularseBeca(json)
                    .enqueue(new Callback<PostularseBecaResponse>() {
                        @Override
                        public void onResponse(Call<PostularseBecaResponse> call, Response<PostularseBecaResponse> response) {
                            mBecasDetalleViewController.Onloading(false);
                            PostularseBecaResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mBecasDetalleViewController.OnFailed(res.mensaje);
                                    mBecasDetalleViewController.OnsuccesPostular(res.mPostuladosBecas);
                                } else {
                                    if (res.status == 0)
                                        mBecasDetalleViewController.OnFailed(res.mensaje);
                                    else {
                                        Log.e("Beca postular", res.mensaje);
                                        mBecasDetalleViewController.OnFailed(ERROR_CONECTION);
                                    }
                                }
                            } else {
                                mBecasDetalleViewController.OnFailed(ERROR_CONECTION);
                                Log.e("Key beca", "Objeto nulo");
                            }
                        }

                        @Override
                        public void onFailure(Call<PostularseBecaResponse> call, Throwable t) {
                            mBecasDetalleViewController.Onloading(false);
                            mBecasDetalleViewController.OnFailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mBecasDetalleViewController.Onloading(false);
        }
    }
}
