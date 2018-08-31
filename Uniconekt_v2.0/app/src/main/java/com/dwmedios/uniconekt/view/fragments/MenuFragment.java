package com.dwmedios.uniconekt.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.MenuPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.DatosUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universidad.PaquetesActivity;
import com.dwmedios.uniconekt.view.activity.Universidad.PostuladosActivity;
import com.dwmedios.uniconekt.view.activity.Universidad.SearchUbicacionUniActivity;
import com.dwmedios.uniconekt.view.activity.Universidad_v2.NotificacionesUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.CuponesViewActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.SearchUniversidadActivity;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;
import com.dwmedios.uniconekt.view.fragments.base.BaseFragment;
import com.dwmedios.uniconekt.view.util.Dialog.CustomDialogReyclerView;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.MenuController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.RelativeLayout.CENTER_VERTICAL;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.KEY_BECAS_COLOR;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.TYPE_VIEW;


public class MenuFragment extends BaseFragment implements MenuController {

    public MenuFragment() {
    }

    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpy;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.conteendorMenu)
    LinearLayout mLinearLayout;
    @BindView(R.id.cardviewCuponMenu)
    CardView mCardViewCupon;
    @BindView(R.id.cardviewFinanciamientoMenu)
    CardView mCardViewFinanciamiento;
    private MenuPresenter menuPresenter;
    private MenuAdapter menuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View master = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, master);
        mSwipeRefreshLayout.setBackgroundColor(Color.parseColor("#000000"));
        menuPresenter = new MenuPresenter(this, getActivity());
        menuPresenter.ConfigureMenu();
        mCardViewCupon.setOnClickListener(mOnClickListener);
        mCardViewFinanciamiento.setOnClickListener(mOnClickListener);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                menuPresenter.ConfigureMenu();
            }
        });

        return master;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cardviewCuponMenu:
                    startActivity(new Intent(getActivity(), CuponesViewActivity.class));
                    break;
                case R.id.cardviewFinanciamientoMenu:
                    FinanciamientoActivity.TYPE_VIEW_F = 1;
                    startActivity(new Intent(getActivity(), FinanciamientoActivity.class));
                    break;
            }
        }
    };

    public void configureRecyclerView(List<ClasViewModel.menu> menus) {
        if (menus != null && menus.size() > 0) {

            int typeUser = SharePrefManager.getInstance(getContext()).getTypeUser();
            if (typeUser == 2) {
                mLinearLayout.setVisibility(View.GONE);
                menuAdapter = new MenuAdapter(menus, mOnclick, 2);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);
            } else if (typeUser == 1) {

                menuAdapter = new MenuAdapter(menus, mOnclick, 1);
                GridLayoutManager nGridLayoutManager = new GridLayoutManager(getContext(), 2);
                mRecyclerView.setLayoutManager(nGridLayoutManager);
                RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mRecyclerView.setLayoutParams(mLayoutParams);

            }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(menuAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.empyRecycler();
        }
    }

    MenuAdapter.onclick mOnclick = new MenuAdapter.onclick() {
        @Override
        public void onclick(ClasViewModel.menu menu) {
            ClasViewModel.tipoMenu validate = ClasViewModel.tipoMenu.valueOf(menu.tipo.toString());
            switch (validate) {
                case Universidad:
                    SharePrefManager.getInstance(getContext()).SearchExtranjero(false);
                    startActivity(new Intent(getActivity(), SearchUniversidadActivity.class));
                    break;
                case Becas:
                    TYPE_VIEW = 1;
                    startActivity(new Intent(getActivity(), BecasActivity.class).putExtra(KEY_BECAS_COLOR, menu.color));
                    break;
                case extranjero:
                    SharePrefManager.getInstance(getContext()).SearchExtranjero(true);
                    startActivity(new Intent(getActivity(), SearchUniversidadActivity.class));
                    break;
                case examen:
                    startActivity(new Intent(getActivity(), NotificacionesUniversidadActivity.class));
                    break;
                case paquetes:
                    startActivity(new Intent(getActivity(), PaquetesActivity.class));

                    break;
                case Viaja:
                    // startActivity(new Intent(getActivity(), SearchUbicacionUniActivity.class));

                    break;

                case postulados:
                    startActivity(new Intent(getActivity(), PostuladosActivity.class));
                    break;
                default:
                    showMessaje("En proceso....");
                    break;
            }

        }
    };

    @Override
    public void OnSucces(List<ClasViewModel.menu> menuList) {
        mTextViewEmpy.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(menuList);
    }

    @Override
    public void OnFailed(String mensaje) {
        mSwipeRefreshLayout.setRefreshing(false);
        showMessaje(mensaje);

    }

    @Override
    public void empyRecycler() {
        mTextViewEmpy.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void OnLoading(boolean loading) {
        if (loading) {
            showOnProgressDialog("cargando...");
        } else {

            dismissProgressDialog();
        }
    }

    @Override
    public void OnLoadHeaders(Persona mPersona, Usuario mUsuario) {

    }

    @Override
    public void updateNumMensajes(int count) {

    }

    @Override
    public void configureCabeceras() {

    }

    @Override
    public void succesCerrarSession() {

    }
}
