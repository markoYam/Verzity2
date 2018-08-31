package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.LicenciaturasResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadResponse;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.view.viewmodel.LicenciaturaViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class LicenciaturasPresenter {
    private ClientService mClientService;
    private LicenciaturaViewController mLicenciaturaViewController;

    public LicenciaturasPresenter(LicenciaturaViewController mLicenciaturaViewController, Context mContext) {
        this.mLicenciaturaViewController = mLicenciaturaViewController;
        mClientService= new ClientService(mContext);
    }

    public void GetLicenciaturas() {
        mLicenciaturaViewController.Onloading(true);
        mClientService
                .getAPI()
                .GetLicenciaturas()
                .enqueue(new Callback<LicenciaturasResponse>() {
                    @Override
                    public void onResponse(Call<LicenciaturasResponse> call, Response<LicenciaturasResponse> response) {
                        LicenciaturasResponse res = response.body();
                        mLicenciaturaViewController.Onloading(false);
                        if (res != null) {

                            if (res.status == 1) {
                                mLicenciaturaViewController.Onsucces(res.mLicenciaturasList);
                            } else {

                                mLicenciaturaViewController.Onfailed(res.mensaje);
                            }
                        } else {

                            mLicenciaturaViewController.Onfailed(ERROR_CONECTION);
                        }
                    }

                    @Override
                    public void onFailure(Call<LicenciaturasResponse> call, Throwable t) {
                        mLicenciaturaViewController.Onloading(false);
                        mLicenciaturaViewController.Onfailed(ERROR_CONECTION);
                    }
                });
    }

}

