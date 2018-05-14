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
    private static final int DATABASE_VERSION = 9;

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
    private static final String CH_COL_12 = "HINH_ANH";


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
                + CH_COL_11 + " TEXT,"
                + CH_COL_12 + " TEXT"
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

    public boolean insertCH(String name, String desc, String addr, String lat, String lng, String open, String close, String phone, String type, String status, String image){
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
        valeus.put(CH_COL_12, image);
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

    public boolean updateCH(String id, String name, String desc, String addr, String lat, String lng, String open, String close, String phone, String type, String status, String image){
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
        valeus.put(CH_COL_12, image);
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

    public Cursor getSpecCH(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor result = db.query(TABLE_NAME_CH, null, CH_COL_2, new String[] {name}, null, null, null);
        Cursor result = db.query(true, TABLE_NAME_CH, null,
                CH_COL_2 + "=?",
                new String[] {name},
                null, null, null, null);
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

    public boolean inputCH(SQLiteDatabase db, String name, String desc, String addr, String lat, String lng, String open, String close, String phone, String type, String status, String image){
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
        valeus.put(CH_COL_12, image);
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
        inputCH(db,"Trà Sữa X-Cute","Đang cập nhật mô tả","176B Ngô Quyền, P. 8, Quận 10, TP. HCM","10.763338", "106.664779", "08:00","21:00","+84987783003","0","1", "https://i.imgur.com/dUWeKFK.jpg");
        inputCH(db,"Trà Sữa Bumba","Đang cập nhật mô tả","FM20 Tầng Hầm 1 Vạn Hạnh Mall, 11 Sư Vạn Hạnh, P. 12,  Quận 10, TP. HCM","10.770266", "106.669973", "08:00","22:00","+02866805366","0","1", "https://i.imgur.com/v9oIwpI.jpg");
        inputCH(db,"TocoToco Bubble Tea - Sư Vạn Hạnh","Đang cập nhật mô tả","770B Sư Vạn Hạnh, P. 10,  Quận 10, TP. HCM","10.770547", "106.670479", "09:00","22:00","+841900636916","0","1", "https://i.imgur.com/QYGttls.jpg");
        inputCH(db,"Trà Sữa Bobapop - Lũy Bán Bích","Đang cập nhật mô tả","384 Lũy Bán Bích,  Quận Tân Phú, TP. HCM","10.776465", "106.634049", "11:00","22:00","+842839739567","0","1", "https://i.imgur.com/vA8WBqk.jpg");
        inputCH(db,"Trà Sữa Bobapop - Sư Vạn Hạnh","Đang cập nhật mô tả","485 Sư Vạn Hạnh, P. 12,  Quận 10, TP. HCM","10.775175", "106.668636", "09:30","22:00","+842838634300","0","1", "https://i.imgur.com/oZhPZpo.jpg");
        inputCH(db,"Trà Sữa Bobapop - Xô Viết Nghệ Tĩnh","Đang cập nhật mô tả","245 Xô Viết Nghệ Tĩnh, P. 17,  Quận Bình Thạnh, TP. HCM","10.802237", "106.710920", "08:00","22:30","+841888456494","0","1", "https://i.imgur.com/9f5lK6p.jpg");
        inputCH(db,"Mix & Mix Trà Sữa","Đang cập nhật mô tả","13 Trần Quốc Toản, P. 8,  Quận 3, TP. HCM","10.789327", "106.6872183", "09:30","22:00","+84963352530","0","1", "https://i.imgur.com/6a5eKLk.jpg");
        inputCH(db,"Trà Sữa Tiên Hưởng - Trần Hưng Đạo","Đang cập nhật mô tả","789 Trần Hưng Đạo, P. 1,  Quận 5, TP. HCM","10.7538253", "106.6685567", "11:00","22:00","+842839236195","0","1", "https://i.imgur.com/UVU324S.jpg");
        inputCH(db,"Trà Sữa Gong Cha - 貢茶 - Hồ Tùng Mậu","Đang cập nhật mô tả","83 Hồ Tùng Mậu,  Quận 1, TP. HCM","10.7720131", "106.701471", "08:30","21:30","+84938347138","0","1", "https://i.imgur.com/X4OigQX.jpg");
        inputCH(db,"Trà Sữa Gong Cha - 貢茶 - Phan Văn Trị","Đang cập nhật mô tả","595 Phan Văn Trị, P. 5,  Quận Gò Vấp, TP. HCM","10.7946774", "106.6817771", "09:00","22:00","+842838945479","0","1", "https://i.imgur.com/ewhvLKn.jpg");
        inputCH(db,"Trà Sữa Gong Cha - 貢茶 - Phan Xích Long","Đang cập nhật mô tả","240 Phan Xích Long, P. 7,  Quận Phú Nhuận, TP. HCM","10.7946695", "106.681777", "09:00","22:00","+842835511568","0","1", "https://i.imgur.com/1VgBwnH.jpg");
    }
}
