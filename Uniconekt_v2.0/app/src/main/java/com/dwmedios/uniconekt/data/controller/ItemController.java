package com.dwmedios.uniconekt.data.controller;

import android.content.Context;

import com.dwmedios.uniconekt.data.database.ItemOrmLite;
import com.dwmedios.uniconekt.data.database.ormlite.base.OrmLiteDatabaseHelper;
import com.dwmedios.uniconekt.model.Item;

import java.util.List;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class ItemController {

    private Context mContext;
    private OrmLiteDatabaseHelper mOrmLiteDatabaseHelper;
    private ItemOrmLite mItemOrmLite;


    public ItemController(Context mContext) {
        this.mContext = mContext;
        mOrmLiteDatabaseHelper = new OrmLiteDatabaseHelper(this.mContext);
        mItemOrmLite = new ItemOrmLite(mOrmLiteDatabaseHelper);
    }

    public List<Item> getItem(){
        return mItemOrmLite.selectAll();
    }

    public boolean addIem(Item mItem){
        return mItemOrmLite.addElement(mItem) > 0;
    }
}
