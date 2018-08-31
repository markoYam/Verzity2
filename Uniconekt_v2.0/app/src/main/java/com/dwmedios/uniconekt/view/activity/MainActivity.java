package com.dwmedios.uniconekt.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.TipoCategoria;
import com.dwmedios.uniconekt.presenter.MainPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.TipoCategoriaAdapter;
import com.dwmedios.uniconekt.view.viewmodel.MainViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainViewController {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
    @BindView(R.id.recyclerview_informacion) RecyclerView mRecyclerView;
    @BindView(R.id.textView) TextView mTextViewEmpty;
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mMainPresenter = new MainPresenter(this,MainActivity.this);
        mMainPresenter.getTiposCategoriasWS();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Item mItem = new Item();
                mItem.nombre = "Luis Cabañas";
                mMainPresenter.addItem(mItem);
            }
        });
    }

    public void configureRecyclerView(List<TipoCategoria> mTipoCategoriaList) {
        if (mTipoCategoriaList != null && mTipoCategoriaList.size() > 0) {
            TipoCategoriaAdapter mTipoCategoriaAdapter = new TipoCategoriaAdapter(mTipoCategoriaList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mTipoCategoriaAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.emptyTiposCategoriasWS();
        }
    }

    //Imlementacion
    @Override
    public void successTiposCategoriasWS(List<TipoCategoria> data) {
        mTextViewEmpty.setVisibility(View.GONE);
        configureRecyclerView(data);
    }

    @Override
    public void emptyTiposCategoriasWS() {
        mTextViewEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void failureLoadTiposCategoriasWS(String message) {
        mTextViewEmpty.setVisibility(View.VISIBLE);
        showInfoDialog("Información",message,"OK");
    }

    @Override
    public void showProgress(boolean visible) {
        if(visible){
            showOnProgressDialog("Cargando");
        }else{
            dismissProgressDialog();
        }
    }

    @Override
    public void getItems(List<Item> data) {
        for (Item mItem : data) {
            showToastMessage(mItem.nombre);
        }
    }
}
