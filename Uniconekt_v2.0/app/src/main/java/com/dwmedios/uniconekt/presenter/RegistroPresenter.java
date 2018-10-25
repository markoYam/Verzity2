package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.controller.RegistroController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.UsuarioResponse;
import com.dwmedios.uniconekt.model.Configuraciones;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.viewmodel.RegistroViewController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.facebook.internal.Utility.isNullOrEmpty;

/**
 * Created by mYam on 16/04/2018.
 */

public class RegistroPresenter {
    private ClientService mClientService;
    private RegistroViewController mRegistroViewController;
    private RegistroController mRegistroController;
    private AllController mAllController;

    public RegistroPresenter(RegistroViewController mRegistroViewController, Context mContext) {
        this.mRegistroViewController = mRegistroViewController;

        if (this.mClientService == null) {
            this.mClientService = new ClientService(mContext);
        }

        if (this.mRegistroController == null) {
            this.mRegistroController = new RegistroController(mContext);
        }
        mAllController = new AllController(mContext);
    }
    public void getTerminos() {
        try {
            Configuraciones mConfiguraciones = mAllController.getConfiguraciones();
            if (mConfiguraciones != null) {
                if (!isNullOrEmpty(mConfiguraciones.terminos)) {
                    mRegistroViewController.setTerminos(mConfiguraciones.terminos);
                }
            }
        } catch (Exception ex) {

        }
    }
    // TODO: 16/04/2018 va recibir la informacion de el activity
    public void registerUser(Usuario mUsuario) {
        mRegistroViewController.showProgress(true);
        String json = ConvertModelToStringGson(mUsuario);
        if (json != null) {
            mClientService
                    .getAPI()
                    .RegisterUser(json).enqueue(new Callback<UsuarioResponse>() {
                @Override
                public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                    UsuarioResponse res = response.body();
                    if (res != null) {
                        if (res.status == 1) {
                            Usuario mUsuario = res.mUsuario;
                            mRegistroViewController.successRegister(mUsuario);
                        } else {
                            if (res.status != -1)
                                mRegistroViewController.failureRegister(res.mensaje);
                            else
                                mRegistroViewController.failureRegister("Ocurrio un error en la conexión");
                        }
                    } else {
                        mRegistroViewController.failureRegister("Ocurrio un error en la conexión");
                    }
                }

                @Override
                public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                    mRegistroViewController.failureRegister("Ocurrio un error en la conexión" + t.getMessage());
                }
            });
        } else {
            mRegistroViewController.failureRegister("Ocurrio un error en la conexión");
        }
        mRegistroViewController.showProgress(false);
    }

    public boolean addUser(Usuario mItem) {
        return mRegistroController.addUsuario(mItem);
    }

    //en este metodo se regresa la informacion del usuario gurdado en la base de datos........

    public void GetUser() {
        List<Usuario> listUser = mRegistroController.getUser();

    }

    public boolean SaveAllInfoUser(Usuario mUsuario) {
        mAllController.clearAllTables();
        mAllController.saveAllInfoUser(mUsuario);
        return true;
    }
}
