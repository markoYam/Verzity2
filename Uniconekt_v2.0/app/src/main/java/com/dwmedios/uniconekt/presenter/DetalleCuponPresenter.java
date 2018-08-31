package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.CuponCanjeadoResponse;
import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.model.CuponesCanjeados;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.viewmodel.DetalleCuponViewCotroller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class DetalleCuponPresenter {
    private ClientService mClientService;
    private DetalleCuponViewCotroller mDetalleCuponViewCotroller;
    private Context mContext;
    private AllController mAllController;

    public DetalleCuponPresenter(DetalleCuponViewCotroller mDetalleCuponViewCotroller, Context mContext) {
        this.mDetalleCuponViewCotroller = mDetalleCuponViewCotroller;
        this.mClientService = new ClientService(mContext);
        this.mContext = mContext;
        mAllController = new AllController(this.mContext);
    }

    public void CanjearCuppon(Cupones mCupones) {
        mDetalleCuponViewCotroller.Onloading(true);
        Persona mPersona = mAllController.getPersonaUsuario();
        if (mPersona != null) {
            final CuponesCanjeados mCuponesCanjeados = new CuponesCanjeados();
            mCuponesCanjeados.id_cupon = mCupones.id;
            mCuponesCanjeados.id_persona = mPersona.id;
            String json = ConvertModelToStringGson(mCuponesCanjeados);
            if (json != null) {
                mClientService
                        .getAPI()
                        .CanjearCuppon(json)
                        .enqueue(new Callback<CuponCanjeadoResponse>() {
                            @Override
                            public void onResponse(Call<CuponCanjeadoResponse> call, Response<CuponCanjeadoResponse> response) {
                                mDetalleCuponViewCotroller.Onloading(false);
                                CuponCanjeadoResponse res = response.body();
                                if (res != null) {
                                    if (res.status == 1) {
                                        mDetalleCuponViewCotroller.OnSuccess(res.mCuponesCanjeados);
                                    } else {
                                        mDetalleCuponViewCotroller.Onfailed(res.mensaje);
                                    }
                                } else {
                                    mDetalleCuponViewCotroller.Onfailed(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<CuponCanjeadoResponse> call, Throwable t) {
                                mDetalleCuponViewCotroller.Onloading(false);
                                mDetalleCuponViewCotroller.Onfailed(ERROR_CONECTION);
                            }
                        });

            } else {
                mDetalleCuponViewCotroller.Onloading(false);
                mDetalleCuponViewCotroller.Onfailed(ERROR_CONECTION);
            }
        } else {
            mDetalleCuponViewCotroller.Onloading(false);
            mDetalleCuponViewCotroller.Onfailed(ERROR_CONECTION);
        }
    }
}
