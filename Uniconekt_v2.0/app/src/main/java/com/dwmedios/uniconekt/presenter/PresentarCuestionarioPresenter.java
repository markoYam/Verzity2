package com.dwmedios.uniconekt.presenter;

import android.content.Context;
import android.util.Log;

import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.DetalleEvaluacionResponse;
import com.dwmedios.uniconekt.data.service.response.RespuestaPersonaResponse;
import com.dwmedios.uniconekt.data.service.response.ResultadosResponse;
import com.dwmedios.uniconekt.model.cuestionarios.EvaluacionesPersona;
import com.dwmedios.uniconekt.model.cuestionarios.RespuestasPersona;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.CustomViewController;
import com.dwmedios.uniconekt.view.viewmodel.PresentarCuestionarioViewController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PresentarCuestionarioPresenter {
    private PresentarCuestionarioViewController mPresentarCuestionarioViewController;
    private Context mContext;
    private ClientService mClientService;
    private CustomViewController mCustomViewController;

    public PresentarCuestionarioPresenter(PresentarCuestionarioViewController mPresentarCuestionarioViewController, Context mContext, CustomViewController mCustomViewController) {
        this.mPresentarCuestionarioViewController = mPresentarCuestionarioViewController;
        this.mContext = mContext;
        this.mClientService = new ClientService(mContext);
        this.mCustomViewController = mCustomViewController;
    }

    public void getCuestionario(EvaluacionesPersona mEvaluacionesPersona) {
        mPresentarCuestionarioViewController.OnLoading(true);
        String json = Utils.ConvertModelToStringGson(mEvaluacionesPersona);
        if (json != null) {
            Log.e("cuestionario", json);
            mClientService.getAPI().getDetalleEvaluacion(json).enqueue(new Callback<DetalleEvaluacionResponse>() {
                @Override
                public void onResponse(Call<DetalleEvaluacionResponse> call, Response<DetalleEvaluacionResponse> response) {
                    mPresentarCuestionarioViewController.OnLoading(false);
                    DetalleEvaluacionResponse res = response.body();
                    switch (res.status) {
                        case 1:
                            mPresentarCuestionarioViewController.OnsuccesGetCuestionario(res.detalleEvaluacionViewModel);
                            break;
                        case 0:
                            mPresentarCuestionarioViewController.OnfailedRespuesta(res.mensaje);
                            break;
                        case -1:
                            mPresentarCuestionarioViewController.OnfailedRespuesta(error);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<DetalleEvaluacionResponse> call, Throwable t) {
                    mPresentarCuestionarioViewController.OnLoading(false);
                    mPresentarCuestionarioViewController.OnfailedRespuesta(error);
                }
            });
        } else {
            mPresentarCuestionarioViewController.OnLoading(false);
            mPresentarCuestionarioViewController.OnfailedRespuesta(error);
        }
    }

    private String error = "Ha ocurrido un error al consultar los cuestionarios.";
    private String error2 = "Ha ocurrido un error al guardar la respuesta.";

    public void GuardarRespuesta(RespuestasPersona mGuardarRespuesta) {
        mPresentarCuestionarioViewController.OnLoading(true);
        String json = Utils.ConvertModelToStringGson(mGuardarRespuesta);
        if (json != null) {
            Log.e("guardar R", json);
            mClientService.getAPI().guardarRespuesta(json).enqueue(new Callback<RespuestaPersonaResponse>() {
                @Override
                public void onResponse(Call<RespuestaPersonaResponse> call, Response<RespuestaPersonaResponse> response) {
                    mPresentarCuestionarioViewController.OnLoading(false);
                    RespuestaPersonaResponse res = response.body();
                    switch (res.status) {
                        case 1:
                            mPresentarCuestionarioViewController.OnsuccesGuardarRespuesta(res.mRespuestasPersona);
                            break;
                        case 0:
                            mPresentarCuestionarioViewController.OnfailedRespuesta(res.mensaje);
                            break;
                        case -1:
                            mPresentarCuestionarioViewController.closeCuestionario(error2);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<RespuestaPersonaResponse> call, Throwable t) {
                    mPresentarCuestionarioViewController.OnLoading(false);
                    mPresentarCuestionarioViewController.closeCuestionario(error2);
                }
            });
        } else {
            mPresentarCuestionarioViewController.OnLoading(false);
            mPresentarCuestionarioViewController.closeCuestionario(error2);
        }

     /*     if (json != null) {
            mPresentarCuestionarioViewController.OnsuccesGuardarRespuesta(mCuestionario);
        } else {
            mPresentarCuestionarioViewController.OnLoading(false);
            mPresentarCuestionarioViewController.Onfailed("Ocurrio un error");
        }
*/
    }


    String error3 = "Ha ocurrido un error al consultar los resultados";

    public void getResultado(EvaluacionesPersona mEvaluacionesPersona) {
        mCustomViewController.OnLoading(true);
        String json = Utils.ConvertModelToStringGson(mEvaluacionesPersona);
        if (json != null) {
            Log.e("Ok", json);
            mClientService.getAPI().getResultado(json).enqueue(new Callback<ResultadosResponse>() {
                @Override
                public void onResponse(Call<ResultadosResponse> call, Response<ResultadosResponse> response) {
                    mCustomViewController.OnLoading(false);
                    ResultadosResponse mEvaluacionesResponse = response.body();
                    switch (mEvaluacionesResponse.status) {
                        case 1:
                            mCustomViewController.OnSucces(mEvaluacionesResponse.mResultados);
                            break;
                        case 0:
                            mCustomViewController.Onfailed2(mEvaluacionesResponse.mensaje);
                            break;
                        case -1:
                            mCustomViewController.Onfailed2(error3);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResultadosResponse> call, Throwable t) {
                    mCustomViewController.OnLoading(false);
                    mCustomViewController.Onfailed2(error3);
                }
            });
        } else {
            mCustomViewController.OnLoading(false);
            mCustomViewController.Onfailed2(error3);
        }

    }

}
