package com.example.a1512572.mobileminiproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZeruS on 5/13/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME_BTS = "BANG_THAM_SO";
    private static final String BTS_COL_1 = "ID";
    private static final String BTS_COL_2 = "TEN";
    private static final String BTS_COL_3 = "GIA_TRI";
    private static final String BTS_COL_4 = "TINH_TRANG";

    private static final String TABLE_NAME_CH = "CUA_HANG";
    private static final String CH_COL_1 = "ID";
    private static final String CH_COL_2 = "TEN";
    private static final String CH_COL_3 = "MO_TA";
    private static final String CH_COL_4 = "DIA_CHI";
    private static final String CH_COL_5 = "LAT";
    private static final String CH_COL_6 = "LNG";
    private static final String CH_COL_7 = "GIO_MO_CUA";
    private static final String CH_COL_8 = "GIO_DONG_CUA";
    private static final String CH_COL_9 = "DIEN_THOAI";
    private static final String CH_COL_10 = "LOAI";
    private static final String CH_COL_11 = "TINH_TRANG";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_BTS + " ("
                + BTS_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BTS_COL_2 + " TEXT,"
                + BTS_COL_3 + " TEXT,"
                + BTS_COL_4 + " TEXT"
                +")");

        db.execSQL("CREATE TABLE " + TABLE_NAME_CH + " ("
                + CH_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CH_COL_2 + " TEXT,"
                + CH_COL_3 + " TEXT,"
                + CH_COL_4 + " TEXT,"
                + CH_COL_5 + " TEXT,"
                + CH_COL_6 + " TEXT,"
                + CH_COL_7 + " TEXT,"
                + CH_COL_8 + " TEXT,"
                + CH_COL_9 + " TEXT,"
                + CH_COL_10 + " TEXT,"
                + CH_COL_11+ " TEXT"
                +")");

        setDataBTS(db);
        setDataCH(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CH);
        onCreate(db);
    }

    //Get/Set methods

    public boolean insertBTS(String name, String value, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valeus = new ContentValues();
        valeus.put(BTS_COL_2, name);
        valeus.put(BTS_COL_3, value);
        valeus.put(BTS_COL_4, status);
        long result = db.insert(TABLE_NAME_BTS, null, valeus);
        if (result == -1)
            return false;
        return true;
    }

    public boolean insertCH(String name, String desc, String addr, String lat, String lng, String open, String close, String phone, String type, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valeus = new ContentValues();
        valeus.put(CH_COL_2, name);
        valeus.put(CH_COL_3, desc);
        valeus.put(CH_COL_4, addr);
        valeus.put(CH_COL_5, lat);
        valeus.put(CH_COL_6, lng);
        valeus.put(CH_COL_7, open);
        valeus.put(CH_COL_8, close);
        valeus.put(CH_COL_9, phone);
        valeus.put(CH_COL_10, type);
        valeus.put(CH_COL_11, status);
        long result = db.insert(TABLE_NAME_CH, null, valeus);
        if (result == -1)
            return false;
        return true;
    }

    public Integer deleteBTS(String id){//Xóa data trong BDX dựa trên ID
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_BTS,"ID = ?", new String[] {id});
    }

    public Integer deleteCH(String id){//Xóa data trong BDX dựa trên ID
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_CH,"ID = ?", new String[] {id});
    }

    public boolean updateBTS(String id, String name, String value, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valeus = new ContentValues();
        valeus.put(BTS_COL_2, name);
        valeus.put(BTS_COL_3, value);
        valeus.put(BTS_COL_4, status);
        db.update(TABLE_NAME_BTS, valeus, "ID = ?", new String[]{ id });
        return true;
    }

    public boolean updateCH(String id, String name, String desc, String addr, String lat, String lng, String open, String close, String phone, String type, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valeus = new ContentValues();
        valeus.put(CH_COL_2, name);
        valeus.put(CH_COL_3, desc);
        valeus.put(CH_COL_4, addr);
        valeus.put(CH_COL_5, lat);
        valeus.put(CH_COL_6, lng);
        valeus.put(CH_COL_7, open);
        valeus.put(CH_COL_8, close);
        valeus.put(CH_COL_9, phone);
        valeus.put(CH_COL_10, type);
        valeus.put(CH_COL_11, status);
        db.update(TABLE_NAME_CH, valeus, "ID = ?", new String[]{ id });
        return true;
    }

    public Cursor getAllBTS(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME_BTS, null);
        return result;
    }

    public Cursor getAllCH(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME_CH, null);
        return result;
    }

    //Set initial data

    public boolean inputBTS(SQLiteDatabase db, String name, String value, String status){
        ContentValues valeus = new ContentValues();
        valeus.put(BTS_COL_2, name);
        valeus.put(BTS_COL_3, value);
        valeus.put(BTS_COL_4, status);
        long result = db.insert(TABLE_NAME_BTS, null, valeus);
        if (result == -1)
            return false;
        return true;
    }

    public boolean inputCH(SQLiteDatabase db, String name, String desc, String addr, String lat, String lng, String open, String close, String phone, String type, String status){
        ContentValues valeus = new ContentValues();
        valeus.put(CH_COL_2, name);
        valeus.put(CH_COL_3, desc);
        valeus.put(CH_COL_4, addr);
        valeus.put(CH_COL_5, lat);
        valeus.put(CH_COL_6, lng);
        valeus.put(CH_COL_7, open);
        valeus.put(CH_COL_8, close);
        valeus.put(CH_COL_9, phone);
        valeus.put(CH_COL_10, type);
        valeus.put(CH_COL_11, status);
        long result = db.insert(TABLE_NAME_CH, null, valeus);
        if (result == -1)
            return false;
        return true;
    }

    private void setDataBTS(SQLiteDatabase db) {
        inputBTS(db,"APP_NAME", "Trà sữa trong tôi","1");
        inputBTS(db,"APP_VERSION", "1.0.0","1");
    }

    private void setDataCH(SQLiteDatabase db) {
        inputCH(db,"Trà Sữa X-Cute","Đang cập nhât","176B Ngô Quyền, P. 8, Quận 10, TP. HCM","10.763338", "106.664779", "08:00","21:00","+84987783003","0","1");
        inputCH(db,"Trà Sữa Bumba","Đang cập nhât","FM20 Tầng Hầm 1 Vạn Hạnh Mall, 11 Sư Vạn Hạnh, P. 12,  Quận 10, TP. HCM","10.770266", "106.669973", "08:00","22:00","+02866805366","1","1");
    }
}
