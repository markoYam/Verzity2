package com.dwmedios.uniconekt.data.database;


import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteBaseRepository;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Item;

public class ItemOrmLite extends OrmLiteBaseRepository<Item> {

    public ItemOrmLite(OrmLiteDatabaseHelper helper) {
        super(helper, Item.class);
    }
}
