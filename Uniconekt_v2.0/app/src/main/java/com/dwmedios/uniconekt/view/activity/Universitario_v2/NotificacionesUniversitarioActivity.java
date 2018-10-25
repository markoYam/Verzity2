package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.NotifiacionEstuResponse;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.NotificacionStatus;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.model.cuestionarios.EvaluacionesPersona;
import com.dwmedios.uniconekt.presenter.NotificacionUniversitarioPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.NotificacionUniversitarioViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.PresentarCuestionarioActivity.KEY_PRESENTAR;
import static com.dwmedios.uniconekt.view.util.Utils.configurefechaCompleted;
import static com.dwmedios.uniconekt.view.util.Utils.getDays;

public class NotificacionesUniversitarioActivity extends BaseActivity implements NotificacionUniversitarioViewController {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    public NotificacionUniversitarioPresenter mNotificacionUniversitarioPresenter;
    private CustomAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones_universitario);
        ButterKnife.bind(this);
        mToolbar.setTitle("Notificaciones");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNotificacionUniversitarioPresenter = new NotificacionUniversitarioPresenter(getApplicationContext(), this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mNotificacionUniversitarioPresenter.getNotificaciones();
            }
        });
    }

    @Override
    protected void onStart() {
        if (!isProces)
            mNotificacionUniversitarioPresenter.getNotificaciones();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.startActivityUp(getApplicationContext(), MainUniversitarioActivity.class);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Utils.startActivityUp(getApplicationContext(), MainUniversitarioActivity.class);
    }

    @Override
    public void Onsucces(List<Notificaciones> mNotificacionesList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextView.setVisibility(View.GONE);
        configureRecyclerView(mNotificacionesList);
    }

    public void configureRecyclerView(List<Notificaciones> mNotificacionesList) {
        if (mNotificacionesList != null && mNotificacionesList.size() > 0) {
            mCustomAdapter = new CustomAdapter(mNotificacionesList, R.layout.row_notificaciones, mConfigureHolder);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapter);
            Utils.setAnimRecyclerView(getApplicationContext(), R.anim.layout_animation, mRecyclerView);

        } else {
            mRecyclerView.setAdapter(null);

            this.EmpyRecycler();
        }
    }

    CustomAdapter.ConfigureHolder mConfigureHolder = new CustomAdapter.ConfigureHolder() {
        @BindView(R.id.textViewContenidoMensaje)
        TextView mTextViewContenido;
        @BindView(R.id.textViewAsunto)
        TextView mTextViewAsunto;
        @BindView(R.id.idCardviewnotificaciones)
        CardView mCardView;
        @BindView(R.id.imageviewIconoNotificaciones)
        ImageView mImageViewIcon;
        @BindView(R.id.imagebuttom)
        ImageButton mImageButton;

        @Override
        public void Configure(View itemView, Object mObject) {
            final Notificaciones mNotificaciones = (Notificaciones) mObject;
            mTextViewContenido = itemView.findViewById(R.id.textViewContenidoMensaje);
            mTextViewAsunto = itemView.findViewById(R.id.textViewAsunto);
            mCardView = itemView.findViewById(R.id.idCardviewnotificaciones);
            mImageViewIcon = itemView.findViewById(R.id.imageviewIconoNotificaciones);
            mImageButton = itemView.findViewById(R.id.imagebuttom);

            mTextViewAsunto.setText(mNotificaciones.asunto + " - " + getDays(mNotificaciones.fecha) + " " + configurefechaCompleted(mNotificaciones.fecha));
            mTextViewContenido.setText(mNotificaciones.mensaje);
            if (mNotificaciones.mNotificacionEstatusList != null && mNotificaciones.mNotificacionEstatusList.size() > 0) {
                if (mNotificaciones.mNotificacionEstatusList.get(0).mEstatus != null) {
                    if (mNotificaciones.mNotificacionEstatusList.get(0).mEstatus.estatusNot != null) {
                        switch (mNotificaciones.mNotificacionEstatusList.get(0).mEstatus.estatusNot) {
                            case "VISTO":
                                mImageViewIcon.setImageResource(R.drawable.ic_action_notifications);
                                mTextViewAsunto.setTypeface(Typeface.DEFAULT);
                                break;
                            case "PENDIENTE":
                                mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
                                mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                                break;
                            default:
                                mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                                mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
                                break;
                        }
                    } else {
                        mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                        mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
                    }
                } else {
                    mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                    mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
                }
            } else {
                mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
            }
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizarStatus(mNotificaciones.id);
                  //  NotificacionUniversitarioActivity.active = false;
                    //startActivityForResult(new Intent(getApplicationContext(), NotificacionUniversitarioActivity.class).putExtra("msg1", mNotificaciones), 200);
                    //showMessage(mNotificaciones.asunto);
                    EvaluacionesPersona mEvaluacionesPersona = new EvaluacionesPersona();
                    mEvaluacionesPersona.idEvaluacion = mNotificaciones.id_discriminador;
                    mEvaluacionesPersona.idPersona = mNotificaciones.id_persona_recibe;
                    startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));

                }
            });
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizarStatus(mNotificaciones.id);
                  //  NotificacionUniversitarioActivity.active = false;
                   // startActivityForResult(new Intent(getApplicationContext(), NotificacionUniversitarioActivity.class).putExtra("msg1", mNotificaciones), 200);
                    // showMessage(mNotificaciones.asunto);

                    EvaluacionesPersona mEvaluacionesPersona = new EvaluacionesPersona();
                    mEvaluacionesPersona.idEvaluacion = mNotificaciones.id_discriminador;
                    mEvaluacionesPersona.idPersona = mNotificaciones.id_persona_recibe;
                    startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));

                }
            });
        }

        @Override
        public void Onclick(Object mObject) {

        }
    };
    AllController mAllController;
    ClientService mClientService;

    public void actualizarStatus(int idNot) {
        mAllController = new AllController(getApplicationContext());
        mClientService = new ClientService(getApplicationContext());
        Dispositivo mPersona = mAllController.getDispositivoPersona();
        NotificacionStatus mNotificacionStatus = new NotificacionStatus();
        mNotificacionStatus.id_notificacion = idNot;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            mNotificacionUniversitarioPresenter.getNotificaciones();
        }
    }

    private void EmpyRecycler() {
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void OnFailed(String mensaje) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextView.setVisibility(View.VISIBLE);
        this.EmpyRecycler();
    }

    boolean isProces = false;

    @Override
    public void OnLoading(boolean isLoading) {
        this.isProces = isLoading;
        if (isLoading)
            showOnProgressDialog("Cargando...");
        else
            dismissProgressDialog();
    }
}
