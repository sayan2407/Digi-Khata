package com.uit.digi_khata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelperCustomer extends SQLiteOpenHelper
{
    public static final String DB_NAME = "customer.db" ;
    public static final String TABLE_NAME = "customer_data" ;
    public static final String COL_1 = "CPHONE" ;
    public static final String COL_2 = "CNAME" ;
    public static final String COL_3 = "CADD";
    public static final String COL_4 = "USERID" ;


    public DatabaseHelperCustomer(Context context)
    {
        super(context,DB_NAME,null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(CPHONE TEXT PRIMARY KEY,CNAME TEXT,CADD TEXT,USERID TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }
    public boolean insert_customer(String phone,String name,String address,String userid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues() ;
        c.put(COL_1,phone);
        c.put(COL_2,name);
        c.put(COL_3,address);
        c.put(COL_4,userid);

        long result = db.insert(TABLE_NAME,null,c);
        if (result == -1)
            return false ;
        else
            return true ;
    }

    public Cursor customer_data()
    {
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor res = db.rawQuery("SELECT CPHONE,CNAME,CADD,USERID FROM "+TABLE_NAME,null) ;
          return res ;
    }
}
