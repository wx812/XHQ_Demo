package com.xhq.demo.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xhq.demo.tools.appTools.AppUtils;

/**
 * contact database
 */
public class DBContactHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Igo.db";  
    private static final int DATABASE_VERSION = 1;  
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String TABLE_START = "(";
    private static final String TABLE_END = ")";
    private static final String TABLE_ID = "_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
    public DBContactHelper() {
        super(AppUtils.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }  

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE +"person" + TABLE_START + TABLE_ID +
        		"sUserName VARCHAR(30), "+
        		"userName VARCHAR(30), " +
        		"nickName VARCHAR(30), " +
        		"noteName VARCHAR(30), " +
        		"headName VARCHAR(30), " +
        		"pinyin VARCHAR(30), " +
        		"signature VARCHAR(30), " +
        		"place VARCHAR(30), " +
                "type INTEGER " + TABLE_END);

        db.execSQL(CREATE_TABLE +"labels" + TABLE_START + TABLE_ID +
        		"sUserName VARCHAR(30), " +
        		"type VARCHAR(3) ," +
        		"id VARCHAR(30) ," +
        		"name VARCHAR(30), " +
        		"gps VARCHAR(40), " +
        		"num INTEGER " + TABLE_END);

        db.execSQL(CREATE_TABLE +"share" + TABLE_START + TABLE_ID +
        		"userName VARCHAR(30), " +
        		"id1 VARCHAR(10), " +
        		"id2 VARCHAR(30), " +
                "bmp BLOB" + TABLE_END);

        db.execSQL(CREATE_TABLE +"message" + TABLE_START + TABLE_ID +
        		"userName VARCHAR(30), " +
        		"toUserName VARCHAR(30), "+
        		"nickName VARCHAR(30), " +
        		"time VARCHAR(20), " +
        		"content VARCHAR(30), " +
        		"url VARCHAR(80), " +
        		"additional VARCHAR(80), " +
        		"flag INTEGER" + TABLE_END);

        db.execSQL(CREATE_TABLE +"usernicknotename" + TABLE_START + TABLE_ID +
        		"userName VARCHAR(30), " +
        		"sUserName VARCHAR(30), "+
        		"nickName VARCHAR(30), "+
        		"noteName VARCHAR(30)" + TABLE_END);

        db.execSQL(CREATE_TABLE +"headurl" + TABLE_START + TABLE_ID +
        		"userName VARCHAR(30), " +
        		"url VARCHAR(100)"+ TABLE_END);

        db.execSQL(CREATE_TABLE +"chat" + TABLE_START + TABLE_ID +
        		"userName VARCHAR(30), " +
        		"sUserName VARCHAR(30), " +
        		"time VARCHAR(20), " +
        		"message VARCHAR(200), " +
        		"voice VARCHAR(30), " +
        		"pic VARCHAR(30), " +
        		"fromto INTEGER" + TABLE_END);
      
        db.execSQL(CREATE_TABLE +"shareitem" + TABLE_START + TABLE_ID +
        		"fUserName VARCHAR(30),"+
        		"userName VARCHAR(30),"+ 
                "nickName VARCHAR(30),"+
        		"shareId VARCHAR(30)," +
        		"restId VARCHAR(30)," +
        		"commentsNum INTEGER," +
        		"okNum INTEGER," +
        		"time INTEGER," +
        		"note VARCHAR(30)," +
        		"position VARCHAR(30)," +
        		"picUrlList VARCHAR(30)," +
        		"picIdList VARCHAR(30)," +
        		"picDescList VARCHAR(30)" + TABLE_END);
	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        Log.e("DBContact", "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS person");  
        onCreate(db);  
          
    }  
}
