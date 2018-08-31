package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.UsuarioResponse;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.viewmodel.ResetPasswordViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class ResetPasswordPresenter {
    private ClientService mClientService;
    private ResetPasswordViewController mResetPasswordViewController;
    private Context mContext;

    public ResetPasswordPresenter(Context mContext, ResetPasswordViewController mResetPasswordViewController) {
        this.mContext = mContext;
        this.mResetPasswordViewController = mResetPasswordViewController;
        if (this.mClientService == null) this.mClientService = new ClientService(mContext);
    }

    public void RessetPassword(Usuario mUsuario) {
        mResetPasswordViewController.Loading(true);
        String json = ConvertModelToStringGson(mUsuario);
        if (json != null) {
            mClientService
                    .getAPI()
                    .ResetPassword(json)
                    .enqueue(new Callback<UsuarioResponse>() {
                        @Override
                        public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                            mResetPasswordViewController.Loading(false);
                            UsuarioResponse res = response.body();
                            if (res.status == 1) {
                                mResetPasswordViewController.Failed(res.mensaje);
                                mResetPasswordViewController.Succes(res.mUsuario);
                            } else {
                                mResetPasswordViewController.Failed(res.mensaje);
                            }
                        }

                        @Override
                        public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                            mResetPasswordViewController.Loading(false);
                            mResetPasswordViewController.Failed(ERROR_CONECTION);
                        }
                    });
        } else {
            mResetPasswordViewController.Loading(false);
            mResetPasswordViewController.Failed(ERROR_CONECTION);
        }

    }
}
