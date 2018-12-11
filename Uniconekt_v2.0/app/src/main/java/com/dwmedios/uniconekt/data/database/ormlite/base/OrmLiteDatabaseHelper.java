package com.dwmedios.uniconekt.data.database.ormlite.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.dwmedios.uniconekt.model.Configuraciones;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.model.VentaPaqueteAsesor;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class OrmLiteDatabaseHelper extends OrmLiteSqliteOpenHelper {

    public OrmLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        for (Class<?> currentClass : mDbClasses) {
            try {
                TableUtils.createTableIfNotExists(connectionSource, currentClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dropDataBase() {
        dropDatabase(getConnectionSource());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        updateDatabase(connectionSource);
    }

    private void updateDatabase(ConnectionSource connSource) {
        for (Class<?> currentClass : mDbClasses) {
            try {
                TableUtils.createTableIfNotExists(connSource, currentClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dropDatabase(ConnectionSource connectionSource) {
        for (Class<?> currentClass : mDbClasses) {
            try {
                TableUtils.dropTable(connectionSource, currentClass, true);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        onCreate(getWritableDatabase(), connectionSource);

    }

    private static Class<?>[] mDbClasses = {Item.class, Direccion.class, Dispositivo.class, Persona.class, Universidad.class, Usuario.class, VentasPaquetes.class, Configuraciones.class, VentaPaqueteAsesor.class,Paquetes.class};

    private static final String DATABASE_NAME = "uniconekt.db";
    private static final int DATABASE_VERSION = 1;
}

