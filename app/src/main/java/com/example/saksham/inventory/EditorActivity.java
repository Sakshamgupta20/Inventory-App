package com.example.saksham.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.saksham.inventory.data.ItemContract;
import com.example.saksham.inventory.data.ItemdbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_PET_LOADER = 0;
    private EditText mPNameEditText;

    private EditText mQuantityEditText;

    private EditText mQuantityRestock;

    private EditText mPriceEditText;

    private EditText mSNameEditText;

    private EditText mSNumberEditText;

    private LinearLayout layout1;

    private boolean mPetHasChanged = false;

    private Uri current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mPNameEditText = (EditText) findViewById(R.id.edit_item_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_item_quantity);
        mQuantityRestock = (EditText) findViewById(R.id.edit_item_restock);
        mPriceEditText = (EditText) findViewById(R.id.edit_item_price);
        mSNameEditText = (EditText) findViewById(R.id.edit_item_supplier);
        mSNumberEditText = (EditText) findViewById(R.id.edit_item_number);

         layout1=(LinearLayout)findViewById(R.id.linear);

        mPNameEditText.setOnTouchListener(mTouchListener);

        mQuantityEditText .setOnTouchListener(mTouchListener);

        mPriceEditText.setOnTouchListener(mTouchListener);

        mQuantityRestock.setOnTouchListener(mTouchListener);

        mSNameEditText.setOnTouchListener(mTouchListener);

        mSNumberEditText.setOnTouchListener(mTouchListener);

        ImageButton incre=(ImageButton) findViewById(R.id.edit_item_add);
        ImageButton decre=(ImageButton) findViewById(R.id.edit_item_remove);
        incre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t=Integer.parseInt(mQuantityEditText.getText().toString());
                mQuantityEditText.setText(String.valueOf(t+1));
            }
        });
        decre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t=Integer.parseInt(mQuantityEditText.getText().toString());
               if(t>0) {
                   mQuantityEditText.setText(String.valueOf(t - 1));
               }
            }
        });

        ImageButton reincre=(ImageButton) findViewById(R.id.edit_item_readd);
        ImageButton redecre=(ImageButton) findViewById(R.id.edit_item_reremove);

       reincre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t=Integer.parseInt(mQuantityRestock.getText().toString());
                mQuantityRestock.setText(String.valueOf(t+1));
            }
        });
        redecre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t=Integer.parseInt(mQuantityRestock.getText().toString());
                if(t>0) {
                    mQuantityRestock.setText(String.valueOf(t - 1));
                }
            }
        });

        Intent intent = getIntent();
        current=intent.getData();

        if (current==null)
        {
            setTitle("Add a Item");
           layout1.setVisibility(View.GONE);
        }
        else {
            setTitle("Edit Item");
            incre.setVisibility(View.GONE);
            decre.setVisibility(View.GONE);
            mQuantityEditText.setEnabled(false);
            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }
    }

    private View.OnTouchListener mTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged=true;
            return false;
        }
    };


public void restock()
{
    int currentquantity=Integer.parseInt(mQuantityEditText.getText().toString());
    int currentrestock=Integer.parseInt(mQuantityRestock.getText().toString());
    if(currentrestock==0)
    {
        Toast.makeText(this,"Enter Restock Value",Toast.LENGTH_SHORT).show();
    }
    else {
        String suppliernumber=mSNumberEditText.getText().toString();
        String pname=mPNameEditText.getText().toString();
        Intent intent1=new Intent(Intent.ACTION_SENDTO);
        intent1.setData(Uri.parse("mailto:"));
        intent1.putExtra(Intent.EXTRA_SUBJECT,"Order for Item");
        intent1.putExtra(Intent.EXTRA_EMAIL, new String[] { suppliernumber });
        String message=pname+"\n"+"Quantity Required - "+currentrestock;
        intent1.putExtra(Intent.EXTRA_TEXT,message);
        if (intent1.resolveActivity(getPackageManager()) != null) {
            startActivity(intent1);
        }
        currentquantity = currentrestock + currentquantity;
        currentrestock = 0;
        mQuantityEditText.setText(String.valueOf(currentquantity));
        mQuantityRestock.setText(String.valueOf(currentrestock));
    }
}
    public void insertitem()
    {

        String pname=mPNameEditText.getText().toString();
         String quantity=mQuantityEditText.getText().toString();
        String restock=mQuantityRestock.getText().toString();
        String price=mPriceEditText.getText().toString();
        String supplirname=mSNameEditText.getText().toString();
        String suppliernumber=mSNumberEditText.getText().toString();

        if(TextUtils.isEmpty(pname))
        {
            Toast.makeText(this,"Enter Product Name",Toast.LENGTH_SHORT).show();
        }
        else if(quantity.equals("0"))
        {
            Toast.makeText(this,"Enter Quantity/Restock",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(this,"Enter Price",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(supplirname))
        {
            Toast.makeText(this,"Enter Suppiler Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(suppliernumber))
        {
            Toast.makeText(this,"Enter Suppiler Email",Toast.LENGTH_SHORT).show();
        }

        else {
            ContentValues values = new ContentValues();
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME, pname);
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_RESTOCK, restock);
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, price);
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, supplirname);
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NUMBER, suppliernumber);


            if(current==null)
            {
                Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.ContentUri, values);
                if (newUri == null) {
                    Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Insert Pet Successful", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
            else
            {
                int rowsAffected= getContentResolver().update(current,values,null,null);
                if (rowsAffected == 0) {
                    Toast.makeText(this, "Pet Update Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Pet Update Successful",Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }
    public void showdeletedialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Delete this Item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePet();
                finish();
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
    private void deletePet()
    {
        long id= getContentResolver().delete(current,null,null);
        if(id==0)
        {
            Toast.makeText(this,"Delete Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Delete Successful",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (current == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
            MenuItem reitem = menu.findItem(R.id.action_restock);
            reitem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_delete:
                showdeletedialog();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_save:
                insertitem();
                return true;
            case R.id.action_restock:
                restock();
                return true;

            case android.R.id.home:

                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // User clicked "Discard" button, navigate to parent activity.
                                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                                }
                            };

                    // Show a dialog that notifies the user they have unsaved changes
                    showUnsavedChangesDialog(discardButtonClickListener);
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
                ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NAME,
                ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NUMBER,
        };
        return new CursorLoader(this, current,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if(cursor.moveToFirst())
        {
            int pnameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY);
            int restockColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_RESTOCK);
            int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
            int supplierColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NAME);
            int suppliernumberColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NUMBER);

            String pname=cursor.getString(pnameColumnIndex);
            String quantity=cursor.getString(quantityColumnIndex);
            String restock=cursor.getString(restockColumnIndex);
            int price=cursor.getInt(priceColumnIndex);
            String supplirname=cursor.getString(supplierColumnIndex);
            String suppliernumber=cursor.getString(suppliernumberColumnIndex);

            mPNameEditText.setText(pname);
            mQuantityEditText.setText(quantity);
            mQuantityRestock.setText(restock);
            mPriceEditText.setText(Integer.toString(price));
            mSNameEditText.setText(supplirname);
            mSNumberEditText.setText(suppliernumber);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPNameEditText.setText("");
        mQuantityEditText.setText("");
        mQuantityRestock.setText("");
        mPriceEditText.setText("");
        mSNameEditText.setText("");
        mSNumberEditText.setText("");
    }
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener)
    {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard", discardButtonClickListener);

        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

}
