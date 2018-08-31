package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Direccion;

/**
 * Created by mYam on 18/04/2018.
 */

public class DireccionOrmlite extends OrmLiteBaseRepository<Direccion> {
    public DireccionOrmlite(OrmLiteDatabaseHelper helper) {
        super(helper, Direccion.class);
    }
}
