package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.BannersResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadDetalleResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadResponse;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadViewController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;
import static com.dwmedios.uniconekt.view.util.Utils.ParseString;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class UniversidadPresenter {
    private UniversidadViewController mUniversidadViewController;
    private ClientService mClientService;
    private Context mContext;
    private AllController mAllController;

    public UniversidadPresenter(UniversidadViewController mUniversidadViewController, Context mContext) {
        this.mUniversidadViewController = mUniversidadViewController;
        this.mClientService = new ClientService(mContext);
        this.mContext = mContext;
        mAllController = new AllController(this.mContext);
    }

    public void Search(SearchUniversidades mSearchUniversidades, int tipo) {
        if (tipo != 3)
            mSearchUniversidades.extranjero = SharePrefManager.getInstance(mContext).isSeachExtranjero();
        Persona mPersona = mAllController.getDatosPersona();
        if (isNullOrEmpty(mSearchUniversidades.pais))
            if (mPersona != null && mPersona.direccion != null) {
                mSearchUniversidades.pais = mPersona.direccion.pais;
            }
        mUniversidadViewController.Onloading(true);
        String json = ConvertModelToStringGson(mSearchUniversidades);
        Log.e("Buscar uni", json);
        if (json != null) {
            mClientService
                    .getAPI()
                    .SearchUniversity(json)
                    .enqueue(new Callback<UniversidadResponse>() {
                        @Override
                        public void onResponse(Call<UniversidadResponse> call, Response<UniversidadResponse> response) {
                            UniversidadResponse res = response.body();
                            mUniversidadViewController.Onloading(false);
                            if (res != null) {

                                if (res.status == 1) {
                                    mUniversidadViewController.OnSuccessSeach(res.mUniversidadList, 1);
                                } else {

                                    mUniversidadViewController.Onfailed(res.mensaje);
                                }
                            } else {

                                mUniversidadViewController.Onfailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<UniversidadResponse> call, Throwable t) {
                            mUniversidadViewController.Onloading(false);
                            mUniversidadViewController.Onfailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mUniversidadViewController.Onloading(true);
            mUniversidadViewController.Onfailed(ERROR_CONECTION);
        }

    }

    public Persona getPersona() {
       return mAllController.getDatosPersona();

    }

    public Persona getDatosPersona() {
        return mAllController.getDatosPersona();
    }
    public Usuario getUsuario()
    {
        return mAllController.getusuarioPersona();
    }

    public void getFavoritos() {
        mUniversidadViewController.Onloading(true);
        String json = ConvertModelToStringGson(getDatosPersona());
        if (json != null) {
            mClientService
                    .getAPI()
                    .getFavoritos(json, SharePrefManager.getInstance(mContext).isSeachExtranjero())
                    .enqueue(new Callback<UniversidadResponse>() {
                        @Override
                        public void onResponse(Call<UniversidadResponse> call, Response<UniversidadResponse> response) {
                            mUniversidadViewController.Onloading(false);
                            UniversidadResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mUniversidadViewController.OnSuccessSeach(res.mUniversidadList, 1);
                                } else {
                                    mUniversidadViewController.Onfailed(res.mensaje);
                                }
                            } else {
                                mUniversidadViewController.Onfailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<UniversidadResponse> call, Throwable t) {
                            mUniversidadViewController.Onloading(true);
                            mUniversidadViewController.Onfailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mUniversidadViewController.Onloading(true);
            mUniversidadViewController.Onfailed(ERROR_CONECTION);
        }

    }

    public void searchUniversidades(List<Universidad> mUniversidads, String criterio) {
        Utils.searchList mSearchList = new Utils.searchList();
        mSearchList.setmHandleSearch(new Utils.handleSearch() {
            @Override
            public boolean hadle(Object object, String criterio) {
                Universidad mUniversidad = (Universidad) object;

                return (mUniversidad.nombre != null && ParseString(mUniversidad.nombre.toLowerCase()).contains(ParseString(criterio.toLowerCase())));
            }
        });
        mSearchList.setmResultSearch(new Utils.resultSearch() {
            @Override
            public void resultSearch(List<?> mObjects) {
                List<Universidad> mUniversidads = (List<Universidad>) mObjects;
                if (mUniversidads != null && mUniversidads.size() > 0) {
                    mUniversidadViewController.OnSuccessSeach(mUniversidads, 0);
                } else {
                    mUniversidadViewController.Onfailed("");
                }
            }
        });
        mSearchList.setdata(mUniversidads, criterio);
        mSearchList.execute();
    }
}
