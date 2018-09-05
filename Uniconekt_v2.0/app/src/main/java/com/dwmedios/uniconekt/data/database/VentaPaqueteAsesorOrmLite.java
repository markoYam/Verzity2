package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.VentaPaqueteAsesor;
import com.dwmedios.uniconekt.model.VentasPaquetes;

public class VentaPaqueteAsesorOrmLite extends OrmLiteBaseRepository<VentaPaqueteAsesor> {
    public VentaPaqueteAsesorOrmLite(OrmLiteDatabaseHelper helper) {
        super(helper, VentaPaqueteAsesor.class);
    }
}
