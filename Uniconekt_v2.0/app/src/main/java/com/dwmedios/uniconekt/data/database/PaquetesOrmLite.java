package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Paquetes;

public class PaquetesOrmLite extends OrmLiteBaseRepository<Paquetes> {
    public PaquetesOrmLite(OrmLiteDatabaseHelper helper) {
        super(helper, Paquetes.class);
    }
}
