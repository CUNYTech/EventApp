package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" +
                Util.KEY_ID + " INTEGER PRIMARY KEY, " +
                Util.KEY_EMAIL_ADDRESS + " TEXT, " + Util.KEY_FIRST_NAME +
                " TEXT," + Util.KEY_LAST_NAME + " TEXT, " + Util.KEY_PASSWORD + " TEXT, " +
                Util.KEY_GENDER + " TEXT, " + Util.KEY_AGE + " INTEGER" + ")";

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
        value.put(Util.KEY_EMAIL_ADDRESS, customer.getemail());
        value.put(Util.KEY_FIRST_NAME, customer.getfirstName());
        value.put(Util.KEY_LAST_NAME, customer.getlastName());
        value.put(Util.KEY_PASSWORD, customer.getPassword());
        value.put(Util.KEY_GENDER, customer.getGender());
        value.put(Util.KEY_AGE, customer.getAge());

        //Insert to row
        db.insert(Util.TABLE_NAME, null, value);
        db.close(); //close db connection
    }

    //Get a customer
    public Customer getCustomer(int id){
        SQLiteDatabase db = this.getReadableDatabase(); //reading through the database

        Cursor cursor = db.query(Util.TABLE_NAME, new String[] {Util.KEY_ID, Util.KEY_EMAIL_ADDRESS,
            Util.KEY_FIRST_NAME, Util.KEY_LAST_NAME, Util.KEY_PASSWORD, Util.KEY_GENDER,
            Util.KEY_AGE}, Util.KEY_ID + " =?", new String[] {String.valueOf(1)},
            null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Customer customer = new Customer(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                Integer.parseInt(cursor.getString(6)));

        return customer;
    }

    //Get all customers
    public List<Customer> getAllCustomers(){

        SQLiteDatabase db = this.getReadableDatabase();

        List<Customer> customerList = new ArrayList<>();

        //Select all customers
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        //Loop through our customers
        //do while will continue to iterate until there is no content left
        if (cursor.moveToFirst()){
            do{
                Customer customer = new Customer();
                customer.setId(Integer.parseInt(cursor.getString(0)));
                customer.setemail(cursor.getString(1));
                customer.setfirstName(cursor.getString(2));
                customer.setlastName(cursor.getString(3));
                customer.setPassword(cursor.getString(4));
                customer.setGender(cursor.getString(5));
                customer.setAge(Integer.parseInt(cursor.getString(6)));

                //add customer object to our customer list
                customerList.add(customer);

            } while (cursor.moveToNext());
        }

        return customerList;
    }

    //updateCustomer
    public int updateCustomer(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_EMAIL_ADDRESS, customer.getemail());
        values.put(Util.KEY_FIRST_NAME, customer.getfirstName());
        values.put(Util.KEY_LAST_NAME, customer.getlastName());
        values.put(Util.KEY_PASSWORD, customer.getPassword());
        values.put(Util.KEY_GENDER, customer.getGender());
        values.put(Util.KEY_AGE, customer.getAge());

        //update row
        return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
                new String[] {String.valueOf(customer.getId())});
    }

    //Delete single contact
    public void deleteCustomer(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
                new String[] {String.valueOf(customer.getId())});

        db.close();
    }

    //Get customer count
    public int getCustomerCount(){
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        //cursor.close();
        return cursor.getCount();
    }
}
