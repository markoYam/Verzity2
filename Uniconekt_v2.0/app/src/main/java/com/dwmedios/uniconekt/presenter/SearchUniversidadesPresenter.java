package com.dwmedios.uniconekt.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.BannersResponse;
import com.dwmedios.uniconekt.data.service.response.UniversidadResponse;
import com.dwmedios.uniconekt.data.service.response.VisitasBannersResponse;
import com.dwmedios.uniconekt.model.Banners;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.VisitasBanners;
import com.dwmedios.uniconekt.view.viewmodel.MenuController;
import com.dwmedios.uniconekt.view.viewmodel.SearchUniversidadViewController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dwmedios.uniconekt.view.util.Utils.ConvertModelToStringGson;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;
import static com.dwmedios.uniconekt.view.util.Utils.getDrawable;

public class SearchUniversidadesPresenter {

    private ClientService mClientService;
    private SearchUniversidadViewController mSearchUniversidadController;
    private Context mContext;
    private AllController mAllController;

    public SearchUniversidadesPresenter(Context mContext, SearchUniversidadViewController mSearchUniversidadController) {
        this.mSearchUniversidadController = mSearchUniversidadController;
        if (mClientService == null) mClientService = new ClientService(mContext);
        this.mContext = mContext;
        mAllController = new AllController(mContext);
    }


    @SuppressLint("ResourceType")
    public void ConfigureMenu() {
        mSearchUniversidadController.OnLoading(true);
        List<ClasViewModel.menu> lisMen = new ArrayList<>();

        ClasViewModel.menu menu1 = new ClasViewModel.menu();
        menu1.nombre = "Programas académicos.";
        menu1.tipo = ClasViewModel.tipoMenu.academicos;
        // menu1.drawableImage = getDrawable("ic_programas_academicos", mContext);
        menu1.drawableImage = R.drawable.ic_programas_academicos;
        menu1.color = mContext.getString(R.color.Color_programas);
        lisMen.add(menu1);

        ClasViewModel.menu menu4 = new ClasViewModel.menu();
        menu4.nombre = "Ubicación";
        menu4.tipo = ClasViewModel.tipoMenu.buscar_pais;
        //menu4.drawableImage = getDrawable("aeroplane", mContext);
        menu4.drawableImage = R.drawable.aeroplane;
        menu4.color = mContext.getString(R.color.Color_euu);
        lisMen.add(menu4);


        ClasViewModel.menu menu3 = new ClasViewModel.menu();
        menu3.nombre = "Geolocalización";
        menu3.tipo = ClasViewModel.tipoMenu.cercaMi;
        // menu3.drawableImage = getDrawable("ic_cerca_mi", mContext);
        menu3.drawableImage = R.drawable.ic_cerca_mi;
        menu3.color = mContext.getString(R.color.Color_location);
        lisMen.add(menu3);

        ClasViewModel.menu menu = new ClasViewModel.menu();
        menu.nombre = "Universidad";
        menu.tipo = ClasViewModel.tipoMenu.nombre;
        //menu.drawableImage = getDrawable("ic_universidad", mContext);
        menu.drawableImage = R.drawable.ic_universidad;
        menu.color = mContext.getString(R.color.colorPrimaryDark);
        lisMen.add(menu);


        ClasViewModel.menu menu5 = new ClasViewModel.menu();
        menu5.nombre = "Favoritos";
        menu5.tipo = ClasViewModel.tipoMenu.favoritos;
        //menu5.drawableImage = getDrawable("star_menu_final", mContext);
        menu5.drawableImage = R.drawable.star;
        menu5.color = mContext.getString(R.color.Color_favoritos);

        lisMen.add(menu5);
        if (lisMen != null && !lisMen.isEmpty()) {
            mSearchUniversidadController.OnSuccesMenu(lisMen);
        } else {
            mSearchUniversidadController.Onfailed("No se pudo cargar el menu");
            mSearchUniversidadController.EmpyRecycler();
        }

        mSearchUniversidadController.OnLoading(false);
    }

    private Persona getPersona() {
        List<Persona> personasList = mAllController.getPersona();
        return personasList.get(0);

    }

    public void getBanners() {
        // mSearchUniversidadController.OnLoading(true);
        mClientService
                .getAPI()
                .getBannersVigentes()
                .enqueue(new Callback<BannersResponse>() {
                    @Override
                    public void onResponse(Call<BannersResponse> call, Response<BannersResponse> response) {
                        BannersResponse res = response.body();
                        // mSearchUniversidadController.OnLoading(false);
                        if (res != null) {

                            if (res.status == 1) {
                                mSearchUniversidadController.OnsuccesBanners(res.mBannersList);
                            } else {

                                mSearchUniversidadController.OnfailedBanners(res.mensaje);
                            }
                        } else {

                            mSearchUniversidadController.OnfailedBanners(ERROR_CONECTION);
                        }
                    }

                    @Override
                    public void onFailure(Call<BannersResponse> call, Throwable t) {
                        mSearchUniversidadController.OnLoading(false);
                        mSearchUniversidadController.OnfailedBanners(ERROR_CONECTION);
                    }
                });
    }

    public void RegistrarVisitaBanner(Banners mBanners) {
        String json = ConvertModelToStringGson(mBanners);
        if (json != null) {
            mClientService
                    .getAPI()
                    .RegistrarVisitaBanner(json)
                    .enqueue(new Callback<BannersResponse>() {
                        @Override
                        public void onResponse(Call<BannersResponse> call, Response<BannersResponse> response) {
                            BannersResponse res = response.body();
                            //no esperar respuesta del ws
                        }

                        @Override
                        public void onFailure(Call<BannersResponse> call, Throwable t) {

                        }
                    });
        }
    }

    public void RegistrarVisita(final Banners mBanners) {
        VisitasBanners object = new VisitasBanners();
        object.idBanner = mBanners.id;
        object.idPersona = getPersona().id;

        mSearchUniversidadController.OnLoading(true);
        String json = ConvertModelToStringGson(object);
        if (json != null) {
            mClientService
                    .getAPI()
                    .RegisterVisitedBanner(json)
                    .enqueue(new Callback<VisitasBannersResponse>() {
                        @Override
                        public void onResponse(Call<VisitasBannersResponse> call, Response<VisitasBannersResponse> response) {
                            VisitasBannersResponse res = response.body();
                            mSearchUniversidadController.OnLoading(false);
                            if (res != null) {

                                if (res.status == 1) {
                                    mSearchUniversidadController.SuccesVisitedBanner(res.mVisitasBanners);
                                    mSearchUniversidadController.ViewBanner(mBanners);
                                } else {

                                    mSearchUniversidadController.FailedVisitedBanner(res.mensaje);
                                }
                            } else {

                                mSearchUniversidadController.FailedVisitedBanner(ERROR_CONECTION);
                            }
                        }

                        @Override
                        public void onFailure(Call<VisitasBannersResponse> call, Throwable t) {
                            mSearchUniversidadController.OnLoading(false);
                            mSearchUniversidadController.FailedVisitedBanner(ERROR_CONECTION);
                        }
                    });
        } else {
            mSearchUniversidadController.OnLoading(true);
            mSearchUniversidadController.FailedVisitedBanner(ERROR_CONECTION);
        }

    }
}
