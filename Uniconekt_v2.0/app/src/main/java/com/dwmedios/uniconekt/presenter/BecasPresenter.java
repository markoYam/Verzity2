package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.becasResponse;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.BecasViewController;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;
import static com.dwmedios.uniconekt.view.util.Utils.ParseString;

public class BecasPresenter {
    private ClientService mClientService;
    private BecasViewController mBecasViewController;

    public BecasPresenter(BecasViewController mBecasViewController, Context mContext) {
        this.mBecasViewController = mBecasViewController;
        this.mClientService = new ClientService(mContext);
    }

    public void GetBecasUniversidad(Universidad mUniversidad) {
        mBecasViewController.Onloading(true);
        String json = ConvertModelToStringGson(mUniversidad);
        if (json != null) {
            mClientService
                    .getAPI()
                    .GetBecas(json)
                    .enqueue(new Callback<becasResponse>() {
                        @Override
                        public void onResponse(Call<becasResponse> call, Response<becasResponse> response) {
                            becasResponse res = response.body();
                            Log.e("Becas", ConvertModelToStringGson(res));
                            mBecasViewController.Onloading(false);
                            if (res != null) {
                                if (res.status == 1) {

                                    mBecasViewController.Onsucces(res.mBecasList, 1);
                                } else {
                                    mBecasViewController.OnFailed(res.mensaje);
                                }
                            } else {
                                mBecasViewController.OnFailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<becasResponse> call, Throwable t) {
                            mBecasViewController.Onloading(false);
                            mBecasViewController.OnFailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mBecasViewController.Onloading(false);
            mBecasViewController.OnFailed(ERROR_CONECTION);
        }
    }

    public void SearchBecas2(List<Becas> mBecasList, String criterio) {

        //  mBecasViewController.Onloading(true);
        List<Becas> temp = new ArrayList<>();
        if (mBecasList != null && mBecasList.size() > 0) {

            for (int i = 0; i < mBecasList.size(); i++) {
                if (mBecasList.get(i).nombre.toLowerCase().contains(criterio.toLowerCase())) {
                    boolean existe = false;
                    for (Becas mBecas : temp) {
                        if (mBecas.id == mBecasList.get(i).id) existe = true;
                    }
                    if (!existe) temp.add(mBecasList.get(i));
                }
            }
            //  mBecasViewController.Onloading(false);
            if (temp != null && temp.size() > 0) {

                mBecasViewController.Onsucces(temp, 0);
            } else {
                mBecasViewController.OnFailed("Empty");
            }
        } else {
            mBecasViewController.OnFailed("Empty");
            // mBecasViewController.Onloading(false);
        }
    }

    public void SearchBecas(List<Becas> mBecasList, String criterio, final boolean isSearchUni) {
        Utils.searchList mSearchList = new Utils.searchList();
        mSearchList.setmHandleSearch(new Utils.handleSearch() {
            @Override
            public boolean hadle(Object object, String criterio) {
                Becas mBecas = ((Becas) object);
                if (!isSearchUni)
                    return mBecas.nombre != null ? ParseString(mBecas.nombre.toLowerCase()).contains(ParseString(criterio.toLowerCase())) : false;
                else {

                    if (mBecas.mUniversidad != null) {
                        return (mBecas.mUniversidad.nombre != null ? ParseString(mBecas.mUniversidad.nombre.toLowerCase()).contains(ParseString(criterio.toLowerCase())) : false);
                    } else {
                        return false;
                    }
                }
            }
        });
        mSearchList.setmResultSearch(new Utils.resultSearch() {
            @Override
            public void resultSearch(List<?> mObjects) {
                List<Becas> mBecasList = (List<Becas>) mObjects;
                if (mBecasList != null && mBecasList.size() > 0) {
                    mBecasViewController.Onsucces(mBecasList, 0);
                } else {
                    mBecasViewController.OnFailed("");
                }
            }
        });
        mSearchList.setdata(mBecasList, criterio);
        mSearchList.execute();
    }
}
