package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.CuponResponse;
import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.view.viewmodel.CuponesViewController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;
import static com.dwmedios.uniconekt.view.util.Utils.ParseString;

public class CuponesPresenter {
    private ClientService mClientService;
    private CuponesViewController mCuponesViewController;

    public CuponesPresenter(CuponesViewController mCuponesViewController, Context mContext) {
        this.mCuponesViewController = mCuponesViewController;
        this.mClientService = new ClientService(mContext);
    }

    public void GetCuponesVigentes() {
        mCuponesViewController.Onloading(true);
        mClientService
                .getAPI()
                .GetCuponesVigentes()
                .enqueue(new Callback<CuponResponse>() {
                    @Override
                    public void onResponse(Call<CuponResponse> call, Response<CuponResponse> response) {
                        mCuponesViewController.Onloading(false);
                        CuponResponse res = response.body();
                        if (res != null) {
                            if (res.status == 1) {
                                mCuponesViewController.OnSucces(res.mCuponesList,1);
                            } else {
                                mCuponesViewController.OnFailed(res.mensaje);
                            }
                        } else {
                            mCuponesViewController.OnFailed(ERROR_CONECTION);
                        }
                    }

                    @Override
                    public void onFailure(Call<CuponResponse> call, Throwable t) {
                        mCuponesViewController.Onloading(false);
                        mCuponesViewController.OnFailed(ERROR_CONECTION);
                    }
                });
    }

    public void searchCupones(List<Cupones> mCuponesList, String criterio) {
        if (criterio.isEmpty()) {
            this.GetCuponesVigentes();
        } else {
            List<Cupones> mCuponesListTemp = new ArrayList<>();
            if (mCuponesList != null && mCuponesList.size() > 0) {
                for (Cupones mCupones : mCuponesList) {
                    if (ParseString(mCupones.nombre.toLowerCase()).contains(ParseString(criterio.toLowerCase()))) {
                        mCuponesListTemp.add(mCupones);
                    }
                }
                if (mCuponesListTemp != null && mCuponesListTemp.size() > 0) {
                    mCuponesViewController.OnSucces(mCuponesListTemp,0);
                } else {
                    mCuponesViewController.EmptyRecyclerView();
                }
            } else {
                mCuponesViewController.EmptyRecyclerView();
            }
        }

    }
}
