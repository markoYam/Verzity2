package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.FavoritosResponse;
import com.dwmedios.uniconekt.data.service.response.PostuladosUniversidadesResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadDetalleResponse;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Favoritos;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadDetalleViewController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class UniversidadDetallePresenter {
    private ClientService mClientService;
    private AllController mAllController;
    private Context mContext;
    private UniversidadDetalleViewController mUniversidadDetalleController;

    public UniversidadDetallePresenter(UniversidadDetalleViewController mUniversidadDetalleController, Context mContext) {
        this.mUniversidadDetalleController = mUniversidadDetalleController;
        this.mClientService = new ClientService(mContext);
        this.mContext = mContext;
        this.mAllController = new AllController(this.mContext);
    }

    public void GetDetail(Universidad mUniversidad) {
        mUniversidadDetalleController.Onloading(true);
        String json = ConvertModelToStringGson(mUniversidad);
        if (json != null) {
            mClientService
                    .getAPI()
                    .GetDetailsUniversity(json)
                    .enqueue(new Callback<UniversidadDetalleResponse>() {
                        @Override
                        public void onResponse(Call<UniversidadDetalleResponse> call, Response<UniversidadDetalleResponse> response) {
                            UniversidadDetalleResponse res = response.body();
                            mUniversidadDetalleController.Onloading(false);
                            if (res != null) {

                                if (res.status == 1) {
                                    mUniversidadDetalleController.OnSuccess(res.mUniversidad);
                                } else {
                                    mUniversidadDetalleController.Onfailed(res.mensaje);
                                }
                            } else {
                                mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<UniversidadDetalleResponse> call, Throwable t) {
                            mUniversidadDetalleController.Onloading(false);
                            mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mUniversidadDetalleController.Onloading(false);
            mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
        }

    }

    public void RegistrarFavorito(Universidad mUniversidad) {
        mUniversidadDetalleController.Onloading(true);
        Persona tempPersona = mAllController.getPersonaUsuario();
        if (tempPersona != null) {
            Favoritos mFavoritos = new Favoritos();
            mFavoritos.id_persona = tempPersona.id;
            mFavoritos.id_universidad = mUniversidad.id;
            String json = ConvertModelToStringGson(mFavoritos);
            if (json != null) {
                mClientService
                        .getAPI()
                        .SetFavorito(json)
                        .enqueue(new Callback<FavoritosResponse>() {
                            @Override
                            public void onResponse(Call<FavoritosResponse> call, Response<FavoritosResponse> response) {
                                FavoritosResponse res = response.body();
                                mUniversidadDetalleController.Onloading(false);
                                if (res != null) {
                                    if (res.status == 1) {
                                        mUniversidadDetalleController.OnsuccesFavorito(res.mFavoritos);
                                    } else {
                                        mUniversidadDetalleController.OnfailedFavorito(res.mensaje);
                                    }
                                } else {
                                    mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<FavoritosResponse> call, Throwable t) {
                                mUniversidadDetalleController.Onloading(false);
                                mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                            }
                        });
            } else {
                mUniversidadDetalleController.Onloading(false);
                mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
            }
        } else {
            mUniversidadDetalleController.Onloading(false);
            mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
        }
    }


    public void VerificarFavorito(Universidad mUniversidad) {
        //   mUniversidadDetalleController.Onloading(true);
        Persona tempPersona = mAllController.getPersonaUsuario();
        if (tempPersona != null) {
            Favoritos mFavoritos = new Favoritos();
            mFavoritos.id_persona = tempPersona.id;
            mFavoritos.id_universidad = mUniversidad.id;
            String json = ConvertModelToStringGson(mFavoritos);
            if (json != null) {
                mClientService
                        .getAPI()
                        .VerificarFavorito(json)
                        .enqueue(new Callback<FavoritosResponse>() {
                            @Override
                            public void onResponse(Call<FavoritosResponse> call, Response<FavoritosResponse> response) {
                                FavoritosResponse res = response.body();
                                //  mUniversidadDetalleController.Onloading(false);
                                if (res != null) {
                                    if (res.status == 1) {
                                        mUniversidadDetalleController.OnsuccesCheck(res.mFavoritos);
                                    } else {
                                        mUniversidadDetalleController.OnfailedFavorito(res.mensaje);
                                    }
                                } else {
                                    mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<FavoritosResponse> call, Throwable t) {
                                //    mUniversidadDetalleController.Onloading(false);
                                mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                            }
                        });
            } else {
                //  mUniversidadDetalleController.Onloading(false);
                mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
            }
        } else {
            //  mUniversidadDetalleController.Onloading(false);
            mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
        }
    }

    public void PostularseUniversidad(PostuladosUniversidades mPostuladosUniversidades) {
        mUniversidadDetalleController.Onloading(true);
        String json = ConvertModelToStringGson(mPostuladosUniversidades);
        if (json != null) {
            mClientService
                    .getAPI().PostularUniversidad(json)
                    .enqueue(new Callback<PostuladosUniversidadesResponse>() {
                        @Override
                        public void onResponse(Call<PostuladosUniversidadesResponse> call, Response<PostuladosUniversidadesResponse> response) {
                            mUniversidadDetalleController.Onloading(false);
                            PostuladosUniversidadesResponse res = response.body();
                            if (res != null) {
                                if (res.status == 1) {

                                    mUniversidadDetalleController.OnsuccesPostular(res.mPostuladosUniversidades);
                                } else {
                                    mUniversidadDetalleController.Onfailed(res.mensaje);
                                }
                                if (res.status == -1) {
                                    Log.e("Error postularseUni", res.mensaje);
                                }
                            } else {
                                mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostuladosUniversidadesResponse> call, Throwable t) {
                            mUniversidadDetalleController.Onloading(false);
                            mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
                        }
                    });
        } else {
            mUniversidadDetalleController.Onloading(false);
            mUniversidadDetalleController.Onfailed(ERROR_CONECTION);
        }
    }

    public void updatePersona(Persona mPersona) {
        mAllController.updateDatosPersona(mPersona);
    }

    public void validateUser() {
        if (!mAllController.validateInfoPersona()) {
             mUniversidadDetalleController.postular();
           // mUniversidadDetalleController.postularDetalle2();
        } else {
            Persona mPersona = mAllController.getDatosPersona();
            mUniversidadDetalleController.postular2(mPersona);
        }
    }

}
