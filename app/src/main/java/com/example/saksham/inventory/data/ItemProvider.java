package com.example.saksham.inventory.data;

/**
 * Created by Saksham on 14-01-2018.
 */

import android.content.ContentProvider;

        import android.content.ContentProvider;
        import android.content.ContentUris;
        import android.content.ContentValues;
        import android.content.UriMatcher;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.net.Uri;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by Saksham on 14-01-2018.
 */

public class ItemProvider extends ContentProvider {
    ItemdbHelper itemdbHelper;

    private static final int Items = 100;
    private static final int Item_ID = 101;

    private static final UriMatcher surimatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        surimatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.path_itms, Items);

        surimatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.path_itms + "/#", Item_ID);
    }

    @Override
    public boolean onCreate() {
        itemdbHelper = new ItemdbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = itemdbHelper.getReadableDatabase();

        Cursor cursor;
        int match = surimatcher.match(uri);

        switch (match) {
            case Items:
                cursor = db.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case Item_ID:
                selection = ItemContract.ItemEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]
                        {
                                String.valueOf(ContentUris.parseId(uri))
                        };
                cursor = db.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = surimatcher.match(uri);
        switch (match) {
            case Items:
                return insertitem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }
    }

    private Uri insertitem(Uri uri, ContentValues values) {
        SQLiteDatabase database = itemdbHelper.getWritableDatabase();
        long id = database.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);

        if (id == -1) {
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int match = surimatcher.match(uri);
        SQLiteDatabase database = itemdbHelper.getWritableDatabase();
        switch (match) {
            case Items:
                int rowsDeleted = database.delete(ItemContract.ItemEntry.TABLE_NAME, s, strings);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case Item_ID:
                s = ItemContract.ItemEntry.COLUMN_ID + "=?";
                strings = new String[]
                        {
                                String.valueOf(ContentUris.parseId(uri))
                        };

                rowsDeleted = database.delete(ItemContract.ItemEntry.TABLE_NAME, s, strings);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int match = surimatcher.match(uri);
        switch (match) {
            case Items:
                return updateitem(uri, contentValues, s, strings);
            case Item_ID:
                s = ItemContract.ItemEntry.COLUMN_ID + "=?";
                strings = new String[]
                        {
                                String.valueOf(ContentUris.parseId(uri))
                        };
                return updateitem(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    public int updateitem(Uri uri, ContentValues values, String s, String[] stringargs) {
        if (values.containsKey(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME)) {
            String name = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = itemdbHelper.getWritableDatabase();
        int rowsUpdated = database.update(ItemContract.ItemEntry.TABLE_NAME, values, s, stringargs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }


    @Override
    public String getType(Uri uri) {
        final int match = surimatcher.match(uri);
        switch (match) {
            case Items:
                return ItemContract.ItemEntry.CONTENT_LIST_TYPE;
            case Item_ID:
                return ItemContract.ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}