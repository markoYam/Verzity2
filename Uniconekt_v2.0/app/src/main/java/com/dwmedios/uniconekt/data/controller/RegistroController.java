package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.UsuarioOrmLite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Usuario;

import java.util.List;

/**
 * Created by mYam on 17/04/2018.
 */

public class RegistroController {
    private Context mContext;
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private UsuarioOrmLite mUsuarioOrmLite;


    public RegistroController(Context mContext) {
        this.mContext = mContext;
        mOrmLiteDatabaseHelper = new OrmLiteDatabaseHelper(this.mContext);
        mUsuarioOrmLite = new UsuarioOrmLite(mOrmLiteDatabaseHelper);
    }

    public List<Usuario> getUser(){
        return mUsuarioOrmLite.selectAll();
    }

    public boolean addUsuario(Usuario mItem){
        return mUsuarioOrmLite.addElement(mItem) > 0;
    }
}
