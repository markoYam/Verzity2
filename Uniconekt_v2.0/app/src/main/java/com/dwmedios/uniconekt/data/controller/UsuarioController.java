package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.UsuarioOrmLite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.Usuario;

import java.util.List;

public class UsuarioController {
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private Context mContext;
    private UsuarioOrmLite mUsuarioOrmLite;

    public UsuarioController(Context mContext) {
        this.mContext = mContext;
        mOrmLiteDatabaseHelper = new OrmLiteDatabaseHelper(this.mContext);
        mUsuarioOrmLite = new UsuarioOrmLite(mOrmLiteDatabaseHelper);
    }

    public List<Usuario> getItem(){
        return mUsuarioOrmLite.selectAll();
    }

    public boolean addIem(Usuario mItem){
        return mUsuarioOrmLite.addElement(mItem) > 0;
    }
}
