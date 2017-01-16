package zt.sakoonkinamaz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Haseeb Bhai on 1/14/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "sakoonkinamaz.db";
    private final static int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(PrayersDataSource.CREATE_PRAYERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(PrayersDataSource.DELETE_PRAYERS);
        onCreate(database);
    }
}
