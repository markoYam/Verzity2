package com.dwmedios.uniconekt.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.SplashPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.PaquetesActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.LoginActivity2;
import com.dwmedios.uniconekt.view.activity.View_Utils.DialogActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.SplashViewController;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.View_Utils.DialogActivity.KEY_DIALOG;

public class SplashActivity extends BaseActivity implements SplashViewController {
    private SplashPresenter mSplashPresenter;
    @BindView(R.id.imageLogo)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        Glide.with(getApplicationContext())
                .load(R.drawable.logo_white)
                .into(mImageView);
        mSplashPresenter = new SplashPresenter(SplashActivity.this, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            in.setDuration(500);
            mImageView.startAnimation(in);
            mImageView.setVisibility(View.VISIBLE);
        }
     //  System.out.print("Key"+generarHash());
    }

    public String generarHash() {
        PackageInfo mPackageInfo;
        String key = null;
        try {
            mPackageInfo = getPackageManager().getPackageInfo("com.dwmedios.uniconekt", PackageManager.GET_SIGNATURES);
            for (Signature mSignature : mPackageInfo.signatures)

            {
                MessageDigest messageDigest;
                messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(mSignature.toByteArray());
                key = new String(Base64.encode(messageDigest.digest(), 0));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return key;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
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
            }, 500);
        } else {
            /*** api <=19*/
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


    }

    public void tryDownloadConfig() {
        Log.e("Configuraciones", "Error al descargar");
        DialogActivity.handleDialog mHandleDialog = new DialogActivity.handleDialog();
        mHandleDialog.logo = R.drawable.ic_action_information;
        mHandleDialog.titulo = "Error";
        mHandleDialog.touchOutSide = false;
        mHandleDialog.buttonCancel = true;
        mHandleDialog.contenido = "Ocurrio un error al descargar las configuraciones. ¿Intentar nuevamente? ";
        startActivityForResult(new Intent(getApplicationContext(), DialogActivity.class).putExtra(KEY_DIALOG, mHandleDialog), 201);

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
        }, 1000);

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
        DialogActivity.handleDialog mHandleDialog = new DialogActivity.handleDialog();
        mHandleDialog.logo = R.drawable.ic_action_information;
        mHandleDialog.titulo = "Atención";
        mHandleDialog.contenido = contenido;
        startActivityForResult(new Intent(getApplicationContext(), DialogActivity.class).putExtra(KEY_DIALOG, mHandleDialog), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                if (mSplashPresenter.borrarTodo()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity2.class));
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
        if (requestCode == 201) {
            if (resultCode == RESULT_OK) {
                mSplashPresenter.getConguraciones();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }
}

