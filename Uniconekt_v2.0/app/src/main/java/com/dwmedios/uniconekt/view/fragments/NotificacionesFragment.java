package com.dwmedios.uniconekt.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.presenter.NotificacionesPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.DetalleNotificacionActivity;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;
import com.dwmedios.uniconekt.view.adapter.NotificacionAdapter;
import com.dwmedios.uniconekt.view.fragments.base.BaseFragment;
import com.dwmedios.uniconekt.view.viewmodel.NotificacionesViewController;

import org.aviran.cookiebar2.CookieBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificacionesFragment extends BaseFragment implements NotificacionesViewController {

    private NotificacionesPresenter mNotificacionesPresenter;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private NotificacionAdapter mNotificacionAdapter;

    public NotificacionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View master = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        ButterKnife.bind(this, master);
        mNotificacionesPresenter = new NotificacionesPresenter(this, getContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
               // mNotificacionesPresenter.ConsultarNotificaciones();
            }
        });
        return master;
    }

    @Override
    public void onStart() {
        super.onStart();
        //mNotificacionesPresenter.ConsultarNotificaciones();
    }

    public void configureRecyclerView(List<Notificaciones> mNotificaciones) {
        if (mNotificaciones != null && mNotificaciones.size() > 0) {
            mNotificacionAdapter = new NotificacionAdapter(mNotificaciones, mOnclick);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mNotificacionAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            //this.EmpyRecycler();
        }
    }

    NotificacionAdapter.onclick mOnclick = new NotificacionAdapter.onclick() {
        @Override
        public void onclick(Notificaciones mNotificaciones) {
            startActivity(new Intent(getActivity(), DetalleNotificacionActivity.class).putExtra("msg1", mNotificaciones));
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void Onsucces(List<Notificaciones> mNotificacioneslist) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextView.setVisibility(View.GONE);
        configureRecyclerView(mNotificacioneslist);
    }

    @Override
    public void OnFailed(String mensaje) {
        EmptyRecyclerView();
        showMessaje(mensaje);
    }

    @Override
    public void Onloading(boolean loading) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void EmptyRecyclerView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
        mTextView.setVisibility(View.VISIBLE);
    }
}
