package com.example.saksham.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Saksham on 13-01-2018.
 */
public class ItemContract {
    public static final String CONTENT_AUTHORITY="com.example.saksham.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String path_itms="items";
    public static abstract class ItemEntry implements BaseColumns
    {
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + path_itms;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + path_itms;

        public static final Uri ContentUri=Uri.withAppendedPath(BASE_CONTENT_URI,path_itms);

        public static final String TABLE_NAME="items";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_ITEM_PRODUCT_NAME= "name";
        public static final String COLUMN_ITEM_QUANTITY = "quantity";
        public static final String COLUMN_ITEM_RESTOCK= "restock";
        public static final String COLUMN_ITEM_PRICE = "price";
        public static final String COLUMN_ITEM_SUPPLIER_NAME = "sname";
        public static final String COLUMN_ITEM_SUPPLIER_NUMBER = "snumber";
    }

}
