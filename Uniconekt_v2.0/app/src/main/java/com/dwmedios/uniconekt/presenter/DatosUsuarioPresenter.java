package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.CodigoPostalResponse;
import com.dwmedios.uniconekt.data.service.response.PaisesResponse;
import com.dwmedios.uniconekt.data.service.response.PersonaResponse;
import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.viewmodel.DatosUsuarioViewController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class DatosUsuarioPresenter {
    private ClientService mClientService;
    private DatosUsuarioViewController mDatosUsuarioViewController;
    private AllController mAllController;

    public DatosUsuarioPresenter(DatosUsuarioViewController mDatosUsuarioViewController, Context mContext) {
        this.mDatosUsuarioViewController = mDatosUsuarioViewController;
        mClientService = new ClientService(mContext);
        mAllController = new AllController(mContext);
    }

    public void Getpaises() {
        mDatosUsuarioViewController.Onloading(true);
        mClientService
                .getAPI()
                .GetPaises()
                .enqueue(new Callback<PaisesResponse>() {
                    @Override
                    public void onResponse(Call<PaisesResponse> call, Response<PaisesResponse> response) {
                        mDatosUsuarioViewController.Onloading(false);
                        PaisesResponse res = response.body();
                        if (res != null) {
                            if (res.status == 1) {
                                mDatosUsuarioViewController.OnsuccesPais(res.mPaisesList);
                            } else {
                                mDatosUsuarioViewController.OnFailed(res.mensaje);
                            }
                        } else {
                            mDatosUsuarioViewController.OnFailed(ERROR_CONECTION);
                        }
                    }

                    @Override
                    public void onFailure(Call<PaisesResponse> call, Throwable t) {
                        mDatosUsuarioViewController.Onloading(false);
                        mDatosUsuarioViewController.OnFailed(ERROR_CONECTION);
                    }
                });
    }

    public void SearCodigoPostal(CodigoPostal mCodigoPostal) {
        mDatosUsuarioViewController.Onloading(true);
        String json = ConvertModelToStringGson(mCodigoPostal);
        if (json != null) {
            mClientService
                    .getAPI()
                    .SearchCodigoPostal(json)
                    .enqueue(new Callback<CodigoPostalResponse>() {
                        @Override
                        public void onResponse(Call<CodigoPostalResponse> call, Response<CodigoPostalResponse> response) {
                            mDatosUsuarioViewController.Onloading(false);
                            CodigoPostalResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {
                                    mDatosUsuarioViewController.OnsuccesCodigo(res.mCodigoPostalList);
                                } else {
                                    mDatosUsuarioViewController.onfailesCodigo();
                                }
                            } else {
                                mDatosUsuarioViewController.onfailesCodigo();
                            }
                        }

                        @Override
                        public void onFailure(Call<CodigoPostalResponse> call, Throwable t) {
                            mDatosUsuarioViewController.Onloading(false);
                            mDatosUsuarioViewController.onfailesCodigo();
                        }
                    });
        } else {
            mDatosUsuarioViewController.Onloading(false);
            mDatosUsuarioViewController.onfailesCodigo();
        }
    }

    public void EditarPerfil(Persona mPersona) {
        mDatosUsuarioViewController.Onloading(true);
        String json = ConvertModelToStringGson(mPersona);
        if (json != null) {
            Log.e("Json editar", json);
            mClientService
                    .getAPI()
                    .EditarPerfil(json)
                    .enqueue(new Callback<PersonaResponse>() {
                        @Override
                        public void onResponse(Call<PersonaResponse> call, Response<PersonaResponse> response) {
                            PersonaResponse res = response.body();
                            mDatosUsuarioViewController.Onloading(false);
                            if (res != null) {
                                if (res.status == 1) {
                                    mDatosUsuarioViewController.OnsuccesEditar(res.mPersona);
                                } else {
                                    mDatosUsuarioViewController.OnFailed(res.mensaje);
                                }
                                if (res.status == -1) {
                                    Log.e("Error EditarPerfil", res.mensaje);
                                }
                            } else {

                                mDatosUsuarioViewController.OnFailed(ERROR_CONECTION);

                            }
                        }

                        @Override
                        public void onFailure(Call<PersonaResponse> call, Throwable t) {
                            mDatosUsuarioViewController.Onloading(false);
                            mDatosUsuarioViewController.OnFailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mDatosUsuarioViewController.Onloading(false);
            mDatosUsuarioViewController.OnFailed(ERROR_CONECTION);
        }
    }

    public void verificarPerfil(Persona mPersona) {
        // mDatosUsuarioViewController.Onloading(true);
        String json = ConvertModelToStringGson(mPersona);
        if (json != null) {
            mClientService
                    .getAPI()
                    .verficarCuenta(json)
                    .enqueue(new Callback<PersonaResponse>() {
                        @Override
                        public void onResponse(Call<PersonaResponse> call, Response<PersonaResponse> response) {
                            PersonaResponse res = response.body();
                            //mDatosUsuarioViewController.Onloading(false);
                            if (res != null) {
                                if (res.status == 1) {
                                    mDatosUsuarioViewController.OnsuccesVerificar(res.mPersona);
                                } else {
                                    mDatosUsuarioViewController.onfailedVerficar(res.mensaje);
                                }
                                if (res.status == -1) {
                                    Log.e("Error verificar...", res.mensaje);
                                }
                            } else {

                                mDatosUsuarioViewController.onfailedVerficar(ERROR_CONECTION);

                            }
                        }

                        @Override
                        public void onFailure(Call<PersonaResponse> call, Throwable t) {
                            // mDatosUsuarioViewController.Onloading(false);
                            mDatosUsuarioViewController.onfailedVerficar(ERROR_CONECTION);
                        }
                    });
        } else {
            //  mDatosUsuarioViewController.Onloading(false);
            mDatosUsuarioViewController.onfailedVerficar(ERROR_CONECTION);
        }
    }

    public void ActualizarPerfil(Persona mPersona) {
        mDatosUsuarioViewController.Onloading(true);
        String json = ConvertModelToStringGson(mPersona);
        if (json != null) {
            mClientService
                    .getAPI()
                    .actualizarCuenta(json)
                    .enqueue(new Callback<PersonaResponse>() {
                        @Override
                        public void onResponse(Call<PersonaResponse> call, Response<PersonaResponse> response) {
                            PersonaResponse res = response.body();
                            mDatosUsuarioViewController.Onloading(false);
                            if (res != null) {
                                if (res.status == 1) {
                                    mDatosUsuarioViewController.OnsuccesActualizar(res.mPersona);
                                } else {
                                    mDatosUsuarioViewController.onfailedActualizar(res.mensaje);
                                }
                                if (res.status == -1) {
                                    Log.e("Error verificar...", res.mensaje);
                                }
                            } else {
                                mDatosUsuarioViewController.onfailedActualizar(ERROR_CONECTION);

                            }
                        }

                        @Override
                        public void onFailure(Call<PersonaResponse> call, Throwable t) {
                            mDatosUsuarioViewController.Onloading(false);
                            mDatosUsuarioViewController.onfailedActualizar(ERROR_CONECTION);
                        }
                    });
        } else {
            mDatosUsuarioViewController.Onloading(false);
            mDatosUsuarioViewController.onfailedActualizar(ERROR_CONECTION);
        }
    }

    //llamar desde el validator
    public void ReturnInfo() {
        List<Persona> mPersonaList = mAllController.getPersona();
        mDatosUsuarioViewController.returnData(mPersonaList.get(0));
    }

    public Persona getInfo() {

        return mAllController.getDatosPersona();
    }

    public Usuario getUsuario() {
        return mAllController.getusuarioPersona();
    }

    public void updateInfouser(Persona mPersona) {
        mAllController.updateDatosPersona(mPersona);
    }
}
