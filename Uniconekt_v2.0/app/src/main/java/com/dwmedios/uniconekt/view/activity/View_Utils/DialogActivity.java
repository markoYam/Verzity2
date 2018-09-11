package com.dwmedios.uniconekt.view.activity.View_Utils;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogActivity extends AppCompatActivity {
    public static final String KEY_DIALOG = "KEYSDSDSD";
    @BindView(R.id.contenedorVista)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.buttonOk)
    Button mButtonOk;
    @BindView(R.id.buttonCancel)
    Button mButtonCancelar;
    @BindView(R.id.textViewTitulo)
    TextView mTextViewTitulo;
    @BindView(R.id.textViewDescripcion)
    TextView mTextViewDescripcion;
    @BindView(R.id.imageViewFoto)
    ImageView imageView;
    private handleDialog mHandleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        mHandleDialog = getIntent().getExtras().getParcelable(KEY_DIALOG);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(20, 0, 20, 0);

            mRelativeLayout.setLayoutParams(layoutParams);
        }
        mButtonCancelar.setOnClickListener(mOnClickListener);
        mButtonOk.setOnClickListener(mOnClickListener);
        if (mHandleDialog != null) {
            mTextViewTitulo.setText(mHandleDialog.titulo);
            mTextViewDescripcion.setText(mHandleDialog.contenido);
            if (mHandleDialog.logo != 0) {
                imageView.setImageResource(mHandleDialog.logo);
            }
            if (mHandleDialog.touchOutSide) {
                setFinishOnTouchOutside(true);
            } else {
                setFinishOnTouchOutside(false);
            }
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonCancel:
                    Intent mIntent = getIntent();
                    setResult(RESULT_CANCELED, mIntent);
                    finish();
                case R.id.buttonOk:
                    Intent mIntent2 = getIntent();
                    setResult(RESULT_OK, mIntent2);
                    finish();
            }
        }
    };

    public static class handleDialog implements Parcelable {
        public int logo;
        public String titulo;
        public String contenido;
        public boolean touchOutSide;


        public handleDialog(Parcel in) {
            logo = in.readInt();
            titulo = in.readString();
            contenido = in.readString();
            touchOutSide = in.readByte() != 0;
        }

        public static final Creator<handleDialog> CREATOR = new Creator<handleDialog>() {
            @Override
            public handleDialog createFromParcel(Parcel in) {
                return new handleDialog(in);
            }

            @Override
            public handleDialog[] newArray(int size) {
                return new handleDialog[size];
            }
        };

        public handleDialog() {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(logo);
            dest.writeString(titulo);
            dest.writeString(contenido);
            dest.writeByte((byte) (touchOutSide ? 1 : 0));
        }
    }

}
