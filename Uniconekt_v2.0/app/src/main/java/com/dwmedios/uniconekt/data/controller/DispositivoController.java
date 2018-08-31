package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.DispositivoOrmlite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Item;

import java.util.List;

public class DispositivoController {

    private Context mContext;
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private DispositivoOrmlite mDispositivoOrmlite;

    public DispositivoController(Context mContext) {
        this.mContext = mContext;
        this.mOrmLiteDatabaseHelper= new OrmLiteDatabaseHelper(mContext);
        this.mDispositivoOrmlite= new DispositivoOrmlite(this.mOrmLiteDatabaseHelper);
    }



    public List<Dispositivo> getItem(){
        return mDispositivoOrmlite.selectAll();
    }

    public boolean addIem(Dispositivo mItem){
        return mDispositivoOrmlite.addElement(mItem) > 0;
    }
}
