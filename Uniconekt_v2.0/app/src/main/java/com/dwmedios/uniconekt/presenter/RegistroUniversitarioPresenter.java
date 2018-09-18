package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.UsuarioResponse;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.viewmodel.RegistroUniversitarioViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;

public class RegistroUniversitarioPresenter {
    private AllController mAllController;
    private Context mContext;
    private ClientService mClientService;
    private RegistroUniversitarioViewController mRegistroUniversitarioViewController;

    public RegistroUniversitarioPresenter(Context mContext, RegistroUniversitarioViewController mRegistroUniversitarioViewController) {
        this.mContext = mContext;
        this.mRegistroUniversitarioViewController = mRegistroUniversitarioViewController;
        this.mClientService = new ClientService(mContext);
        this.mAllController = new AllController(mContext);
    }

    public void CrearCuentaAcceso(Usuario mUsuario) {
        mRegistroUniversitarioViewController.OnLoadingRegistro(true);
        String json = ConvertModelToStringGson(mUsuario);
        Log.e("registro", json);
        if (json != null) {
            mClientService.getAPI().RegisterUniversitario(json).enqueue(new Callback<UsuarioResponse>() {
                @Override
                public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                    mRegistroUniversitarioViewController.OnLoadingRegistro(false);
                    UsuarioResponse res = response.body();
                    if (res != null) {
                        switch (res.status) {
                            case 1:
                                mRegistroUniversitarioViewController.OnsuccesRegistro(res.mUsuario);
                                break;
                            case 0:
                                mRegistroUniversitarioViewController.OnFailedRegistro(res.mensaje);
                                break;
                            case -1:
                                mRegistroUniversitarioViewController.OnFailedRegistro("Ocurrió un error en el registro.");
                                break;
                        }
                    } else {
                        mRegistroUniversitarioViewController.OnFailedRegistro("Ocurrió un error en el registro.");
                    }
                }

                @Override
                public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                    mRegistroUniversitarioViewController.OnLoadingRegistro(false);
                    mRegistroUniversitarioViewController.OnFailedRegistro("Ocurrió un error en el registro.");
                }
            });
        } else {
            mRegistroUniversitarioViewController.OnLoadingRegistro(false);
            mRegistroUniversitarioViewController.OnFailedRegistro("Ocurrió un error en el registro.");
        }
    }

    public boolean SaveAllInfoUser(Usuario mUsuario) {
        mAllController.clearAllTables();
        mAllController.saveAllInfoUser(mUsuario);
        return true;
    }
}
