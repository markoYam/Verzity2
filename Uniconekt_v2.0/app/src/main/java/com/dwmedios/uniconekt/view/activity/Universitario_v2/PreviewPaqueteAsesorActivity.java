package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dwmedios.uniconekt.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewPaqueteAsesorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_paquete_asesor);
        ButterKnife.bind(this);
        mToolbar.setTitle("Confirmación de la compra");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
