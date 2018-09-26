package com.xhq.demo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBService {
	private SQLiteDatabase db;
    
	public DBService() {
		DBContactHelper helper = new DBContactHelper();
		db = helper.getWritableDatabase();
	}
	
    public void dropTable(String tableName) {  
    	db.execSQL("DROP TABLE IF EXISTS " + tableName);
  
    }  

    public void closeDatabase(String DatabaseName) {  
        db.close();        
    }  

    public void closeDB() {  
        db.close();  
    }  
    /** 
     * query table, return amount 
     * @return  int 
     */  
    public int queryTableAmount(String pTableName) {  
    	return db.rawQuery("SELECT * FROM "+ pTableName, null).getCount();    
    }  

    public void addPerson(Person pPerson) {
//    	Log.e("db addPerson","A="+pPerson.nickName);
    	deleteFriend(pPerson.sUserName, pPerson.userName);
    	if(pPerson.noteName.length()>1){
//    		pPerson.pinyin= HanziToPinyin.cn2FirstSpell(pPerson.noteName);
    	}else {
//    		pPerson.pinyin=HanziToPinyin.cn2FirstSpell(pPerson.nickName);
		}
    	Log.e("pinyin",pPerson.pinyin);
        db.beginTransaction();  
        try {  
            db.execSQL("INSERT INTO person VALUES(null,?,?,?,?,?,?,?,?,?)", 
            		new Object[]{
            		pPerson.sUserName,
            		pPerson.userName,
            		pPerson.nickName,
            		pPerson.noteName,
            		pPerson.headName,
            		pPerson.pinyin,
            		pPerson.signatureString,
            		pPerson.place,
            		pPerson.type
            });
            db.setTransactionSuccessful();  
        } finally {  
            db.endTransaction();    
        }  
//        Log.e("db addPerson","C="+pPerson.userName);
    } 

   
    public void updateRemark(String suername,String username,String remark){
    	ContentValues cv = new ContentValues();
        db.beginTransaction();  
        try {  
       	     cv.put("noteName", remark);
//       	     cv.put("pinyin", HanziToPinyin.cn2FirstSpell(remark));
       	     db.update("person", cv, "sUserName = ? and  userName = ?", new String[]{suername,username});
       	     db.setTransactionSuccessful();  
        } finally {  
            db.endTransaction();
        } 
    }
    
    public void deleteFriend (String suername,String username){
        db.delete("person",  "sUserName = ? and  userName = ?", new String[]{suername,username});
   } 
    public ArrayList<Person> getPersons (String sUsername){
    	ArrayList<Person> personList = new ArrayList<>();
    	db.beginTransaction();  
        try { 
        	Cursor c = db.rawQuery("select * from person where susername=? order by pinyin ASC"
					,new String[]{sUsername});
        	while (c.moveToNext()){
        		Person pPerson = new Person();
        		pPerson.sUserName =c.getString(c.getColumnIndex("sUserName"));
        		pPerson.userName =c.getString(c.getColumnIndex("userName"));
        		pPerson.nickName =c.getString(c.getColumnIndex("nickName"));
        		pPerson.noteName =c.getString(c.getColumnIndex("noteName"));
        		pPerson.headName =c.getString(c.getColumnIndex("headName"));
        		pPerson.pinyin=c.getString(c.getColumnIndex("pinyin"));
        		pPerson.signatureString=c.getString(c.getColumnIndex("signature"));
        		pPerson.place=c.getString(c.getColumnIndex("place"));
        		pPerson.type=c.getInt(c.getColumnIndex("type"));
        		personList.add(pPerson);
        		Log.e("nickName",pPerson.nickName);
        	}
         	 c.close();
       	     db.setTransactionSuccessful();  
        } finally {  
            db.endTransaction();
        }
    	return personList;
    }
    

    public Person getPerson (String sUsername,String fUsername){
    	Person pPerson = new Person();
    	db.beginTransaction();  
        try { 
        	Cursor c = db.rawQuery("select * from person where susername=? and username=?"
					,new String[]{sUsername,fUsername});
        	while (c.moveToNext()){
        		
        		pPerson.sUserName =c.getString(c.getColumnIndex("sUserName"));
        		pPerson.userName =c.getString(c.getColumnIndex("userName"));
        		pPerson.nickName =c.getString(c.getColumnIndex("nickName"));
        		pPerson.noteName =c.getString(c.getColumnIndex("noteName"));
        		pPerson.headName =c.getString(c.getColumnIndex("headName"));
        		pPerson.pinyin=c.getString(c.getColumnIndex("pinyin"));
        		pPerson.signatureString=c.getString(c.getColumnIndex("signature"));
        		pPerson.place=c.getString(c.getColumnIndex("place"));
        		pPerson.type=c.getInt(c.getColumnIndex("type"));
        	}
         	 c.close();
       	     db.setTransactionSuccessful();  
        } finally {  
            db.endTransaction();
        }
    	return pPerson;
    }
    

    
    
}
