package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.controller.DireccionController;
import com.dwmedios.uniconekt.data.controller.DispositivoController;
import com.dwmedios.uniconekt.data.controller.PersonaController;
import com.dwmedios.uniconekt.data.controller.UniversidadController;
import com.dwmedios.uniconekt.data.controller.UsuarioController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.DispositivoResponse;
import com.dwmedios.uniconekt.data.service.response.UsuarioResponse;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.viewmodel.LoginViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

/**
 * Created by mYam on 17/04/2018.
 */

public class LoginPresenter {

    private ClientService mClientService;
    private LoginViewController mLoginViewController;

    private DireccionController mDireccionController;
    private DispositivoController mDispositivoController;
    private PersonaController mPersonaController;
    private UniversidadController mUniversidadController;
    private UsuarioController mUsuarioController;
    private AllController mAllController;

    private Context mContext;

    public LoginPresenter(LoginViewController mLoginViewController, Context mContext) {
        this.mLoginViewController = mLoginViewController;
        this.mContext = mContext;

        if (this.mClientService == null) {
            this.mClientService = new ClientService(mContext);
        }
        if (mDireccionController == null)
            this.mDireccionController = new DireccionController(this.mContext);
        if (mAllController == null)
            this.mAllController = new AllController(this.mContext);
        if (this.mDispositivoController == null)
            this.mDispositivoController = new DispositivoController(this.mContext);
        if (this.mPersonaController == null)
            this.mPersonaController = new PersonaController(this.mContext);
        if (this.mUniversidadController == null)
            this.mUniversidadController = new UniversidadController(this.mContext);
        if (this.mUsuarioController == null)
            this.mUsuarioController = new UsuarioController(this.mContext);
    }

    public void LoginUser(Usuario mUsuario) {
        mLoginViewController.Loading(true);
        String json = ConvertModelToStringGson(mUsuario);
        if (json != null) {
            mClientService
                    .getAPI()
                    .Login(json).enqueue(new Callback<UsuarioResponse>() {
                @Override
                public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                    mLoginViewController.Loading(false);
                    UsuarioResponse res = response.body();
                    if (res != null) {
                        if (res.status == 1) {
                            // mLoginViewController.LoginFailed(res.mensaje);
                            Usuario user = res.mUsuario;
                            mLoginViewController.LoginSucces(user);
                        } else {
                            if (res.status != -1) {
                                mLoginViewController.LoginFailed(res.mensaje);
                            } else {
                                mLoginViewController.LoginFailed(ERROR_CONECTION);
                            }

                        }
                    } else {
                        mLoginViewController.LoginFailed(ERROR_CONECTION);
                    }
                }

                @Override
                public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                    mLoginViewController.Loading(false);
                    mLoginViewController.LoginFailed(ERROR_CONECTION);
                }
            });
        } else {
            mLoginViewController.Loading(false);
            mLoginViewController.LoginFailed(ERROR_CONECTION);
        }

    }

    /*
        public void LoginUniversitario(Dispositivo mDispositivo) {
            mLoginViewController.Loading(true);
            String json = ConvertModelToStringGson(mDispositivo);
            if (json != null) {
                mClientService
                        .getAPI()
                        .LoginUniversitario(json)
                        .enqueue(new Callback<DispositivoResponse>() {
                            @Override
                            public void onResponse(Call<DispositivoResponse> call, Response<DispositivoResponse> response) {
                                mLoginViewController.Loading(false);
                                DispositivoResponse res = response.body();
                                if (res != null) {
                                    if (res.status == 1) {
                                        saveInfoUniversitario(res.mDispositivo);
                                        mLoginViewController.LoginSuccesUniversitario();
                                    } else {
                                        mLoginViewController.LoginFailed(res.mensaje);
                                    }
                                } else {
                                    mLoginViewController.LoginFailed(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<DispositivoResponse> call, Throwable t) {
                                mLoginViewController.Loading(false);
                                mLoginViewController.LoginFailed(ERROR_CONECTION);
                            }
                        });
            } else {
                mLoginViewController.Loading(false);
                mLoginViewController.LoginFailed(ERROR_CONECTION);
            }

        }
    */

    public boolean SaveAllInfoUser(Usuario mUsuario) {
        mAllController.clearAllTables();
        mAllController.saveAllInfoUser(mUsuario);
        return true;
    }

    public boolean saveInfoUniversitario(Dispositivo mDispositivo) {
        mAllController.clearAllTables();
        mAllController.addDispositivo(mDispositivo);
        //falta ponerle la direccion
        mAllController.addPersona(mDispositivo.mPersona);
        if (mDispositivo.mPersona.direccion != null)
            mAllController.addDireccion(mDispositivo.mPersona.direccion);
        return true;
    }
}
