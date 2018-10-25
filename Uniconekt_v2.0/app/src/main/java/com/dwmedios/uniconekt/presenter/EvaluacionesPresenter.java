package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.EvaluacionesResponse;
import com.dwmedios.uniconekt.data.service.response.ResultadosResponse;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.cuestionarios.EvaluacionesPersona;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.CustomViewController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ParseString;

public class EvaluacionesPresenter {
    CustomViewController mCustomViewController;
    ClientService mClientService;
    Context mContext;
    AllController mAllController;

    public EvaluacionesPresenter(CustomViewController mCustomViewController, Context mContext) {
        this.mCustomViewController = mCustomViewController;
        this.mContext = mContext;
        this.mClientService = new ClientService(this.mContext);
        this.mAllController = new AllController(this.mContext);
    }


    public void getEvaluaciones() {
        mCustomViewController.OnLoading(true);
        Persona mPersona = mAllController.getDatosPersona();
        if (mPersona != null) {
            String json = Utils.ConvertModelToStringGson(mPersona);
            if (json != null) {
                Log.e("get E", json);
                mClientService.getAPI().getEvaluaciones(json).enqueue(new Callback<EvaluacionesResponse>() {
                    @Override
                    public void onResponse(Call<EvaluacionesResponse> call, Response<EvaluacionesResponse> response) {
                        mCustomViewController.OnLoading(false);
                        EvaluacionesResponse mEvaluacionesResponse = response.body();
                        switch (mEvaluacionesResponse.status) {
                            case 1:
                                List<EvaluacionesPersona> unoList = new ArrayList<>();
                                List<EvaluacionesPersona> ceroList = new ArrayList<>();
                                List<EvaluacionesPersona> ListGeneral = new ArrayList<>();
                                for (int i = 0; i < mEvaluacionesResponse.mEvaluacionesPersonas.size(); i++) {
                                    mEvaluacionesResponse.mEvaluacionesPersonas.get(i).mEvaluaciones.changeNameSection();
                                    if (mEvaluacionesResponse.mEvaluacionesPersonas.get(i).mEvaluaciones.isPaga == 1)
                                        unoList.add(mEvaluacionesResponse.mEvaluacionesPersonas.get(i));
                                    else {
                                        ceroList.add(mEvaluacionesResponse.mEvaluacionesPersonas.get(i));
                                    }
                                }
                                ListGeneral.addAll(ceroList);
                                ListGeneral.addAll(unoList);

                                mCustomViewController.OnSucces(ListGeneral, 0);
                                break;
                            case 0:
                                mCustomViewController.Onfailed(mEvaluacionesResponse.mensaje);
                                break;
                            case -1:
                                mCustomViewController.Onfailed(error);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<EvaluacionesResponse> call, Throwable t) {
                        mCustomViewController.OnLoading(false);
                        mCustomViewController.Onfailed(error);
                    }
                });
            } else {
                mCustomViewController.OnLoading(false);
                mCustomViewController.Onfailed(error);
            }
        } else {
            mCustomViewController.OnLoading(false);
            mCustomViewController.Onfailed(error);
        }
    }

    String error = "Ha ocurrido un error al consultar los cuestionarios.";
    String error2 = "Ha ocurrido un error al consultar los resultados";

    public void getResultado(EvaluacionesPersona mEvaluacionesPersona) {
        mCustomViewController.OnLoading(true);
        String json = Utils.ConvertModelToStringGson(mEvaluacionesPersona);
        if (json != null) {
            Log.e("Ok", json);
            mClientService.getAPI().getResultado(json).enqueue(new Callback<ResultadosResponse>() {
                @Override
                public void onResponse(Call<ResultadosResponse> call, Response<ResultadosResponse> response) {
                    mCustomViewController.OnLoading(false);
                    ResultadosResponse mEvaluacionesResponse = response.body();
                    switch (mEvaluacionesResponse.status) {
                        case 1:
                            mCustomViewController.OnSucces(mEvaluacionesResponse.mResultados);
                            break;
                        case 0:
                            mCustomViewController.Onfailed2(mEvaluacionesResponse.mensaje);
                            break;
                        case -1:
                            mCustomViewController.Onfailed2(error2);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResultadosResponse> call, Throwable t) {
                    mCustomViewController.OnLoading(false);
                    mCustomViewController.Onfailed2(error2);
                }
            });
        } else {
            mCustomViewController.OnLoading(false);
            mCustomViewController.Onfailed2(error2);
        }

    }

    public void SearchEvaluaciones(List<EvaluacionesPersona> mEvaluacionesPersonaList, String criterio) {
        Utils.searchList mSearchList = new Utils.searchList();
        mSearchList.setmHandleSearch(new Utils.handleSearch() {
            @Override
            public boolean hadle(Object object, String criterio) {
                EvaluacionesPersona mEvaluacionesPersona = ((EvaluacionesPersona) object);

                return mEvaluacionesPersona.mEvaluaciones.nombre != null ? ParseString(mEvaluacionesPersona.mEvaluaciones.nombre.toLowerCase()).contains(ParseString(criterio.toLowerCase())) : false;
            }
        });
        mSearchList.setmResultSearch(new Utils.resultSearch() {
            @Override
            public void resultSearch(List<?> mObjects) {
                List<EvaluacionesPersona> mEvaluacionesPersonaList = (List<EvaluacionesPersona>) mObjects;
                if (mEvaluacionesPersonaList != null && mEvaluacionesPersonaList.size() > 0) {
                    mCustomViewController.OnSucces(mEvaluacionesPersonaList, 1);
                } else {
                    mCustomViewController.Onfailed("");
                }
            }
        });
        mSearchList.setdata(mEvaluacionesPersonaList, criterio);
        mSearchList.execute();
    }
}
