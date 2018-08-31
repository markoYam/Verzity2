package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.VentasPaquetes;

public class VentasPaquetesOrmlite extends OrmLiteBaseRepository<VentasPaquetes> {
    public VentasPaquetesOrmlite(OrmLiteDatabaseHelper helper) {
        super(helper, VentasPaquetes.class);
    }
}
