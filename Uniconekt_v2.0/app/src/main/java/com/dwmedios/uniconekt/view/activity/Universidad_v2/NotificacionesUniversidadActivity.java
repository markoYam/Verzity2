package com.dwmedios.uniconekt.view.activity.Universidad_v2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.UniconektApplication;
import com.dwmedios.uniconekt.model.NotificacionUniversidad;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.presenter.NotificacionesUniPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.DetalleNotificacionActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.NotificacionAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.NotificacionesUniversidadViewController;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesUniversidadActivity extends BaseActivity implements NotificacionesUniversidadViewController {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public NotificacionesUniPresenter mNotificacionesUniPresenter;
    private TabLayout mTabLayout;
    private static int postionLoad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones_universidad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notificaciones");
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mNotificacionesUniPresenter = new NotificacionesUniPresenter(getApplicationContext(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.current_item = 0;
        Utils.filtro_financiamientos = 1;
        Utils.filtro_becas = 1;
        Utils.filtro_licenciaturas = 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.current_item = mViewPager.getCurrentItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void Onsucces(List<NotificacionUniversidad> mNotificacionUniversidads) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mNotificacionUniversidads);
        mViewPager.setAdapter(null);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(Utils.current_item);
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Utils.current_item = position;
            Log.e("view pager", position + "");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void Onloading(boolean isLoading) {
        if (isLoading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void onStart() {
        super.onStart();
        mNotificacionesUniPresenter.mNotificacionesUniPresenter();
    }

    public void cargarNot() {
        Utils.current_item = mViewPager.getCurrentItem();
        mNotificacionesUniPresenter.mNotificacionesUniPresenter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static class PlaceholderFragment extends Fragment {
        private int position;
        private static final String ARG_SECTION_NUMBER = "section_number";
        public NotificacionUniversidad mNotificacionUniversidads;
        public List<Notificaciones> listVisto;
        public List<Notificaciones> lisPendientes;
        //@BindView(R.id.recyclerview_utils)
        RecyclerView mRecyclerView;
        //@BindView(R.id.textView_empyRecycler)
        TextView mTextView;
        SwipeRefreshLayout mSwipeRefreshLayout;
        Context mContext;
        //ImageButton mButtonTodos;
        ImageButton mButtonVisto;
        ImageButton mButtonPendientes;
        TextView mTextViewTodos;
        TextView mTextViewVisto;
        TextView mTextViewPendientes;
        LinearLayout layoutTodos;
        LinearLayout layoutVistos;
        LinearLayout layoutPendietentes;
        private NotificacionAdapter mNotificacionAdapter;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(NotificacionUniversidad mNotificacionUniversidads, int position, Context mContext) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.mNotificacionUniversidads = mNotificacionUniversidads;
            fragment.position = position;
            fragment.mContext = mContext;
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notificaciones_universidad, container, false);
            //ButterKnife.bind(getContext(), rootView);
            mRecyclerView = rootView.findViewById(R.id.recyclerview_utils);
            mTextView = rootView.findViewById(R.id.textView_empyRecycler);
            mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
            //mButtonTodos = rootView.findViewById(R.id.buttonTodos);
            mButtonVisto = rootView.findViewById(R.id.buttonVisto);
            mButtonPendientes = rootView.findViewById(R.id.buttonPendiente);
            mTextViewTodos = rootView.findViewById(R.id.textViewTodos);
            mTextViewVisto = rootView.findViewById(R.id.textViewVisto);
            mTextViewPendientes = rootView.findViewById(R.id.textviewPenditente);

            layoutTodos = rootView.findViewById(R.id.layoutTodos);
            layoutVistos = rootView.findViewById(R.id.LayoutVisto);
            layoutPendietentes = rootView.findViewById(R.id.LayoutPenditentes);

            configureRecyclerView(mNotificacionUniversidads.mNotificacionesList);

            layoutVistos.setOnClickListener(mOnClickListener);
            layoutTodos.setOnClickListener(mOnClickListener);
            layoutPendietentes.setOnClickListener(mOnClickListener);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    try {
                        final Activity finalCurrentActivity = ((UniconektApplication) mContext).getCurrentActivity();
                        Utils.current_item = position;
                        if (finalCurrentActivity instanceof NotificacionesUniversidadActivity) {
                            if (((NotificacionesUniversidadActivity) finalCurrentActivity).mNotificacionesUniPresenter != null) {
                                ((NotificacionesUniversidadActivity) finalCurrentActivity).cargarNot();
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            switch (mNotificacionUniversidads.nombreSeccion) {
                case "Financiamientos":
                    buscar(Utils.filtro_financiamientos);
                    break;
                case "Becas":
                    buscar(Utils.filtro_becas);
                    break;
                case "Licenciaturas":
                    buscar(Utils.filtro_licenciaturas);
                    break;
            }

        }

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layoutTodos:
                        buscar(1);
                        break;
                    case R.id.LayoutVisto:
                        buscar(2);
                        break;
                    case R.id.LayoutPenditentes:
                        buscar(3);
                        break;
                }
            }
        };

        @SuppressLint("ResourceAsColor")
        public void buscar(int tipo) {

            switch (mNotificacionUniversidads.nombreSeccion) {
                case "Financiamientos":
                    Utils.filtro_financiamientos = tipo;
                    break;
                case "Becas":
                    Utils.filtro_becas = tipo;
                    break;
                case "Licenciaturas":
                    Utils.filtro_licenciaturas = tipo;
                    break;
            }

            switch (tipo) {
                case 1:
                    //color de fondo principal
                    layoutTodos.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    layoutVistos.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    layoutPendietentes.setBackgroundColor(getResources().getColor(R.color.colorBlanco));

                    mButtonVisto.setColorFilter(getResources().getColor(R.color.colorPrimaryDark),
                            PorterDuff.Mode.SRC_ATOP);
                    mButtonPendientes.setColorFilter(getResources().getColor(R.color.colorPrimaryDark),
                            PorterDuff.Mode.SRC_ATOP);

                    //icono
                    mTextViewTodos.setTextColor(getResources().getColor(R.color.colorBlanco));
                    mTextViewVisto.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mTextViewPendientes.setTextColor(getResources().getColor(R.color.colorPrimary));


                    configureRecyclerView(mNotificacionUniversidads.mNotificacionesList);
                    break;
                case 2:

                    layoutVistos.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    layoutTodos.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    layoutPendietentes.setBackgroundColor(getResources().getColor(R.color.colorBlanco));

                    //Color text boton
                    mButtonVisto.setColorFilter(getResources().getColor(R.color.colorBlanco),
                            PorterDuff.Mode.SRC_ATOP);
                    mButtonVisto.setBackgroundColor(getResources().getColor(R.color.colorTrasparente));

                    mButtonPendientes.setColorFilter(getResources().getColor(R.color.colorPrimaryDark),
                            PorterDuff.Mode.SRC_ATOP);

                    //icono
                    mTextViewVisto.setTextColor(getResources().getColor(R.color.colorBlanco));
                    mTextViewTodos.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mTextViewPendientes.setTextColor(getResources().getColor(R.color.colorPrimary));

                    listVisto = new ArrayList<>();
                    for (Notificaciones notificaciones : mNotificacionUniversidads.mNotificacionesList) {
                        if (notificaciones.mNotificacionEstatusList.get(0).mEstatus.estatusNot.equals("VISTO")) {
                            listVisto.add(notificaciones);
                        }
                    }
                    configureRecyclerView(listVisto);
                    break;

                case 3:
                    layoutPendietentes.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    layoutTodos.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    layoutVistos.setBackgroundColor(getResources().getColor(R.color.colorBlanco));

                    //Color text boton
                    mButtonPendientes.setColorFilter(getResources().getColor(R.color.colorBlanco),
                            PorterDuff.Mode.SRC_ATOP);
                    mButtonPendientes.setBackgroundColor(getResources().getColor(R.color.colorTrasparente));
                   /* mButtonTodos.setColorFilter(getResources().getColor(R.color.colorPrimaryDark),
                            PorterDuff.Mode.SRC_ATOP);*/
                    mButtonVisto.setColorFilter(getResources().getColor(R.color.colorPrimaryDark),
                            PorterDuff.Mode.SRC_ATOP);

                    //icono
                    mTextViewPendientes.setTextColor(getResources().getColor(R.color.colorBlanco));
                    mTextViewTodos.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mTextViewVisto.setTextColor(getResources().getColor(R.color.colorPrimary));


                    lisPendientes = new ArrayList<>();
                    for (Notificaciones notificaciones : mNotificacionUniversidads.mNotificacionesList) {
                        if (notificaciones.mNotificacionEstatusList.get(0).mEstatus.estatusNot.equals("PENDIENTE")) {
                            lisPendientes.add(notificaciones);
                        }
                    }
                    configureRecyclerView(lisPendientes);
                    break;

            }
        }

        public void configureRecyclerView(List<Notificaciones> mNotificaciones) {
            if (mNotificaciones != null && mNotificaciones.size() > 0) {
                mTextView.setVisibility(View.GONE);
                mNotificacionAdapter = new NotificacionAdapter(mNotificaciones, mOnclick);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mNotificacionAdapter);
                Utils.setAnimRecyclerView(getActivity(), R.anim.layout_animation, mRecyclerView);

            } else {
                mRecyclerView.setAdapter(null);
                mTextView.setVisibility(View.VISIBLE);
            }
        }

        NotificacionAdapter.onclick mOnclick = new NotificacionAdapter.onclick() {
            @Override
            public void onclick(Notificaciones mNotificaciones) {
                Utils.current_item = position;
                startActivity(new Intent(getActivity(), DetalleNotificacionActivity.class).putExtra("msg1", mNotificaciones));
            }
        };
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        List<NotificacionUniversidad> mNotificacionUniversidads;

        public SectionsPagerAdapter(FragmentManager fm, List<NotificacionUniversidad> mNotificacionUniversidads) {
            super(fm);
            this.mNotificacionUniversidads = mNotificacionUniversidads;
        }


        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(mNotificacionUniversidads.get(position), position, getApplicationContext());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
