package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.FinanciamientoResponse;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.model.Financiamientos;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.FinanciamientoViewController;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;
import static com.dwmedios.uniconekt.view.util.Utils.ParseString;

public class FinanciamientoPresenter {
    private ClientService mClientService;
    private FinanciamientoViewController mFinanciamientoViewController;

    public FinanciamientoPresenter(FinanciamientoViewController mFinanciamientoViewController, Context mContext) {
        this.mFinanciamientoViewController = mFinanciamientoViewController;
        mClientService = new ClientService(mContext);
    }

    public void SearchFinanciamientos(Universidad mUniversidad) {
        mFinanciamientoViewController.Onloading(true);
        String json = ConvertModelToStringGson(mUniversidad);
        if (json != null) {
            mClientService
                    .getAPI()
                    .GetFinanciamientos(json)
                    .enqueue(new Callback<FinanciamientoResponse>() {
                        @Override
                        public void onResponse(Call<FinanciamientoResponse> call, Response<FinanciamientoResponse> response) {
                            mFinanciamientoViewController.Onloading(false);
                            FinanciamientoResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mFinanciamientoViewController.OnSucces(res.mFinanciamientosList,1);
                                } else {
                                    mFinanciamientoViewController.OnFailed(res.mensaje);
                                }
                            } else {
                                mFinanciamientoViewController.OnFailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<FinanciamientoResponse> call, Throwable t) {
                            mFinanciamientoViewController.Onloading(false);
                            mFinanciamientoViewController.OnFailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mFinanciamientoViewController.Onloading(false);
            mFinanciamientoViewController.OnFailed(ERROR_CONECTION);
        }
    }

    public void SearchFinanciamientos2(List<Financiamientos> mFinanciamientosList, String criterio) {
        List<Financiamientos> mFinanciamientosListTemp = new ArrayList<>();
        if (mFinanciamientosList != null && mFinanciamientosList.size() > 0) {
            for (Financiamientos mFinanciamientos : mFinanciamientosList) {
                if (mFinanciamientos.nombre.toLowerCase().contains(criterio.toLowerCase())) {
                    mFinanciamientosListTemp.add(mFinanciamientos);
                }
            }
            if (mFinanciamientosListTemp != null && mFinanciamientosListTemp.size() > 0) {
              //  mFinanciamientoViewController.OnSucces(mFinanciamientosListTemp);
            } else {
                mFinanciamientoViewController.EmptyRecyclerView();
            }
        } else {
            mFinanciamientoViewController.EmptyRecyclerView();
        }


    }
    public static String limpiarAcentos(String cadena) {
        String limpio =null;
        if (cadena !=null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
        }
        return limpio;
    }
    public void SearchFinanciamientos(List<Financiamientos> mFinanciamientosList, String criterio, final boolean isUniversidad) {
        Utils.searchList mSearchList = new Utils.searchList();
        mSearchList.setmHandleSearch(new Utils.handleSearch() {
            @Override
            public boolean hadle(Object object, String criterio) {
                Financiamientos mFinanciamientos = (Financiamientos) object;
                if (!isUniversidad)
                    return mFinanciamientos.nombre != null ? ParseString(mFinanciamientos.nombre.toLowerCase()).contains(ParseString(criterio.toLowerCase())) : false;
                else {

                    if (mFinanciamientos.universidad != null) {
                        return (mFinanciamientos.universidad.nombre != null ? ParseString(mFinanciamientos.universidad.nombre.toLowerCase()).contains(ParseString(criterio.toLowerCase())) : false);
                    } else {
                        return false;
                    }
                }
            }
        });
        mSearchList.setmResultSearch(new Utils.resultSearch() {
            @Override
            public void resultSearch(List<?> mObjects) {
                List<Financiamientos> mFinanciamientos = (List<Financiamientos>) mObjects;
                if (mFinanciamientos != null && mFinanciamientos.size() > 0) {
                    mFinanciamientoViewController.OnSucces(mFinanciamientos, 0);
                } else {
                    mFinanciamientoViewController.OnFailed("");
                }
            }
        });
        mSearchList.setdata(mFinanciamientosList, criterio);
        mSearchList.execute();
    }
}
