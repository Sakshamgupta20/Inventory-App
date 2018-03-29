package com.example.saksham.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saksham.inventory.data.ItemContract;
import com.example.saksham.inventory.data.ItemdbHelper;

/**
 * Created by Saksham on 14-01-2018.
 */

public class ItemAdapter extends CursorAdapter {

    ItemAdapter(Context context, Cursor c)
    {
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        String namestring=cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME));
        String pricestring=cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE));
        pricestring=pricestring + " $";
        final String quantitystring=cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY));


        TextView name=(TextView)view.findViewById(R.id.name1);
        TextView price=(TextView)view.findViewById(R.id.price1);
        TextView quantity=(TextView)view.findViewById(R.id.quantity1);
        Button saleButton=(Button)view.findViewById(R.id.sale);

        int currentId = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ID));
        // Make the content uri for the current Id
        final Uri contentUri = Uri.withAppendedPath(ItemContract.ItemEntry.ContentUri, Integer.toString(currentId));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantityint = Integer.valueOf(quantitystring);

                if (quantityint > 0) {
                    quantityint = quantityint - 1;
                }
                ContentValues values = new ContentValues();
                values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, quantityint);
                // update the database
                context.getContentResolver().update(contentUri, values, null, null);
            }
        });
        name.setText(namestring);
        quantity.setText(quantitystring);
        price.setText(pricestring);
    }
}
