package com.dwmedios.uniconekt.view.activity.View_Utils;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ImageModel;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewImageActivity extends BaseActivity {
    public static final String KEY_VIEW = "KEY_VIEW8921358425";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.imageViewFoto)
    ImageView mImageView;
    private ImageModel modelImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorTrasparente));
        mToolbar.setTitle("Seleccionar fotografía");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configureLoad();
    }

    private void configureLoad() {
        try {
            modelImage = getIntent().getExtras().getParcelable(KEY_VIEW);
            if (modelImage != null) {
                if (modelImage.mBitmap != null) {
                    mImageView.setImageBitmap(modelImage.mBitmap);
                }else
                {
                    Bitmap mBitmap= SharePrefManager.getInstance(getApplicationContext()).getImageTemp();
                    mImageView.setImageBitmap(mBitmap);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
