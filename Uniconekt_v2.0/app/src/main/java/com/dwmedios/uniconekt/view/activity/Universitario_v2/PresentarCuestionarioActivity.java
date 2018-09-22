package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.GuardarRespuesta;
import com.dwmedios.uniconekt.model.cuestionarios.Cuestionario;
import com.dwmedios.uniconekt.model.cuestionarios.Preguntas;
import com.dwmedios.uniconekt.model.cuestionarios.Respuestas;
import com.dwmedios.uniconekt.presenter.PresentarCuestionarioPresenter;
import com.dwmedios.uniconekt.view.activity.View_Utils.DialogActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.util.ImageUtils;
import com.dwmedios.uniconekt.view.viewmodel.PresentarCuestionarioViewController;
import com.google.gson.annotations.SerializedName;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.View_Utils.DialogActivity.KEY_DIALOG;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class PresentarCuestionarioActivity extends BaseActivity implements PresentarCuestionarioViewController {
    public static final String RESUELTO = "RESUELTO";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.cardTerminar)
    CardView mCardViewTerminar;
    @BindView(R.id.cardResponder)
    CardView mCardViewResponder;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    /*  @BindView(R.id.swiperefresh)
      SwipeRefreshLayout mSwipeRefreshLayout;*/
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpty;
    @BindView(R.id.webviewPregunta)
    WebView mWebView;
    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar;
    @BindView(R.id.textViewcountPreguntas)
    TextView mTextViewCount;
    @BindView(R.id.textViewResponder)
    TextView mTextViewResponder;
    @BindView(R.id.contenedorNest)
    NestedScrollView mNestedScrollView;
    int posicionActual = 1, numPreguntas = 0, posicionLista = 0;
    Cuestionario mCuestionario;
    private CustomAdapter mCustomAdapter;
    private List<Respuestas> mRespuestasList;
    private PresentarCuestionarioPresenter mPresentarCuestionarioPresenter;
    private int idRespuestSelccionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentar_cuestionario);
        ButterKnife.bind(this);
        mToolbar.setTitle("Cuestionario Demo");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresentarCuestionarioPresenter = new PresentarCuestionarioPresenter(this);
        mCardViewResponder.setOnClickListener(mOnClickListener);
        mCardViewTerminar.setOnClickListener(mOnClickListener);
        mPresentarCuestionarioPresenter.getCuestionario(new Cuestionario());

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cardResponder:
                    siguiente();
                    break;
                case R.id.cardTerminar:
                    finalizar();
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finalizar();
        return true;
    }

    @Override
    public void onBackPressed() {
        finalizar();
    }

    private void configurePregunta(Preguntas pregunta) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        String mostrarFinal = template.replace("@body", pregunta.nombre);
        mWebView.loadData(mostrarFinal, "text/html; charset=utf-8", "UTF-8");
        configureRecyclerView(pregunta.mRespuestasList);
    }

    private void loadFirt(Cuestionario mCuestionario) {
        if (mCuestionario != null) {
            this.mCuestionario = mCuestionario;
            restaurarFormulario();
            if (mCuestionario.mPreguntasList != null && mCuestionario.mPreguntasList.size() > 0) {
                numPreguntas = mCuestionario.mPreguntasList.size();
                if (numPreguntas > 0) {
                    mProgressBar.setMax(mCuestionario.mPreguntasList.size());
                    if (posicionActual <= numPreguntas) {
                        mTextViewCount.setText((posicionActual) + "/" + numPreguntas);
                        mProgressBar.setProgress((posicionActual));
                    } else {
                        mTextViewCount.setText((posicionActual) + "/" + numPreguntas);
                        mProgressBar.setProgress(posicionActual);
                    }

                }
                configurePregunta(mCuestionario.mPreguntasList.get(posicionLista));
            } else {
                showMessage("No hay preguntas");
                finish();
            }
        }
    }

    private void siguiente() {

        if (mCuestionario != null && mCuestionario.mPreguntasList != null && mCuestionario.mPreguntasList.size() > 0) {
            if (idRespuestSelccionada != -1) {
                if (posicionActual <= numPreguntas) {
                    GuardarRespuesta mGuardarRespuesta = new GuardarRespuesta();
                    mGuardarRespuesta.idCuestionario = mCuestionario.id;
                    mGuardarRespuesta.idPregunta = mCuestionario.mPreguntasList.get(posicionLista).id;
                    List<Respuestas> respuestasList = new ArrayList<>();
                    Respuestas mRespuestas = new Respuestas();
                    mRespuestas.id = idRespuestSelccionada;
                    respuestasList.add(mRespuestas);
                    mGuardarRespuesta.mRespuestasList = this.mRespuestasList;
                    mPresentarCuestionarioPresenter.GuardarRespuesta(mGuardarRespuesta, mRespuestas);
                } else {
                    if ((posicionActual) == (numPreguntas)) {
                        mTextViewResponder.setText("Finalizar");
                        showMessage("llego a fin sig");
                    }
                }
            } else {
                showMessage("Seleccione una respuesta para continuar.");
            }
        }
    }

    private void finalizar() {
        String contenido = "¿Desea cancelar la presentación del cuestionario? Se guardará su progreso actual.";
        DialogActivity.handleDialog mHandleDialog = new DialogActivity.handleDialog();
        mHandleDialog.logo = R.drawable.ic_action_highlight_off;
        mHandleDialog.titulo = "Atención";
        mHandleDialog.buttonCancel = true;
        mHandleDialog.color = getResources().getColor(R.color.colorrojo);
        mHandleDialog.contenido = contenido;
        startActivityForResult(new Intent(getApplicationContext(), DialogActivity.class).putExtra(KEY_DIALOG, mHandleDialog), 200);

    }

    private void VerResultado() {
        String contenido = "Felicidades usted postula para ............";
        DialogActivity.handleDialog mHandleDialog = new DialogActivity.handleDialog();
        mHandleDialog.logo = R.drawable.ic_action_check_circle_outline;
        mHandleDialog.titulo = "Atención";
        mHandleDialog.touchOutSide = false;
        mHandleDialog.color = getResources().getColor(R.color.colorPrimaryDark2);
        mHandleDialog.contenido = contenido;
        startActivityForResult(new Intent(getApplicationContext(), DialogActivity.class).putExtra(KEY_DIALOG, mHandleDialog), 201);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    //guardar y finalizar
                    showMessage("bye");
                    finish();
                }
                break;
            case 201:
                if (resultCode == RESULT_OK) {
                    //guardar y finalizar
                    showMessage("bye");
                    finish();
                }
                break;
        }
    }

    public void configureRecyclerView(List<Respuestas> mPreguntas) {
        mRespuestasList = mPreguntas;
        if (mPreguntas != null && mPreguntas.size() > 0) {
            mTextViewEmpty.setVisibility(View.GONE);
            // mSwipeRefreshLayout.setRefreshing(false);
            mCustomAdapter = new CustomAdapter(mPreguntas, R.layout.row_preguntas, mConfigureHolder);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapter);

        } else {
            mRecyclerView.setAdapter(null);

            this.EmpyRecycler();
        }
    }

    CustomAdapter.ConfigureHolder mConfigureHolder = new CustomAdapter.ConfigureHolder() {
        RadioButton mRadioButton;
        ImageView mImageView;
        CardView mCardView;

        @Override
        public void Configure(View itemView, Object mObject) {
            final Respuestas mRespuesta = (Respuestas) mObject;
            mRadioButton = itemView.findViewById(R.id.radio);
            mImageView = itemView.findViewById(R.id.imagenPregunta);
            mCardView = itemView.findViewById(R.id.cardview);

            if (!isNullOrEmpty(mRespuesta.nombre)) {
                mRadioButton.setText(mRespuesta.nombre);
            } else {
                mRadioButton.setText("");
            }

            if (!isNullOrEmpty(mRespuesta.foto)) {
                mImageView.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(mRespuesta.foto, mImageView, ImageUtils.OptionsImageLoaderItems);
            } else {
                mImageView.setVisibility(View.GONE);
            }
            if (mRespuesta.isSeleccionado) {
                //Asignar el id de la respuesta seleccionada
                idRespuestSelccionada = mRespuesta.id;
               // showMessage(mRespuesta.nombre + "-" + idRespuestSelccionada);
                mRadioButton.setChecked(true);
            } else
                mRadioButton.setChecked(false);
            mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mRespuestasList != null && mRespuestasList.size() > 0) {
                        for (int i = 0; i < mRespuestasList.size(); i++) {
                            if (mRespuestasList.get(i).id == mRespuesta.id) {
                                mRespuestasList.get(i).isSeleccionado = isChecked;
                            } else {
                                mRespuestasList.get(i).isSeleccionado = !isChecked;
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCustomAdapter != null) {
                                    mCustomAdapter.mList = mRespuestasList;
                                    mCustomAdapter.notifyDataSetChanged();
                                    mRecyclerView.setAdapter(mCustomAdapter);
                                }
                            }
                        }, 100);
                    }
                }
            });
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRespuestasList != null && mRespuestasList.size() > 0) {
                        for (int i = 0; i < mRespuestasList.size(); i++) {
                            if (mRespuestasList.get(i).id == mRespuesta.id) {
                                mRespuestasList.get(i).isSeleccionado = true;
                            } else {
                                mRespuestasList.get(i).isSeleccionado = false;
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCustomAdapter != null) {
                                    mCustomAdapter.mList = mRespuestasList;
                                    mCustomAdapter.notifyDataSetChanged();
                                    mRecyclerView.setAdapter(mCustomAdapter);
                                }
                            }
                        }, 250);
                    }
                }
            });
        }

        @Override
        public void Onclick(Object mObject) {

        }
    };

    private void EmpyRecycler() {
        mTextViewEmpty.setVisibility(View.VISIBLE);
        // mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }

    private void restaurarFormulario() {
        int position = 0;
        for (Preguntas mPreguntas : mCuestionario.mPreguntasList) {
            position++;
            if (mPreguntas.mEstatus.estatusNot.equals(RESUELTO)) {
                posicionActual = (position + 1);
                posicionLista = position;
                break;
            }
        }
    }

    @Override
    public void OnsuccesGetCuestionario(Cuestionario mCuestionario) {
        loadFirt(mCuestionario);
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void OnLoading(boolean isLoading) {
        if (isLoading)
            showOnProgressDialog("Cargando...");
        else
            dismissProgressDialog();
    }

    @Override
    public void GuardarRespuesta(GuardarRespuesta mGuardarRespuesta, Respuestas mRespuestas) {
        showMessage("En contrucción");
    }

    private boolean isUltimo() {
        if (posicionActual == numPreguntas) {
            showMessage("llego a fin");
            return true;
        } else
            return false;
    }

    @Override
    public void OnsuccesGuardarRespuesta(Respuestas mRespuestas) {
        if (mCuestionario != null && mCuestionario.mPreguntasList != null && mCuestionario.mPreguntasList.size() > 0) {
            if (!isUltimo()) {
                mNestedScrollView.scrollTo(0, 0);
                posicionActual++;
                posicionLista++;
                idRespuestSelccionada = -1;
                if ((posicionActual) == numPreguntas) {
                    mTextViewResponder.setText("Finalizar");
                }
                mTextViewCount.setText((posicionActual) + "/" + numPreguntas);
                mProgressBar.setProgress((posicionActual));
                configurePregunta(mCuestionario.mPreguntasList.get(posicionLista));
            } else {
                showMessage("Cuestionario finalizado");
                VerResultado();
                //finalizar cuestionario
            }

        }
        //showMessage("En contrucción");
    }


    @Override
    public void getResultado(Cuestionario mCuestionario) {
        showMessage("En contrucción");
    }

    public class cuestionarioResponse {
        @SerializedName("Data")
        public Cuestionario mCuestionario;
    }


    private String template = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "<style>\n" +
            "* {\n" +
            "  box-sizing: border-box;\n" +
            "}\n" +
            ".main {\n" +
            "  float: left;\n" +
            "  width: 100%;\n" +
            "  height: 100%;\n" +
            "  padding: 0 20px;\n" +
            "  overflow: hidden;\n" +
            "}\n" +
            ".responsive-img\n" +
            "{\n" +
            "  max-width:100%;\n" +
            "  height:auto;\n" +
            "}\n" +
            "\n" +
            "</style>\n" +
            "</head>\n" +
            "<body style=\"font-family:Verdana; width: 100%;height: 100%\">\n" +
            "  <div class=\"main\">\n" +
            "@body\n" +
            "  </div>\n" +
            "\n" +
            "\n" +
            "</body>\n" +
            "</html>";
}
