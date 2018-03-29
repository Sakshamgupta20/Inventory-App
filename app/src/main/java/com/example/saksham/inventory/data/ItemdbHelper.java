package com.example.saksham.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Saksham on 13-01-2018.
 */

public class ItemdbHelper extends SQLiteOpenHelper {
    private static final String DatabaseName="items.db";
    private static final int DatabaseVersion=1;
public ItemdbHelper(Context context)
{
    super(context,DatabaseName,null,DatabaseVersion);
}
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createtable="CREATE TABLE "
                + ItemContract.ItemEntry.TABLE_NAME + " ("
                + ItemContract.ItemEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + ItemContract.ItemEntry.COLUMN_ITEM_RESTOCK + " INTEGER DEFAULT 0, "
                + ItemContract.ItemEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NAME + " TEXT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NUMBER + " TEXT);";

        db.execSQL(createtable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
