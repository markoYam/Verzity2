package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.VentasPaquetesOrmlite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;

public class VentasPaquetesController {
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private Context mContext;
    private VentasPaquetesOrmlite mVentasPaquetesOrmlite;

    public VentasPaquetesController(Context mContext) {
        this.mContext = mContext;
        mOrmLiteDatabaseHelper = new OrmLiteDatabaseHelper(mContext);
        mVentasPaquetesOrmlite = new VentasPaquetesOrmlite(mOrmLiteDatabaseHelper);
    }
}
