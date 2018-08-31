package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.UniversidadOrmlite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.Universidad;

import java.util.List;

public class UniversidadController {

    private Context mContext;
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private UniversidadOrmlite mUniversidadOrmlite;

    public UniversidadController(Context mContext) {
        this.mContext = mContext;
        this.mOrmLiteDatabaseHelper= new OrmLiteDatabaseHelper(this.mContext);
        this.mUniversidadOrmlite= new UniversidadOrmlite(mOrmLiteDatabaseHelper);
    }

    public List<Universidad> getItem(){
        return mUniversidadOrmlite.selectAll();
    }

    public boolean addIem(Universidad mItem){
        return mUniversidadOrmlite.addElement(mItem) > 0;
    }
}
