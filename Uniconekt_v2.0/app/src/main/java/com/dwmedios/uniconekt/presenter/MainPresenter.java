package com.dwmedios.uniconekt.presenter;

import android.content.Context;


import com.dwmedios.uniconekt.data.controller.ItemController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.data.service.response.TipoCategoriaResponse;
import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.TipoCategoria;
import com.dwmedios.uniconekt.view.viewmodel.MainViewController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class MainPresenter {
    private MainViewController mMainViewController;
    private ClientService mClientService;
    private ItemController mItemController;

    public MainPresenter(MainViewController mMainViewController, Context mContext) {
        this.mMainViewController = mMainViewController;

        if (this.mClientService == null) {
            this.mClientService = new ClientService(mContext);
        }

        if(this.mItemController == null){
            this.mItemController = new ItemController(mContext);
        }
    }

    public void getTiposCategoriasWS(){
        mMainViewController.showProgress(true);
        mClientService
                .getAPI()
                .getTiposCategorias()
                .enqueue(new Callback<TipoCategoriaResponse>() {
                    @Override
                    public void onResponse(Call<TipoCategoriaResponse> call, Response<TipoCategoriaResponse> response) {
                        TipoCategoriaResponse mResponse = response.body();
                        if(mResponse != null){
                            if(mResponse.status == 1){
                                List<TipoCategoria> data = mResponse.data;
                                mMainViewController.successTiposCategoriasWS(data);
                            }else{
                                mMainViewController.failureLoadTiposCategoriasWS(mResponse.mensaje);
                            }
                        }else{
                            mMainViewController.failureLoadTiposCategoriasWS("Ocurrio un error en la conexión");
                        }
                    }

                    @Override
                    public void onFailure(Call<TipoCategoriaResponse> call, Throwable t) {
                        mMainViewController.failureLoadTiposCategoriasWS("Ocurrio un error en la conexión");
                    }
                });
        mMainViewController.showProgress(false);
    }

    public void addItem(Item mItem){
        if(mItemController.addIem(mItem)){
            this.mMainViewController.getItems(mItemController.getItem());
        }
    }
}
