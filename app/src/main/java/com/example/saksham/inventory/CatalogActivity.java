package com.example.saksham.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.saksham.inventory.data.ItemContract;
import com.example.saksham.inventory.data.ItemdbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
   static ItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);

            }
        });
        ListView list=(ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        list.setEmptyView(emptyView);
         adapter=new ItemAdapter(this,null);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                Uri uri1= ContentUris.withAppendedId(ItemContract.ItemEntry.ContentUri,id);

                Intent intent=new Intent(CatalogActivity.this,EditorActivity.class);

                intent.setData(uri1);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0,null,this);
    }

    public void insertItem()
    {
        ContentValues values =new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME,"Nike Flip Flopsd");
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY,50);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_RESTOCK,0);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE,5);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NAME,"Amazone");
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NUMBER,"amazone@gmail.com");

        getContentResolver().insert(ItemContract.ItemEntry.ContentUri, values);
    }
    public void showdeletedialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all Items?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(ItemContract.ItemEntry.ContentUri,null,null);
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                 insertItem();
                return true;

            case R.id.action_delete_all_entries:
                showdeletedialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ItemContract.ItemEntry.COLUMN_ID,
                ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME,
                ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemContract.ItemEntry.COLUMN_ITEM_RESTOCK,
                ItemContract.ItemEntry.COLUMN_ITEM_PRICE,
                ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NUMBER,
                ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NUMBER,
        };
        return new CursorLoader(this, ItemContract.ItemEntry.ContentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
