package com.sharingame.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocalDB {

    private LocalDBHelper helper;

    public LocalDB(Context context)
    {
        helper = new LocalDBHelper(context);
    }

    public long insertData(String name, String pass)
    {
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalDBHelper.NAME, name);
        contentValues.put(LocalDBHelper.MyPASSWORD, pass);
        long id = dbb.insert(LocalDBHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public String getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {LocalDBHelper.UID, LocalDBHelper.NAME, LocalDBHelper.MyPASSWORD};
        Cursor cursor =db.query(LocalDBHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(LocalDBHelper.UID));
            String name =cursor.getString(cursor.getColumnIndex(LocalDBHelper.NAME));
            String  password =cursor.getString(cursor.getColumnIndex(LocalDBHelper.MyPASSWORD));
            buffer.append(cid+ "   " + name + "   " + password +" \n");
        }
        return buffer.toString();
    }

    public  int delete(String user_name)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs ={user_name};

        int count =db.delete(LocalDBHelper.TABLE_NAME ,LocalDBHelper.NAME+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalDBHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(LocalDBHelper.TABLE_NAME,contentValues, LocalDBHelper.NAME+" = ?",whereArgs );
        return count;
    }

    static class LocalDBHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "sharg_local_db";    // Database Name
        private static final String TABLE_NAME = "local_table";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="u_id";     // Column I (Primary Key)
        private static final String NAME = "sharg";    //Column II
        private static final String MyPASSWORD= "a123456b";    // Column III
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+ MyPASSWORD+" VARCHAR(225));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public LocalDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                Message.message(context,""+e);
            }
        }
    }
}