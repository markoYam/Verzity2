package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Configuraciones;

public class ConfiguracionesOrmlite extends OrmLiteBaseRepository<Configuraciones> {
    public ConfiguracionesOrmlite(OrmLiteDatabaseHelper helper) {
        super(helper, Configuraciones.class);
    }
}
