package com.dwmedios.uniconekt.view.activity.Universitario;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.MenuPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad_v2.DatosUniversidad3Activity;
import com.dwmedios.uniconekt.view.activity.Universidad_v2.NotificacionesUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.LoginActivity2;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.MisAsesorActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.NotificacionesUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.fragments.MenuFragment;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.toast;
import com.dwmedios.uniconekt.view.viewmodel.MenuController;
import com.facebook.login.widget.ProfilePictureView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity.KEY_VER_PERFIL_REPRESENTANTE;
import static com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity.KEY_VER_PERFIL_UNIVERSITARIO;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class MainUniversitarioActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuController {
    public static String ACTIVITY_NOTIFICATIONS = "load_messages";
    ProfilePictureView mProfilePictureView;
    private MenuPresenter menuPresenter;
    private TextView mensajes;
    private NavigationView navigationView;
    public static Toolbar mToolbar;
    private boolean isReceiverRegistered;

    // TODO: 08/05/2018 1=>Universitario , 2=>Universidad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_universitario);
        //  ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Inicio");
        menuPresenter = new MenuPresenter(this, getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        this.configureCabeceras();
        fragmentManager.beginTransaction().replace(R.id.contenidoPanel, new MenuFragment()).commit();

    }

    toast.handleActions mHandleActions = new toast.handleActions() {
        @Override
        public void Onclick(View mView, Object m) {
            if (mView != null && m != null) {

                Notificaciones mNotificaciones = (Notificaciones) m;
                TextView mTextViewAsunto = mView.findViewById(R.id.textViewAsuntoCustom);
                TextView mTextViewContenido = mView.findViewById(R.id.textViewContenidoCustom);
                CardView mcCardView = mView.findViewById(R.id.linearRoot);
                mTextViewAsunto.setText(mNotificaciones.asunto);
                mTextViewContenido.setText(mNotificaciones.mensaje);
                mcCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMessage("Hola mundo");
                    }
                });
            }
        }
    };
    TextView mTextViewNombre;
    TextView mTextViewCorreo;
    ImageView mImageViewFoto;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mTextViewNombre = (TextView) findViewById(R.id.nameUser);
        mTextViewCorreo = (TextView) findViewById(R.id.emailUser);
        mImageViewFoto = (ImageView) findViewById(R.id.profile_image);
        mProfilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        menuPresenter.ConfigureCabeceras();
        //getMenuInflater().inflate(R.menu.main_universitario, menu);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mTextViewNombre != null)
            menuPresenter.ConfigureCabeceras();
        navigationView.getMenu().findItem(R.id.menu_inicio).setChecked(true);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       /* if (id == R.id.action_settings) {
            finish();
        }*/

        return super.onOptionsItemSelected(item);
    }

    FragmentManager fragmentManager = getSupportFragmentManager();
    int delay = 300;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_inicio) {
            getSupportActionBar().setTitle("Inicio");
            fragmentManager.beginTransaction().replace(R.id.contenidoPanel, new MenuFragment()).commit();
        }
        if (id == R.id.menu_opcion_cerrar) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(0);
            menuPresenter.closeSesion();
        }
        if (id == R.id.menu_perfil) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), DatosUniversidad3Activity.class));
                }
            }, delay);
        }
        if (id == R.id.menu_perfil_universitario) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (SharePrefManager.getInstance(getApplicationContext()).getTypeUser() == 1) {

                        KEY_VER_PERFIL_UNIVERSITARIO = true;
                        KEY_VER_PERFIL_REPRESENTANTE = false;
                        startActivity(new Intent(getApplicationContext(), DatosUniversitarioActivity.class));
                    } else if (SharePrefManager.getInstance(getApplicationContext()).getTypeUser() == 2) {
                        KEY_VER_PERFIL_UNIVERSITARIO = false;
                        KEY_VER_PERFIL_REPRESENTANTE = true;
                        startActivity(new Intent(getApplicationContext(), DatosUniversitarioActivity.class));
                    }
                }
            }, delay);

        }
        if (id == R.id.menu_mensajes) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int type = SharePrefManager.getInstance(getApplicationContext()).getTypeUser();
                    if (type == 1)
                        startActivity(new Intent(getApplicationContext(), NotificacionesUniversitarioActivity.class));
                    else
                        startActivity(new Intent(getApplicationContext(), NotificacionesUniversidadActivity.class));
                }
            }, delay);
        }
        if (id == R.id.menu_mis_asesores) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MisAsesorActivity.class));
                }
            }, delay);


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void toolbarTitle(String titulo) {
        mToolbar.setTitle(titulo);
    }

    @Override
    public void OnSucces(List<ClasViewModel.menu> menuList) {

    }

    @Override
    public void OnFailed(String mensaje) {

    }

    @Override
    public void empyRecycler() {

    }

    @Override
    public void OnLoading(boolean loading) {
        if (loading)
            showOnProgressDialog("Cargando...");
        else
            dismissProgressDialog();
    }

    @Override
    public void OnLoadHeaders(Persona mPersona, Usuario mUsuario) {
        if (mPersona != null) {
            if (mPersona != null) {
                mTextViewNombre.setVisibility(View.VISIBLE);
                mTextViewCorreo.setVisibility(View.VISIBLE);
                mTextViewCorreo.setText(mPersona.correo);
                mTextViewNombre.setText(mPersona.nombre);
                if (mPersona.foto != null) {
                    Glide.with(getApplicationContext())
                            .load(getUrlImage(mPersona.foto, getApplicationContext()))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    mImageViewFoto.setImageResource(R.drawable.profile);
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(mImageViewFoto);

                    //new taskImageP(getUrlImage(mPersona.foto, getApplicationContext())).execute();
                    // ImageLoader.getInstance().displayImage(getUrlImage(mPersona.foto, getApplicationContext()), mImageViewFoto, OptionsImageLoaderUser);
                } else {
                    if (mUsuario != null) {

                        if (mUsuario.cv_facebook != null) {
                            //  mImageViewFoto.setVisibility(View.GONE);
                            // mProfilePictureView.setVisibility(View.VISIBLE);
                            String url = "https://graph.facebook.com/" + mUsuario.cv_facebook + "/picture?type=large";
                            Glide.with(getApplicationContext())
                                    .load(url)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            mImageViewFoto.setImageResource(R.drawable.profile);
                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    })
                                    .into(mImageViewFoto);

                            new taskImage(mUsuario.cv_facebook).execute();
                            //  mProfilePictureView.setProfileId(mUsuario.cv_facebook);
                        }
                    }
                }
            }
        }
    }

    public class taskImageP extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;

        public taskImageP(String key) {
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(key);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bmp != null)
                mImageViewFoto.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void updateNumMensajes(int count) {
        if (mensajes != null) {
            if (count == 0) {
                mensajes.setVisibility(View.GONE);
            } else {
                mensajes.setText(count + "");
            }
        }
    }

    @Override
    public void configureCabeceras() {

        navigationView.getMenu().findItem(R.id.menu_inicio).setChecked(true);
        if (SharePrefManager.getInstance(getApplicationContext()).getTypeUser() == 2) {
            navigationView.getMenu().findItem(R.id.menu_perfil).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_mensajes).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_perfil_universitario).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_perfil_universitario).setTitle("Ver  Perfil representante");
            navigationView.getMenu().findItem(R.id.navigation_subheader).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_mis_asesores).setVisible(false);
            mensajes = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.menu_mensajes));
            mensajes.setGravity(Gravity.CENTER_VERTICAL);
            mensajes.setTypeface(null, Typeface.BOLD);
            mensajes.setTextColor(getResources().getColor(R.color.colorrojo));
            // TODO: 21/05/2018 este codigo es temporal
            this.updateNumMensajes(0);

        } else if (SharePrefManager.getInstance(getApplicationContext()).getTypeUser() == 1) {
            navigationView.getMenu().findItem(R.id.menu_perfil).setVisible(false);
            navigationView.getMenu().findItem(R.id.menu_mensajes).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_perfil_universitario).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_mis_asesores).setVisible(true);
            navigationView.getMenu().findItem(R.id.navigation_subheader).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_perfil_universitario).setTitle("Ver  Perfil universitario");
        }
    }

    @Override
    public void succesCerrarSession() {
        if (menuPresenter.closeSesionLocal()) ;
        {
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();
            Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public class taskImage extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;

        public taskImage(String key) {
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://graph.facebook.com/" + key + "/picture?type=large");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bmp != null)
                mImageViewFoto.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }
}
