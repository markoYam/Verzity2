package com.dwmedios.uniconekt.view.activity.Universitario;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.model.CuponesCanjeados;
import com.dwmedios.uniconekt.model.FotosCupones;
import com.dwmedios.uniconekt.presenter.DetalleCuponPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.ViewPagerUtils;
import com.dwmedios.uniconekt.view.viewmodel.DetalleCuponViewCotroller;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;
import static com.dwmedios.uniconekt.view.util.Utils.configurefecha;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class DetalleCuponActivity extends BaseActivity implements DetalleCuponViewCotroller {
    public static final String KEY_DETALLE_CUPON = "key detalle cupon";
    @BindView(R.id.viewPagerFotosCupon)
    ViewPager mViewPager;
    @BindView(R.id.textViewNombreCuponD)
    TextView mTextViewNombre;
    @BindView(R.id.textViewDescripcionCuponD)
    TextView mTextViewDescripcion;
    @BindView(R.id.textVigenciaCuponD)
    TextView mTextViewVigencia;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapseTolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.floatingCupponDetalle)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.cardContentQr)
    CardView mLinearLayoutContent;
    @BindView(R.id.imageviewQR)
    ImageView mImageViewQR;
    @BindView(R.id.textViewClaveQr)
    TextView mTextViewClave;
    private ViewPagerUtils mViewPagerUtils;
    @BindView(R.id.defaultImage)
    ImageView mImageView;
    private Cupones mCupones;
    private DetalleCuponPresenter mDetalleCuponPresenter;
    private boolean isCanjeado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cupon);
        supportPostponeEnterTransition();
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Cambiar el color del toolbar y status bar
        setStatusBarGradiant(this, R.drawable.status_cupon);
        mCupones = getIntent().getExtras().getParcelable(KEY_DETALLE_CUPON);
        mDetalleCuponPresenter = new DetalleCuponPresenter(this, this);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        configureViewPager();
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mCupones.nombre != null) {
                        mCollapsingToolbarLayout.setTitle(mCupones.nombre);
                    }
                    mFloatingActionButton.hide();
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                    if (!isCanjeado)
                        mFloatingActionButton.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void configureViewPager() {
        if (mCupones != null) {
            mTextViewNombre.setText(mCupones.nombre);
            mTextViewDescripcion.setText(mCupones.descripcion);
            mTextViewVigencia.setText("Validez: " + configurefecha(mCupones.inicio) + " al " + configurefecha(mCupones.fin));
            if (mCupones.mFotosCuponesList != null && mCupones.mFotosCuponesList.size() > 0) {
                mImageView.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mViewPager.setTransitionName(CuponesViewActivity.KEY_TRANSITION_CUPON1);
                    supportStartPostponedEnterTransition();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mImageView.setTransitionName(CuponesViewActivity.KEY_TRANSITION_CUPON1);
                    supportStartPostponedEnterTransition();
                }
                mImageView.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
            }
            mViewPagerUtils = new ViewPagerUtils(mCupones.mFotosCuponesList, getSupportFragmentManager(), DetalleCuponActivity.this);
            mViewPagerUtils.setLayoutInflater(R.layout.item_viewpagerutils);
            mViewPagerUtils.setmHandlerView(new ViewPagerUtils.HandlerView() {
                @Override
                public View handler(View mView, List<?> mObjects, int position, ViewGroup container) {
                    ImageView mImageview = mView.findViewById(R.id.imageviewFotoUtil);
                    List<FotosCupones> list = (List<FotosCupones>) mObjects;
                    ImageLoader.getInstance().displayImage(getUrlImage(list.get(position).ruta, DetalleCuponActivity.this), mImageview, OptionsImageLoaderItems);
                    return mView;
                }
            });
            mViewPagerUtils.build(mViewPager);
            mViewPagerUtils.setAnimate(true);


        }
    }

    @Override
    public void onBackPressed() {
        mViewPagerUtils.finishAnim();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mViewPagerUtils.finishAnim();
        super.onDestroy();
    }

    @Override
    public void OnSuccess(CuponesCanjeados mCuponesCanjeados) {
//    }
        isCanjeado = true;
        LoadQr(mCuponesCanjeados);
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDetalleCuponPresenter.CanjearCuppon(mCupones);
        }
    };

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void LoadQr(CuponesCanjeados mCuponesCanjeados) {


        Bitmap bitmap = null;
        try {
            bitmap = encodeAsBitmap(mCupones.clave);
            mImageViewQR.setImageBitmap(bitmap);
            if (bitmap != null) {
                mAppBarLayout.setExpanded(false, true);
                mLinearLayoutContent.setVisibility(View.VISIBLE);
                mImageViewQR.setImageBitmap(bitmap);
                mTextViewClave.setText(mCupones.clave);
            } else {
                Toast.makeText(DetalleCuponActivity.this, ERROR_CONECTION,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 500, 500, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }
}
