package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.api.ClientAPI;
import com.dwmedios.uniconekt.data.service.response.CodigoPostalResponse;
import com.dwmedios.uniconekt.data.service.response.EstadosResponse;
import com.dwmedios.uniconekt.data.service.response.PaisesResponse;
import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Paises;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.viewmodel.GetPaisesViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;

public class GetPaisesPresenter {
    private GetPaisesViewController mGetPaisesViewController;
    private ClientService mClientService;
    private Context mContext;
    private AllController mAllController;

    public GetPaisesPresenter(GetPaisesViewController mGetPaisesViewController, Context mContext) {
        this.mGetPaisesViewController = mGetPaisesViewController;
        this.mContext = mContext;
        this.mAllController= new AllController(mContext);
        mClientService = new ClientService(this.mContext);
    }

    public void getPaises() {
        mGetPaisesViewController.OnLoading(true);
        mClientService.getAPI().GetPaises().enqueue(new Callback<PaisesResponse>() {
            @Override
            public void onResponse(Call<PaisesResponse> call, Response<PaisesResponse> response) {
                mGetPaisesViewController.OnLoading(false);
                PaisesResponse res = response.body();
                if (res != null) {
                    switch (res.status) {
                        case 1:
                            mGetPaisesViewController.onSucces(res.mPaisesList);
                            break;
                        case 0:
                            mGetPaisesViewController.onFailed(res.mensaje);
                            break;
                        case -1:
                            mGetPaisesViewController.onFailed("Ocurrió un error al obtener los países");
                            break;
                    }
                } else {
                    mGetPaisesViewController.onFailed("Ocurrió un error al obtener los países");
                }
            }

            @Override
            public void onFailure(Call<PaisesResponse> call, Throwable t) {
                mGetPaisesViewController.OnLoading(false);
                mGetPaisesViewController.onFailed("Ocurrió un error al obtener los países");
            }
        });
    }

    /**
     * Aqui  ira el metodo para consultar los estdados del pais..!!
     */
    public void SearCodigoPostal(CodigoPostal mCodigoPostal) {
        mGetPaisesViewController.OnLoading(true);
        String json = ConvertModelToStringGson(mCodigoPostal);
        if (json != null) {
            mClientService
                    .getAPI()
                    .SearchCodigoPostal(json)
                    .enqueue(new Callback<CodigoPostalResponse>() {
                        @Override
                        public void onResponse(Call<CodigoPostalResponse> call, Response<CodigoPostalResponse> response) {
                            mGetPaisesViewController.OnLoading(false);
                            CodigoPostalResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mGetPaisesViewController.onSuccesCodigo(res.mCodigoPostalList);
                                } else {
                                    mGetPaisesViewController.OnfailedCodigo(res.mensaje);
                                }
                            } else {
                                mGetPaisesViewController.OnfailedCodigo("No se encontraron coincidencias.");
                            }
                        }

                        @Override
                        public void onFailure(Call<CodigoPostalResponse> call, Throwable t) {
                            mGetPaisesViewController.OnLoading(false);
                            mGetPaisesViewController.OnfailedCodigo("No se encontraron coincidencias.");
                        }
                    });
        } else {
            mGetPaisesViewController.OnLoading(false);
            mGetPaisesViewController.OnfailedCodigo("No se encontraron coincidencias.");
        }
    }

    public void getEstados(Paises mPaises) {
        mGetPaisesViewController.OnLoading(true);
        String json = ConvertModelToStringGson(mPaises);
        if (json != null) {
            mClientService.getAPI().getEstados(json).enqueue(new Callback<EstadosResponse>() {
                @Override
                public void onResponse(Call<EstadosResponse> call, Response<EstadosResponse> response) {
                    mGetPaisesViewController.OnLoading(false);
                    EstadosResponse res = response.body();
                    switch (res.status) {
                        case 1:
                            mGetPaisesViewController.onSuccesEstado(res.mEstadosList);
                            break;
                        case 0:
                            mGetPaisesViewController.onFailedEstado(res.mensaje);
                            break;
                        case -1:
                            mGetPaisesViewController.OnLoading(false);
                            mGetPaisesViewController.onFailedEstado("Ocurrió un error al obtener los estados");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<EstadosResponse> call, Throwable t) {
                    mGetPaisesViewController.OnLoading(false);
                    mGetPaisesViewController.onFailedEstado("Ocurrió un error al obtener los estados");
                }
            });
        } else {
            mGetPaisesViewController.OnLoading(false);
            mGetPaisesViewController.onFailedEstado("Ocurrió un error al obtener los estados");
        }
    }
    public Persona getPersona()
    {
        return mAllController.getDatosPersona();
    }
}
