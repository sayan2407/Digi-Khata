package com.uit.digi_khata;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseAccountHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME="Acc.db";
    public static final String TAB1_NAME="paym_data";
    public static final String COL_1="ID";
    public static final String COL_2="C_NAME";
    public static final String COL_3="C_PHONE";

    public static final String COL_5="DATE";
    public static final String COL_6="TIME";
    public static final String COL_7="ACCEPT";
    public static final String COL_8="CREDIT";
    public DatabaseAccountHelper(Context context)
    {
        super(context,DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase() ;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB1_NAME+"(ID TEXT,C_NAME TEXT,C_PHONE TEXT, DATE TEXT,TIME TEXT,ACCEPT TEXT,CREDIT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public boolean paymentInsert(String id,String cname,String cphone,String date,String time,String accept,String pay)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put(COL_1,id);
        c.put(COL_2,cname);
        c.put(COL_3,cphone);
        c.put(COL_5,date);
        c.put(COL_6,time);
        c.put(COL_7,accept);
        c.put(COL_8,pay);
        long result=db.insert(TAB1_NAME,null,c);
        if (result==-1)
            return false;
        else
            return true;

    }
}
