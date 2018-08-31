package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.view.adapter.holder.MenuHolder;

import java.util.List;
import java.util.zip.Inflater;

public class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {
    public List<ClasViewModel.menu> menu;
    int typeUser;

    public MenuAdapter(List<ClasViewModel.menu> menu, onclick mOnclick, int typeUser) {
        this.menu = menu;
        this.mOnclick = mOnclick;
        this.typeUser = typeUser;
    }

    public interface onclick {
        void onclick(ClasViewModel.menu menu);
    }

    public onclick mOnclick;

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = null;
        if (typeUser == 2)
            master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_menu, parent, false);
        if (typeUser == 1)
            master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_menu_universitario, parent, false);
        MenuHolder view = new MenuHolder(master);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        ClasViewModel.menu men = menu.get(position);
        holder.ConfigureMenu(men, mOnclick,typeUser);
    }

    @Override
    public int getItemCount() {
        return (menu != null && !menu.isEmpty() ? menu.size() : 0);
    }
}
