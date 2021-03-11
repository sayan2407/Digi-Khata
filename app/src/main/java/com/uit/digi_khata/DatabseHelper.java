package com.uit.digi_khata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabseHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "Profile.db" ;
    public static final String TABLE_NAME = "profile_data" ;
    public static final String COL_1 = "USERID" ;
    public static final String COL_2 = "EMAIL" ;
    public static final String COL_3 = "PHONE";
    public static final String COL_4 = "OWNER" ;
    public static final String COL_5 = "CNAME" ;

    public DatabseHelper(Context context)
    {
        super(context,DB_NAME,null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(USERID TEXT PRIMARY KEY,EMAIL TEXT,PHONE TEXT,OWNER TEXT,CNAME TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);

    }
    public boolean insertData(String id,String email,String phone,String owner,String cname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,email);
        contentValues.put(COL_3,phone);
        contentValues.put(COL_4,owner);
        contentValues.put(COL_5,cname);
        long result = db.insert(TABLE_NAME,null,contentValues) ;
        if (result == -1)
            return false ;
        else
            return true ;
    }
    public Cursor getOwnerData()
    {
        SQLiteDatabase db = this.getWritableDatabase() ;
        Cursor res = db.rawQuery("SELECT USERID,EMAIL,PHONE,OWNER,CNAME FROM "+TABLE_NAME,null) ;
        return  res ;
    }
}
