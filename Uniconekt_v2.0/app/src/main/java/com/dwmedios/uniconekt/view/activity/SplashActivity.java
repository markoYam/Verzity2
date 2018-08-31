package com.dwmedios.uniconekt.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.SplashPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.PaquetesActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.LoginActivity2;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.SplashViewController;

public class SplashActivity extends BaseActivity implements SplashViewController {
    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mSplashPresenter = new SplashPresenter(SplashActivity.this, this);
        if (checkDatasMovil() || checkWifi()) {
            mSplashPresenter.getConguraciones();
        } else {
            showMessage("Sin conexión a internet.");
            showConfirmDialog1("Atención", "Sin conexión a internet.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
        }
    }

    public void tryDownloadConfig() {
        Log.e("Configuraciones", "Error al descargar");
        showdialogMaterialConfig("Error", "Ocurrio un error al descargar las configuraciones. ¿Intentar nuevamente? ", R.drawable.ic_action_information, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSplashPresenter.getConguraciones();
            }
        });
    }

    @Override
    public void Userloggedin() {
        if (SharePrefManager.getInstance(getApplicationContext()).getTypeUser() == 2) {
            mSplashPresenter.verificarUniversidad();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainUniversitarioActivity.class));
                    finish();
                }
            }, 500);
        }
    }

    @Override
    public void userNotLoggedIn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity2.class));
                finish();
            }
        }, 2000);

    }

    public static int TYPE_LOGIN_PAQUETES = 0;

    @Override
    public void OnsuccesVerifyUniversity(Universidad mUniversidad) {
        mSplashPresenter.updateUniversidad(mUniversidad);
        if (mUniversidad.mVentasPaquetesList != null && mUniversidad.mVentasPaquetesList.size() > 0) {
            mSplashPresenter.updatePaquete(mUniversidad.mVentasPaquetesList.get(0));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainUniversitarioActivity.class));
                    finish();
                }
            }, 1000);
        } else {
            mSplashPresenter.deletePaquetes();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TYPE_LOGIN_PAQUETES = 1;
                    Intent intent = new Intent(getApplicationContext(), PaquetesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }, 800);

        }
    }

    @Override
    public void OnFailedVerifyUniversity(String mensaje) {
        if (SharePrefManager.getInstance(getApplicationContext()).getTypeUser() == 2) {
            //aqui estas mensaje.......................................
            cancelValidateInfo();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainUniversitarioActivity.class));
                    finish();
                }
            }, 500);
        }
    }

    private void cancelValidateInfo() {
        String contenido = getString(R.string.universidad_desactivada);
        showdialogMaterial2("Atención", contenido, R.drawable.ic_action_information, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSplashPresenter.borrarTodo()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity2.class));
                    finish();
                }
            }
        });
    }
}

