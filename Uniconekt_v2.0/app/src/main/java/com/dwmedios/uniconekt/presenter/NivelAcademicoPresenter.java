package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.NivelAcademicoResponse;
import com.dwmedios.uniconekt.view.viewmodel.NivelAcademicoViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NivelAcademicoPresenter {
    public ClientService mClientService;
    private Context mContext;
    private NivelAcademicoViewController mNivelAcademicoViewController;

    public NivelAcademicoPresenter(Context mContext, NivelAcademicoViewController mNivelAcademicoViewController) {
        this.mContext = mContext;
        this.mNivelAcademicoViewController = mNivelAcademicoViewController;
        this.mClientService = new ClientService(this.mContext);
    }

    public void getNivelesAcademicos() {
        mClientService.getAPI().getNivelesAcademicos().enqueue(new Callback<NivelAcademicoResponse>() {
            @Override
            public void onResponse(Call<NivelAcademicoResponse> call, Response<NivelAcademicoResponse> response) {
                NivelAcademicoResponse res = response.body();
                switch (res.status) {
                    case 1:
                        mNivelAcademicoViewController.Onsucces(res.mNivelAcademicosList);
                        break;
                    case 0:
                        mNivelAcademicoViewController.Onfailed(res.mensaje);
                        break;
                    case -1:
                        mNivelAcademicoViewController.Onfailed(MES);
                        break;
                }
            }

            @Override
            public void onFailure(Call<NivelAcademicoResponse> call, Throwable t) {
                mNivelAcademicoViewController.Onloading(false);
                mNivelAcademicoViewController.Onfailed(MES);
            }
        });
    }

    public static final String MES = "No se encontraron niveles académicos";
}
