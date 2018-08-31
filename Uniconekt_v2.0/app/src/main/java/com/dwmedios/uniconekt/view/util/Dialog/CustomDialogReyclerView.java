package com.dwmedios.uniconekt.view.util.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomDialogReyclerView {
    private Context mContext;
    private Activity mActivity;
    private int mLayout;
    private Dialog mDialog;
    @BindView(R.id.buttonCancel)
    Button mButtonCancel;
    @BindView(R.id.buttonOk)
    Button mButtonOk;
    @BindView(R.id.ContenedorBotones)
    LinearLayout mLinearLayoutButtons;
    @BindView(R.id.textViewTitulo)
    TextView mTextViewTitulo;
    @BindView(R.id.textViewDescripcion)
    TextView mTextViewDescripcion;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpty;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String titulo;
    private String descripcion;
    private View master;
    private handleView mHandleView;
    private List<?> mList;
    private handleOnclick mHandleOnclick;

    public CustomDialogReyclerView(Context mContext, Activity mActivity, int mLayout) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mLayout = mLayout;
        master = View.inflate(mContext, mLayout, null);
        ButterKnife.bind(this, master);
    }

    public void setmHandleView(handleView mHandleView, List<?> mList) {
        this.mHandleView = mHandleView;
        this.mList = mList;
    }

    public void setmHandleOnclick(handleOnclick mHandleOnclick) {
        this.mHandleOnclick = mHandleOnclick;
    }

    public void setTitulo(String mTitulo) {
        this.titulo = mTitulo;
        mTextViewTitulo.setText(mTitulo);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        mTextViewDescripcion.setText(descripcion);
    }

    public void showButtonOkButtonCancel() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(master);
        changeVisivity();
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Ok", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.x = -100;
        mDialog.getWindow().setAttributes(params);
        mDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mDialog.show();

    }

    public void showButtonOk() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(master);
        changeVisivity();
        mButtonCancel.setVisibility(View.GONE);
        mButtonOk.setVisibility(View.VISIBLE);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.x = -100;
        mDialog.getWindow().setAttributes(params);
        mDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mDialog.show();

    }

    public void showDialogRecyclerView() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
        mDialog.setContentView(master);
        changeVisivity();
        mButtonCancel.setVisibility(View.VISIBLE);
        mButtonOk.setVisibility(View.VISIBLE);
        mLinearLayoutButtons.setVisibility(View.GONE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                configureRecyclerView();
            }
        });
        configureRecyclerView();
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        //params.x = -100;
        mDialog.getWindow().setAttributes(params);
        mDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mDialog.show();
    }

    public void dismisDialog() {
        if (mDialog != null) mDialog.dismiss();
    }

    private void changeVisivity() {
        if (titulo == null || titulo.isEmpty()) mTextViewTitulo.setVisibility(View.GONE);
        if (descripcion == null || descripcion.isEmpty())
            mTextViewDescripcion.setVisibility(View.GONE);
    }

    public void configureRecyclerView() {
        if (mList != null && mList.size() > 0) {
            mTextViewEmpty.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            customAdapter menuAdapter = new customAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(menuAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.empyRecycler();
        }
    }

    public void empyRecycler() {
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public class holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tituloRow)
        TextView mTextView;
        @BindView(R.id.cardRow)
        CardView mCardView;

        public holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void configureView(handleView mHandleView, final Object m, final handleOnclick mHandleOnclick) {
            if (mHandleView != null) mHandleView.handle(mTextView, m);
            if (mHandleOnclick != null) mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandleOnclick.onclick(m);
                }
            });
        }

    }

    private class customAdapter extends RecyclerView.Adapter<holder> {

        @Override
        public holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View master2 = LayoutInflater.from(mContext).inflate(R.layout.row_custom_view, parent, false);
            return new holder(master2);
        }

        @Override
        public void onBindViewHolder(holder holder, int position) {
            holder.configureView(mHandleView, mList.get(position), mHandleOnclick);
        }

        @Override
        public int getItemCount() {
            return (mList != null && mList.size() > 0 ? mList.size() : 0);
        }
    }

    /**
     * interfaz de manejo de la vista....
     */
    public interface handleView {
        void handle(TextView mTextView, Object m);
    }

    /**
     * interfza del retorno de datos
     */
    public interface handleOnclick {
        void onclick(Object m);
    }
}
