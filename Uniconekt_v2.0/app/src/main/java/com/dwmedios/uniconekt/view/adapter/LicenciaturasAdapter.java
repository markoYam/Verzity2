package com.dwmedios.uniconekt.view.adapter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Licenciaturas;
import com.dwmedios.uniconekt.view.adapter.holder.LicenciaturasHolder;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;

public class LicenciaturasAdapter extends RecyclerView.Adapter<LicenciaturasHolder> implements StickyRecyclerHeadersAdapter {
    List<Licenciaturas> mLicenciaturasList;
    public listSelected mListSelected;

    public interface listSelected {
        void Selected(List<Licenciaturas> mLicenciaturasList);
    }

    public LicenciaturasAdapter(List<Licenciaturas> mLicenciaturasList, listSelected mListSelected) {
        this.mLicenciaturasList = mLicenciaturasList;
        this.mListSelected = mListSelected;
    }

    @NonNull
    @Override
    public LicenciaturasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_programas_academicos, parent, false);
        return new LicenciaturasHolder(master);
    }

    @Override
    public void onBindViewHolder(@NonNull final LicenciaturasHolder holder, final int position) {

        //mLicenciaturasList.get(position).ischeCked = holder.mCheckBox.isChecked();
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                  mLicenciaturasList.get(position).ischeCked = b;
                //   if (mListSelected != null)
                //     mListSelected.Selected(mLicenciaturasSelected());

            }
        });
        //
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = holder.mCheckBox.isChecked();

                if (mLicenciaturasList.get(position).id == -1) {
                    for (int i = 0; i < mLicenciaturasList.size(); i++) {
                        if (mLicenciaturasList.get(i).mNivelEstudios.id == mLicenciaturasList.get(position).mNivelEstudios.id)
                            mLicenciaturasList.get(i).ischeCked = checked;
                    }
                } else {
                    int count = 0, countCheked = 0;
                    for (int i = 0; i < mLicenciaturasList.size(); i++) {
                        if (mLicenciaturasList.get(i).mNivelEstudios.id == mLicenciaturasList.get(position).mNivelEstudios.id) {
                            if (mLicenciaturasList.get(i).ischeCked && mLicenciaturasList.get(i).id!=-1) {
                                countCheked++;
                            }
                            count++;
                        }
                    }
                    if ((count-1) == countCheked) {
                        for (int i = 0; i < mLicenciaturasList.size(); i++) {
                            if (mLicenciaturasList.get(i).mNivelEstudios.id == mLicenciaturasList.get(position).mNivelEstudios.id) {
                                if (mLicenciaturasList.get(i).id == -1) {
                                    mLicenciaturasList.get(i).ischeCked = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < mLicenciaturasList.size(); i++) {
                            if (mLicenciaturasList.get(i).mNivelEstudios.id == mLicenciaturasList.get(position).mNivelEstudios.id) {
                                if (mLicenciaturasList.get(i).id == -1) {
                                    mLicenciaturasList.get(i).ischeCked = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                }, 50);
                if (mListSelected != null)
                    mListSelected.Selected(mLicenciaturasSelected());
            }
        });
        holder.configureLiecnciaturas(mLicenciaturasList.get(position));

    }

    public List<Licenciaturas> mLicenciaturasSelected() {
        List<Licenciaturas> temp = new ArrayList<>();
        for (Licenciaturas licenciaturas : mLicenciaturasList) {
            if (licenciaturas.ischeCked && licenciaturas.id!=-1) {
                temp.add(licenciaturas);
            }
        }
        return temp;
    }

    @Override
    public long getHeaderId(int position) {
        return mLicenciaturasList.get(position).mNivelEstudios.nombre.charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View master2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.preguntas_header_row, parent, false);
        LicenciaturasHolder mLicenciaturasHolder = new LicenciaturasHolder(master2, false);
        return mLicenciaturasHolder;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView itemHolder = (TextView) holder.itemView;
        itemHolder.setText(mLicenciaturasList.get(position).mNivelEstudios.nombre);
    }

    @Override
    public int getItemCount() {
        return (mLicenciaturasList != null && mLicenciaturasList.size() > 0 ? mLicenciaturasList.size() : 0);
    }
}
