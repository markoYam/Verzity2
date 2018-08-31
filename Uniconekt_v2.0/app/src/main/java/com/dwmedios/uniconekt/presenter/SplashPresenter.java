package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.api.apiConfiguraciones;
import com.dwmedios.uniconekt.data.service.response.ConfiguracionesResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadDetalleResponse;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.SplashViewController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dwmedios.uniconekt.data.service.ClientService.URL_BASE_TEMP;
import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class SplashPresenter {

    private Context mContext;
    private SplashViewController mSplashViewController;
    private AllController mAllController;
    private ClientService mClientService;

    public SplashPresenter(Context mContext, SplashViewController mSplashViewController) {
        this.mContext = mContext;
        this.mSplashViewController = mSplashViewController;
        this.mAllController = new AllController(mContext);
        this.mClientService = new ClientService(mContext);
    }

    public void LoadMenu() {
        int type = SharePrefManager.getInstance(mContext).getTypeUser();
        if (type == 1 || type == 2) {
            mSplashViewController.Userloggedin();
        } else {
            SharePrefManager.getInstance(mContext).saveTypeUser(0);
            mSplashViewController.userNotLoggedIn();
        }
       /* if (mAllController.ExisteSesionUniversidad()) {
            SharePrefManager.getInstance(mContext).saveTypeUser(2);
            mSplashViewController.Userloggedin();
        } else if (mAllController.ExisteSesionUniversiTario()) {
            SharePrefManager.getInstance(mContext).saveTypeUser(1);
            mSplashViewController.Userloggedin();
        } else if (SharePrefManager.getInstance(mContext).getTypeUser() == 0) {
            SharePrefManager.getInstance(mContext).saveTypeUser(0);
            mSplashViewController.userNotLoggedIn();
        } else {
            SharePrefManager.getInstance(mContext).saveTypeUser(0);
            mSplashViewController.userNotLoggedIn();
        }*/


    }

    public void getConguraciones() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE_TEMP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiConfiguraciones mApiConfiguraciones = mRetrofit.create(apiConfiguraciones.class);
        mApiConfiguraciones.GetConfiguracionesGenerales().enqueue(new Callback<ConfiguracionesResponse>() {
            @Override
            public void onResponse(Call<ConfiguracionesResponse> call, Response<ConfiguracionesResponse> response) {
                ConfiguracionesResponse res = response.body();
                if (res != null) {
                    if (res.status == 1) {
                        mAllController.saveConfiguraciones(res.mConfiguraciones);
                        LoadMenu();
                    } else {
                        mSplashViewController.tryDownloadConfig();
                    }
                } else {
                    mSplashViewController.tryDownloadConfig();
                }
            }

            @Override
            public void onFailure(Call<ConfiguracionesResponse> call, Throwable t) {
                mSplashViewController.tryDownloadConfig();
            }
        });
    }

    public void verificarUniversidad() {
        if (SharePrefManager.getInstance(mContext).getTypeUser() == 2) {
            Universidad mUniversidad = mAllController.getuniversidadPersona();
            String json = ConvertModelToStringGson(mUniversidad);
            if (json != null && mUniversidad != null) {
                mClientService.getAPI()
                        .VerificarStatusUniversidad(json)
                        .enqueue(new Callback<UniversidadDetalleResponse>() {
                            @Override
                            public void onResponse(Call<UniversidadDetalleResponse> call, Response<UniversidadDetalleResponse> response) {
                                UniversidadDetalleResponse res = response.body();
                                if (res != null) {
                                    if (res.status == 1) {
                                        mSplashViewController.OnsuccesVerifyUniversity(res.mUniversidad);
                                    } else {
                                        mSplashViewController.OnFailedVerifyUniversity(res.mensaje);
                                    }
                                } else {
                                    mSplashViewController.OnFailedVerifyUniversity(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<UniversidadDetalleResponse> call, Throwable t) {
                                mSplashViewController.OnFailedVerifyUniversity(ERROR_CONECTION);
                            }
                        });
            } else {
                mSplashViewController.OnFailedVerifyUniversity(ERROR_CONECTION);
            }
        } else {
            mSplashViewController.OnFailedVerifyUniversity(ERROR_CONECTION);
        }
    }

    public void updatePaquete(VentasPaquetes mVentasPaquetes) {
        if (mAllController.updatePaquete(mVentasPaquetes)) {
            Log.e("Paquetes", "Informacion actualizada");
        } else {
            mAllController.deletePaquetes();
            if (mAllController.addPaquetes(mVentasPaquetes)) {
                Log.e("Paquetes", "Informacion actualizada");
            } else

                Log.e("Paquetes", "Erorr al actualizar la informacion");
        }

    }

    public void updateUniversidad(Universidad mUniversidad) {
        mAllController.updateDatosUniversidad(mUniversidad);
    }

    public void deletePaquetes() {
        mAllController.deletePaquetes();
    }

    public boolean borrarTodo() {
        return mAllController.clearAllTables();
    }
}
