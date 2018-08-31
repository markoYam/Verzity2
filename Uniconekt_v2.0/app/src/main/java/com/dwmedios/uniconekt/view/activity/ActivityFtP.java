package com.dwmedios.uniconekt.view.activity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.UtilsFtp.ftpClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityFtP extends BaseActivity {

    @BindView(R.id.button2)
    Button mButton;
    @BindView(R.id.ImageviewDemo)ImageView mImageView;
    private ftpClient mFtpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft_p);
        ButterKnife.bind(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFtpClient = new ftpClient(getApplicationContext(), ActivityFtP.this);
                mFtpClient.pickImage(new ftpClient.patchImageInterface() {
                    @Override
                    public void patch(String patch) {
                        showMessage(patch);
                    }
                }, mImageView);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFtpClient.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mFtpClient.permisonResult(requestCode,permissions,grantResults);
    }
}
