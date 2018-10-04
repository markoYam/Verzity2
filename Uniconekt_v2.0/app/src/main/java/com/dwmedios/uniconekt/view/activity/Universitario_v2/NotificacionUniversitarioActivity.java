package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.NotifiacionEstuResponse;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.NotificacionStatus;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.model.cuestionarios.EvaluacionesPersona;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.PresentarCuestionarioActivity.KEY_PRESENTAR;
import static com.dwmedios.uniconekt.view.util.Utils.configurefechaCompleted;
import static com.dwmedios.uniconekt.view.util.Utils.getDays;

public class NotificacionUniversitarioActivity extends BaseActivity {
    @BindView(R.id.textViewTitulo)
    TextView mTextViewTitulo;
    @BindView(R.id.textViewMensaje)
    TextView mTextViewMensaje;
    @BindView(R.id.buttonEvaluaciones)
    Button mButtonEvaluaciones;
    @BindView(R.id.buttonPresentar)
    Button mButtonPresentar;
    @BindView(R.id.textViewFecha)
    TextView mTextViewFecha;
    @BindView(R.id.contenedor)
    CardView mCardView;
    Notificaciones mNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_universitario);
        ButterKnife.bind(this);
        mButtonEvaluaciones.setOnClickListener(mOnClickListener);
        mButtonPresentar.setOnClickListener(mOnClickListener);
        setMargin();
        getInfo();
        actualizarStatus();
    }

    private void setMargin() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 0, 20, 0);
        mCardView.setLayoutParams(layoutParams);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonEvaluaciones:
                    Intent intent = new Intent(getApplicationContext(), EvaluacionesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.buttonPresentar:
                    EvaluacionesPersona mEvaluacionesPersona = new EvaluacionesPersona();
                    mEvaluacionesPersona.idEvaluacion = mNotificaciones.id_discriminador;
                    mEvaluacionesPersona.idPersona = mNotificaciones.id_persona_recibe;
                    startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                    break;
            }
        }
    };

    private void getInfo() {
        try {
            //Notificacion...
            Gson mGson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            String json = getIntent().getExtras().get("msg").toString();
            mNotificaciones = mGson.fromJson(json, Notificaciones.class);
            if (mNotificaciones != null) {
                setDatos();
            }
        } catch (Exception ex) {
            //Objeto....
            try {
                mNotificaciones = getIntent().getExtras().getParcelable("msg1");
                setDatos();
            } catch (Exception ex1) {
                showMessage("No se encontró información sobre la notificación.");
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setDatos() {
        mTextViewFecha.setText(getDays(mNotificaciones.fecha) + " " + configurefechaCompleted(mNotificaciones.fecha));
        mTextViewMensaje.setText(mNotificaciones.mensaje);
        mTextViewTitulo.setText(mNotificaciones.asunto);
    }

    private AllController mAllController;
    private ClientService mClientService;

    public void actualizarStatus() {
        mAllController = new AllController(getApplicationContext());
        mClientService = new ClientService(getApplicationContext());
        Dispositivo mPersona = mAllController.getDispositivoPersona();
        NotificacionStatus mNotificacionStatus = new NotificacionStatus();
        mNotificacionStatus.id_notificacion = mNotificaciones.id;
        mNotificacionStatus.id_dispositivo = mPersona.id;

        String json = Utils.ConvertModelToStringGson(mNotificacionStatus);
        if (json != null) {
            mClientService.getAPI().ActualizarNot(json).enqueue(new Callback<NotifiacionEstuResponse>() {
                @Override
                public void onResponse(Call<NotifiacionEstuResponse> call, Response<NotifiacionEstuResponse> response) {
                    //exito...........
                }

                @Override
                public void onFailure(Call<NotifiacionEstuResponse> call, Throwable t) {
//fallo..................
                }
            });
        } else {
//Error
        }

    }
}
