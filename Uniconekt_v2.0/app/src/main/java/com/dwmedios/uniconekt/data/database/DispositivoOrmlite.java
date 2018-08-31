package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Dispositivo;

/**
 * Created by mYam on 18/04/2018.
 */

public class DispositivoOrmlite extends OrmLiteBaseRepository<Dispositivo> {
    public DispositivoOrmlite(OrmLiteDatabaseHelper helper) {
        super(helper, Dispositivo.class);
    }
}
