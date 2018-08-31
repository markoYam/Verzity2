package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Universidad;

/**
 * Created by mYam on 18/04/2018.
 */

public class UniversidadOrmlite extends OrmLiteBaseRepository<Universidad> {

    public UniversidadOrmlite(OrmLiteDatabaseHelper helper) {
        super(helper, Universidad.class);
    }
}
