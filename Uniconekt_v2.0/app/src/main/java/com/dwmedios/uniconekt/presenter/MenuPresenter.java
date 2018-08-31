package com.dwmedios.uniconekt.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.DispositivoResponse;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.MenuController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;
import static com.dwmedios.uniconekt.view.util.Utils.getDrawable;

public class MenuPresenter {
    private MenuController menuController;
    private Context mContext;
    private AllController mAllController;
    private ClientService mClientService;

    public MenuPresenter(MenuController menuController, Context mContext) {
        this.menuController = menuController;
        this.mContext = mContext;
        mAllController = new AllController(mContext);
        mClientService = new ClientService(mContext);
    }

    @SuppressLint("ResourceType")
    public void ConfigureMenu() {
        menuController.OnLoading(true);
        List<ClasViewModel.menu> lisMen = new ArrayList<>();
        int typeUser = SharePrefManager.getInstance(mContext).getTypeUser();
        if (typeUser == 1) {

            ClasViewModel.menu menu = new ClasViewModel.menu();
            menu.nombre = "Buscar universidades";
            menu.tipo = ClasViewModel.tipoMenu.Universidad;
            //menu.drawableImage = getDrawable("ic_search_2", mContext);
            menu.drawableImage = R.drawable.ic_search_2;
            menu.color = mContext.getString(R.color.Color_buscarUniversidad);
            lisMen.add(menu);


            ClasViewModel.menu menu1 = new ClasViewModel.menu();
            menu1.nombre = "Becas";
            menu1.tipo = ClasViewModel.tipoMenu.Becas;
            //menu1.drawableImage = getDrawable("ic_becas", mContext);
            menu1.drawableImage = R.drawable.ic_becas;
            menu1.color = mContext.getString(R.color.Color_becas);
            lisMen.add(menu1);

            ClasViewModel.menu menu3 = new ClasViewModel.menu();
            menu3.nombre = "Examen Vocacional";
            menu3.tipo = ClasViewModel.tipoMenu.examen;
            menu3.drawableImage = R.drawable.ic_examen;
            menu3.color = mContext.getString(R.color.Color_examen);
            lisMen.add(menu3);


            ClasViewModel.menu menu4 = new ClasViewModel.menu();
            menu4.nombre = "Estudia en el Extranjero";
            menu4.tipo = ClasViewModel.tipoMenu.extranjero;
            menu4.drawableImage = R.drawable.aeroplane;
            menu4.color = mContext.getString(R.color.Color_extranjero);
            lisMen.add(menu4);




/*
            ClasViewModel.menu menu5 = new ClasViewModel.menu();
            menu5.nombre = "Viaja al extranjero";
            menu5.tipo = ClasViewModel.tipoMenu.Viaja;
            menu5.drawableImage = getDrawable("ic_world", mContext);
            lisMen.add(menu5);*/

        } else if (typeUser == 2) {
            ClasViewModel.menu menu = new ClasViewModel.menu();
            menu.nombre = "Paquetes";
            menu.tipo = ClasViewModel.tipoMenu.paquetes;
            //menu.drawableImage = getDrawable("ic_dollar", mContext);
            menu.drawableImage = R.drawable.ic_comprar_paquete;
            menu.color = mContext.getString(R.color.Color_financiamientos);
            lisMen.add(menu);

            ClasViewModel.menu menu1 = new ClasViewModel.menu();
            menu1.nombre = "Postulados";
            menu1.tipo = ClasViewModel.tipoMenu.postulados;
            //menu1.drawableImage = getDrawable("ic_dollar", mContext);
            menu1.drawableImage = R.drawable.ic_programas_academicos;
            menu1.color = mContext.getString(R.color.Color_web);
            lisMen.add(menu1);
        }

        if (lisMen != null && !lisMen.isEmpty()) {
            menuController.OnSucces(lisMen);
        } else {
            menuController.OnFailed("No se pudo cargar el menu");
            menuController.empyRecycler();
        }

        menuController.OnLoading(false);
    }

    public void ConfigureCabeceras() {
        List<Persona> mPersonas = mAllController.getPersona();
        if (mPersonas != null && mPersonas.size() > 0) {
            Persona mPersona = mPersonas.get(0);
            if (!mPersona.nombre.equals("temp")) {
                List<Usuario> mUsuarioList = mAllController.getUsuario();
                if (mUsuarioList != null && mUsuarioList.size() > 0) {
                    Usuario mUsuario = mUsuarioList.get(0);
                    menuController.OnLoadHeaders(mPersona, mUsuario);
                } else {
                    menuController.OnLoadHeaders(mPersona, null);
                }
            }
        } else {
            menuController.OnLoadHeaders(null, null);
        }
    }

    public void closeSesion() {
        menuController.OnLoading(true);
        Dispositivo mDispositivo = mAllController.getDispositivoPersona();
        if (mDispositivo != null) {
            String json = ConvertModelToStringGson(mDispositivo);
            if (json != null) {
                mClientService.getAPI()
                        .cerrarSesion(json)
                        .enqueue(new Callback<DispositivoResponse>() {
                            @Override
                            public void onResponse(Call<DispositivoResponse> call, Response<DispositivoResponse> response) {
                                DispositivoResponse res = response.body();
                                menuController.OnLoading(false);
                                if (res != null) {
                                    if (res.status == 1) {
                                        menuController.succesCerrarSession();
                                    } else if (res.status == 0) {
                                        menuController.OnFailed(res.mensaje);
                                    } else {
                                        menuController.OnFailed(ERROR_CONECTION);
                                    }
                                } else {
                                    menuController.OnFailed(ERROR_CONECTION);
                                }
                            }

                            @Override
                            public void onFailure(Call<DispositivoResponse> call, Throwable t) {
                                menuController.OnLoading(false);
                                menuController.OnFailed(ERROR_CONECTION);
                            }
                        });
            } else {
                menuController.OnLoading(false);
                menuController.OnFailed(ERROR_CONECTION);
            }
        } else {
            menuController.OnLoading(false);
            menuController.OnFailed(ERROR_CONECTION);
        }
    }

    public boolean closeSesionLocal() {
        return mAllController.clearAllTables();
    }
}
