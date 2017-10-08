package Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import Model.Customer;
import Util.Util;

/**
 * Created by napti on 10/8/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    //, String name, SQLiteDatabase.CursorFactory factory, int version
    // (might have to use, came with the class

    public DatabaseHandler(Context context) {
        super(context,Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL - Structured Query Language
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE" + Util.TABLE_NAME + "(" +
                Util.KEY_EMAIL_ADDRESS + " TEXT PRIMARY KEY," + Util.KEY_FIRST_NAME +
                " TEXT," + Util.KEY_LAST_NAME + " TEXT," + Util.KEY_PASSWORD + " TEXT," +
                Util.KEY_GENDER + " INTEGER," + Util.KEY_AGE + " TEXT" + ")";

        db.execSQL(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Dropping is deleting the table!
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);

        //CREATE TABLE AGAIN
        onCreate(db);
    }

    /**
     *  CRUD OPERATIONS - Create, Read, Update, Delete
     */

    //Add Customer
    public void addCustomer(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(Util.KEY_EMAIL_ADDRESS, customer.getEmail_address());
        value.put(Util.KEY_FIRST_NAME, customer.getFirst_name());
        value.put(Util.KEY_LAST_NAME, customer.getLast_name());
        value.put(Util.KEY_FIRST_NAME, customer.getFirst_name());
        value.put(Util.KEY_FIRST_NAME, customer.getFirst_name());
    }
}
