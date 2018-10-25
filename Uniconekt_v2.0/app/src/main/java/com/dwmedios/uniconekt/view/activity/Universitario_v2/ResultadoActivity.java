package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.cuestionarios.Resultados;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoActivity extends BaseActivity {
    public static final String KEY_RESULTADO = "KEY";
    @BindView(R.id.buttonOk)
    ImageButton mButton;
    @BindView(R.id.webviewMensaje)
    WebView mWebView;
    @BindView(R.id.textViewTitulo)
    TextView mTextViewTitulo;
    @BindView(R.id.textViewPuntaje)
    TextView mTextViewPuntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        ButterKnife.bind(this);
        Resultados mResultados = getIntent().getExtras().getParcelable(KEY_RESULTADO);
        if (mResultados != null) {
            mTextViewTitulo.setText(mResultados.nombre);
            mTextViewPuntaje.setText(mResultados.puntaje);
            configurePregunta(mResultados.mensaje);
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configurePregunta(String pregunta) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        /** quitar cliente para que los hipervinculos los abra en otro navegador**/
        // mWebView.setWebViewClient(new WebViewClient());
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        String mostrarFinal = template.replace("@body", pregunta);
        mWebView.loadData(mostrarFinal, "text/html; charset=utf-8", "UTF-8");
        // configureRecyclerView(pregunta.mRespuestasList);
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    private String template = "<!DOCTYPE html>\n" +
            "<head>\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "<style type=\"text/css\">\n" +
            "img{\n" +
            "      max-width:100% !important;\n" +
            "      border-radius: .25rem;\n" +
            "      height:50% !important;\n" +
            "}\n" +
            ".main{\n" +
            "      width: 100%\n" +
            "}\n" +
            "</style>\n" +
            "</head>\n" +
            "<body style=\"font-family: Verdana;\">\n" +
            "      <div class=\"main\">\n" +
            "            @body\n" +
            "      </div>\n" +
            "</body>\n" +
            "</html>";
}
