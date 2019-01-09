package com.xhq.demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class FavoriteDBHelper extends SQLiteOpenHelper {

	private SQLiteDatabase db; // 数据库对象
	private static final String DATABASE_NAME = "favorite.db"; // 数据库名
	private static final int DATABASE_VERSION = 2; // 数据库版本号
	private static final String TABLE_NAME = "tbl_favorite"; // 表名
	private static final String RESTAURANT_ID = "restaurant_id"; // 餐馆ID
	// 创建数据库表的sql语句
	private static final String CREATE_TABLE = "create table " + TABLE_NAME
			+ " (" + RESTAURANT_ID + " text" + ");";
	// 获取数据库数据需返回的数据行数
	private static final String[] columns = { RESTAURANT_ID };
	private ArrayList<String> restaurantIdList;

	/**
	 * 创建数据库
	 */
	public FavoriteDBHelper(Context context, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = getWritableDatabase();// 创建数据库对象
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
		// 清空数据库数据
	}

	public void close() {
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 按照ID查询餐馆是否收藏
	 */
	public Boolean queryRestaurant(String[] restaurantIds) {
		Boolean returnFlag = false;
		String where = RESTAURANT_ID + "=?";
		Cursor cursor;
		cursor = db.query(TABLE_NAME, columns, where, restaurantIds, null, null, null);
		while (cursor.moveToNext()) {
			returnFlag = true;
			break;
		}
        cursor.close();
        close();
		return returnFlag;
	}

	/**
	 * 获取数据库中所有的商品信息
	 */
	public List<String> queryAllRestaurant() {
		Cursor cursor;
		restaurantIdList = new ArrayList<>();
		cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
		while (cursor.moveToNext()) {
			restaurantIdList.add(cursor.getString(cursor
					.getColumnIndex(RESTAURANT_ID)));
		}
        cursor.close();
        close();
		return restaurantIdList;
	}

	/**
	 * 插入收藏餐馆ID
	 */
	public Boolean insertFavorite(String restaurantId) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(RESTAURANT_ID, restaurantId);
		db.insert(TABLE_NAME, null, contentValues);
		close();
		return true;
	}

	/**
	 * 删除餐馆ID
	 */
	public Boolean deleteFavorite(String restaurantId) {
		String where = RESTAURANT_ID + "=? ";
		String args[] = { restaurantId };
		db.delete(TABLE_NAME, where, args);
		close();
		return true;
	}

}
