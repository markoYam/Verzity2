package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.DireccionOrmlite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Item;

import java.util.List;

public class DireccionController {
    private Context mContext;
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private DireccionOrmlite mDireccionOrmlite;

    public DireccionController(Context mContext) {
        this.mContext = mContext;
        this.mOrmLiteDatabaseHelper= new OrmLiteDatabaseHelper(this.mContext);
        this.mDireccionOrmlite= new DireccionOrmlite(mOrmLiteDatabaseHelper);
    }
    public List<Direccion> getItem(){
        return mDireccionOrmlite.selectAll();
    }

    public boolean addIem(Direccion mItem){
        return mDireccionOrmlite.addElement(mItem) > 0;
    }
}
