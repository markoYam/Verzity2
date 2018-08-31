package com.dwmedios.uniconekt.data.database;

import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Persona;

/**
 * Created by mYam on 18/04/2018.
 */

public class PersonaOrmlite extends OrmLiteBaseRepository<Persona> {
    public PersonaOrmlite(OrmLiteDatabaseHelper helper) {
        super(helper, Persona.class);
    }
}
