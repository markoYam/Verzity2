package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.Usuario;

/**
 * Created by mYam on 17/04/2018.
 */

public class UsuarioOrmLite extends OrmLiteBaseRepository<Usuario> {

    public UsuarioOrmLite(OrmLiteDatabaseHelper helper) {
        super(helper, Usuario.class);
    }
}
