package com.dwmedios.uniconekt.presenter;

import android.os.Handler;

import com.dwmedios.uniconekt.model.GuardarRespuesta;
import com.dwmedios.uniconekt.model.cuestionarios.Cuestionario;
import com.dwmedios.uniconekt.model.cuestionarios.Respuestas;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.PresentarCuestionarioActivity;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.PresentarCuestionarioViewController;
import com.google.gson.Gson;


public class PresentarCuestionarioPresenter {
    private PresentarCuestionarioViewController mPresentarCuestionarioViewController;

    public PresentarCuestionarioPresenter(PresentarCuestionarioViewController mPresentarCuestionarioViewController) {
        this.mPresentarCuestionarioViewController = mPresentarCuestionarioViewController;
    }

    public void getCuestionario(Cuestionario mCuestionario) {
        mPresentarCuestionarioViewController.OnLoading(true);
        String json = Utils.ConvertModelToStringGson(mCuestionario);
        Gson mGson = new Gson();
        PresentarCuestionarioActivity.cuestionarioResponse mResponse = mGson.fromJson(ok, PresentarCuestionarioActivity.cuestionarioResponse.class);
        mCuestionario = mResponse.mCuestionario;
        final Cuestionario finalMCuestionario = mCuestionario;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresentarCuestionarioViewController.OnLoading(false);
                mPresentarCuestionarioViewController.OnsuccesGetCuestionario(finalMCuestionario);
            }
        }, 2000);

        /*if (json != null) {
            mPresentarCuestionarioViewController.OnsuccesGetCuestionario(mCuestionario);
        } else {
            mPresentarCuestionarioViewController.OnLoading(false);
            mPresentarCuestionarioViewController.Onfailed("Ocurrio un error");
        }*/
    }

    public void GuardarRespuesta(GuardarRespuesta mGuardarRespuesta, final Respuestas mRespuestas) {
        mPresentarCuestionarioViewController.OnLoading(true);
        String json = Utils.ConvertModelToStringGson(mGuardarRespuesta);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresentarCuestionarioViewController.OnLoading(false);

                mPresentarCuestionarioViewController.OnsuccesGuardarRespuesta(mRespuestas);

            }
        }, 1000);
     /*     if (json != null) {
            mPresentarCuestionarioViewController.OnsuccesGuardarRespuesta(mCuestionario);
        } else {
            mPresentarCuestionarioViewController.OnLoading(false);
            mPresentarCuestionarioViewController.Onfailed("Ocurrio un error");
        }
*/
    }

    private final String ok = "{\n" +
            "  \"Estatus\": 1,\n" +
            "  \"Data\": {\n" +
            "    \"nbCuestionario\": \"nbCuestionario Demo\",\n" +
            "    \"Preguntas\": [\n" +
            "      {\n" +
            "        \"idPregunta\": 1,\n" +
            "        \"nbPregunta\": \"<p><span style=\\\"color: #2c4467; font-family: Roboto, sans-serif; background-color: rgba(255, 255, 255, 0.3);\\\">El n&uacute;mero 0.25 es igual a la suma de:</span></p>\",\n" +
            "        \"desPregunta\": \"Demo desPregunta\",\n" +
            "        \"Estatus\": {\n" +
            "          \"idEstatus\": 1,\n" +
            "          \"desEstatus\": \"Pendiente\"\n" +
            "        },\n" +
            "        \"TipoPregunta\": {\n" +
            "          \"idTipoPregunta\": 12,\n" +
            "          \"desTipoPregunta\": \"Selección múltiple\"\n" +
            "        },\n" +
            "        \"Respuestas\": [\n" +
            "          {\n" +
            "            \"idRespuesta\": 1,\n" +
            "            \"nbRespuesta\": \"1/4\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 2,\n" +
            "            \"nbRespuesta\": \"1/5\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 3,\n" +
            "            \"nbRespuesta\": \"1/6\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 4,\n" +
            "            \"nbRespuesta\": \"1/7\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"idPregunta\": 2,\n" +
            "        \"nbPregunta\": \"<p><img title=\\\"\\frac{4}{9}m^{2}\\\" src=\\\"http://latex.codecogs.com/gif.latex?\\frac{4}{9}m^{2}\\\" /></p> \",\n" +
            "        \"desPregunta\": \"Demo desPregunta 2\",\n" +
            "        \"Estatus\": {\n" +
            "          \"idEstatus\": 1,\n" +
            "          \"desEstatus\": \"Pendiente\"\n" +
            "        },\n" +
            "        \"TipoPregunta\": {\n" +
            "          \"idTipoPregunta\": 12,\n" +
            "          \"desTipoPregunta\": \"Selección múltiple\"\n" +
            "        },\n" +
            "        \"Respuestas\": [\n" +
            "          {\n" +
            "            \"idRespuesta\": 1,\n" +
            "            \"nbRespuesta\": \"Demo 1\",\n" +
            "            \"desRutaFoto\": \"https://4.bp.blogspot.com/-9RCRJrebl-w/VuqdXNTI2oI/AAAAAAAAAUI/FfGB0Fwuy_wa_5W-etfpi9CMUNtDnQl4w/w1200-h630-p-k-no-nu/colegio-cerrado.jpg\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 2,\n" +
            "            \"nbRespuesta\": \"Demo 2\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 3,\n" +
            "            \"nbRespuesta\": \"Demo 3\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 4,\n" +
            "            \"nbRespuesta\": \"Demo 4\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"idPregunta\": 2,\n" +
            "        \"nbPregunta\": \"<p>Hola mundo</p> \",\n" +
            "        \"desPregunta\": \"Demo desPregunta 2\",\n" +
            "        \"Estatus\": {\n" +
            "          \"idEstatus\": 1,\n" +
            "          \"desEstatus\": \"Pendiente\"\n" +
            "        },\n" +
            "        \"TipoPregunta\": {\n" +
            "          \"idTipoPregunta\": 12,\n" +
            "          \"desTipoPregunta\": \"Selección múltiple\"\n" +
            "        },\n" +
            "        \"Respuestas\": [\n" +
            "          {\n" +
            "            \"idRespuesta\": 1,\n" +
            "            \"nbRespuesta\": \"Demo 1\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 2,\n" +
            "            \"nbRespuesta\": \"Demo 2\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 3,\n" +
            "            \"nbRespuesta\": \"Demo 3\",\n" +
            "            \"desRutaFoto\": \"https://image.shutterstock.com/image-vector/classical-school-building-bus-isolated-260nw-306667898.jpg\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 4,\n" +
            "            \"nbRespuesta\": \"Demo 4\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"idPregunta\": 2,\n" +
            "        \"nbPregunta\": \"<div class=\\\"row\\\"><p>&iquest;Qu&eacute; agencia es la m&aacute;s barata despu&eacute;s de aplicar el descuento por pago en efectivo? Redondea el resultado a la unidad m&aacute;s cercana.</p><div class=\\\"col l12 m12 s12 center-align\\\"><img class=\\\"responsive-img\\\" style=\\\"display: block; margin-left: auto; margin-right: auto;\\\" src=\\\"http://rcsa-dab1.com/files/files/mszddjlmkevhb03.png\\\" /></div></div>\",\n" +
            "        \"desPregunta\": \"Demo desPregunta 2\",\n" +
            "        \"Estatus\": {\n" +
            "          \"idEstatus\": 1,\n" +
            "          \"desEstatus\": \"Pendiente\"\n" +
            "        },\n" +
            "        \"TipoPregunta\": {\n" +
            "          \"idTipoPregunta\": 12,\n" +
            "          \"desTipoPregunta\": \"Selección múltiple\"\n" +
            "        },\n" +
            "        \"Respuestas\": [\n" +
            "          {\n" +
            "            \"idRespuesta\": 1,\n" +
            "            \"nbRespuesta\": \"Demo 1\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 2,\n" +
            "            \"nbRespuesta\": \"Demo 2\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 3,\n" +
            "            \"nbRespuesta\": \"Demo 3\",\n" +
            "            \"desRutaFoto\": \"https://planosinfin.com/wp-content/uploads/2015/08/escuelas77-770x631.jpg\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"idRespuesta\": 4,\n" +
            "            \"nbRespuesta\": \"Demo 4\",\n" +
            "            \"desRutaFoto\": \"\",\n" +
            "            \"fgSeleccionado\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"Mensaje\": \"Operación realizada con éxito.\"\n" +
            "}";
}
