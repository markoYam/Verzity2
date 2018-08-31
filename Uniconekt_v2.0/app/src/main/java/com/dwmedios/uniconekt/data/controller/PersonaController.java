package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.PersonaOrmlite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.Persona;

import java.util.List;

public class PersonaController {
    private Context mContext;
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private PersonaOrmlite mPersonaOrmlite;

    public PersonaController(Context mContext) {
        this.mContext = mContext;
        this.mOrmLiteDatabaseHelper= new OrmLiteDatabaseHelper(this.mContext);
        this.mPersonaOrmlite= new PersonaOrmlite(mOrmLiteDatabaseHelper);
    }

    public List<Persona> getItem(){
        return mPersonaOrmlite.selectAll();
    }

    public boolean addIem(Persona mItem){
        return mPersonaOrmlite.addElement(mItem) > 0;
    }
}
