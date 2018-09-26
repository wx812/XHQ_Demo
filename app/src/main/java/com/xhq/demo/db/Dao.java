//package com.xhq.demo.db;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.smzw.entity.DownloadInfo;
//import com.smzw.entity.FileStatus;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Dao
//{
//	private static Dao dao;
//	private static DBHelper dbHelper;
//	public static byte[] Lock = new byte[0];
//	public static byte[] file_Lock = new byte[0];
//
//	private Dao()
//	{
//	}
//
//	public static Dao getInstance(Context context)
//	{
//		if (dao == null)
//		{
//			dao = new Dao();
//			dbHelper = new DBHelper(context);
//		}
//		return dao;
//	}
//
//	public boolean isExist(String url)
//	{
//		SQLiteDatabase database = dbHelper.getReadableDatabase();
//		String sql = "select count(*) from " + DBHelper.TABLE_LOCAL_DOWNLOAD_INFO + " where url=?";
//		Cursor cursor = database.rawQuery(sql, new String[] { url });
//		cursor.moveToFirst();
//		int count = cursor.getInt(0);
//		cursor.close();
//		return count > 0;
//	}
//
//	public boolean isFirstDownload(String url)
//	{
//		SQLiteDatabase database = dbHelper.getReadableDatabase();
//		String sql = "select count(*) from " + DBHelper.TABLE_DOWNLOAD_INFO + " where url=?";
//		Cursor cursor = database.rawQuery(sql, new String[] { url });
//		cursor.moveToFirst();
//		int count = cursor.getInt(0);
//		cursor.close();
//		return count == 0;
//	}
//
//	public void saveInfos(List<DownloadInfo> infos, Context context)
//	{
//		// ����ҲҪ��������ķ������Ч��
//		synchronized (Lock)
//		{
//			SQLiteDatabase database = dbHelper.getWritableDatabase();
//			database.beginTransaction();
//			try
//			{
//				for (DownloadInfo info : infos)
//				{
//					String sql = "insert into " + DBHelper.TABLE_DOWNLOAD_INFO + "(thread_id,start_pos, end_pos,complete_size,url) values (?,?,?,?,?)";
//					Object[] bindArgs = { info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompeleteSize(), info.getUrl() };
//					database.execSQL(sql, bindArgs);
//				}
//				database.setTransactionSuccessful();
//			}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				database.endTransaction();
//			}
//		}
//	}
//
//	public List<DownloadInfo> getInfos(String urlstr)
//	{
//		List<DownloadInfo> list = new ArrayList<DownloadInfo>();
//		SQLiteDatabase database = dbHelper.getReadableDatabase();
//		String sql = "select thread_id, start_pos, end_pos,complete_size,url from " + DBHelper.TABLE_DOWNLOAD_INFO + " where url=?";
//		Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
//		while (cursor.moveToNext())
//		{
//			DownloadInfo info = new DownloadInfo(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
//			list.add(info);
//		}
//		cursor.close();
//		return list;
//	}
//
//	public void insertFileStatus(FileStatus fileStatus)
//	{
//		synchronized (file_Lock)
//		{
//			SQLiteDatabase database = dbHelper.getWritableDatabase();
//			database.beginTransaction();
//			try
//			{
//				String sql = "insert into " + DBHelper.TABLE_LOCAL_DOWNLOAD_INFO + " (name,url,completeSize,fileSize,status) values(?,?,?,?,?)";
//				Object[] bindArgs = { fileStatus.getName(), fileStatus.getUrl(), fileStatus.getCompleteSize(), fileStatus.getFileSize(), fileStatus.getStatus() };
//				database.execSQL(sql, bindArgs);
//				database.setTransactionSuccessful();
//			}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				database.endTransaction();
//			}
//		}
//	}
//
//	public void updataInfos(int threadId, int compeleteSize, String urlstr, Context context)
//	{
//		synchronized (Lock)
//		{
//			String sql = "update " + DBHelper.TABLE_DOWNLOAD_INFO + " set complete_size=? where thread_id=? and url=?";
//			String hql = "update " +DBHelper.TABLE_LOCAL_DOWNLOAD_INFO + " set completeSize = (select sum(complete_size) from "+DBHelper.TABLE_DOWNLOAD_INFO+" where url = ? group by url) where url = ?";
//			Object[] bindArgs = { compeleteSize, threadId, urlstr };
//			Object[] hqlArgs = { urlstr, urlstr };
//			SQLiteDatabase database = dbHelper.getWritableDatabase();
//			database.beginTransaction();
//			try
//			{
//				database.execSQL(sql, bindArgs);
//				database.execSQL(hql, hqlArgs);
//				database.setTransactionSuccessful();
//			}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				database.endTransaction();
//			}
//		}
//	}
//
//	public void updateFileStatus(String url)
//	{
//		synchronized (file_Lock)
//		{
//			String sql = "update " + DBHelper.TABLE_LOCAL_DOWNLOAD_INFO + " set status=? where url=?";
//			Object[] bindArgs = { 1, url };
//			SQLiteDatabase database = dbHelper.getWritableDatabase();
//			database.beginTransaction();
//			try
//			{
//				database.execSQL(sql, bindArgs);
//				database.setTransactionSuccessful();
//			}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				database.endTransaction();
//			}
//		}
//	}
//
//	public List<FileStatus> getFileStatus()
//	{
//		List<FileStatus> list = new ArrayList<FileStatus>();
//		SQLiteDatabase database = dbHelper.getReadableDatabase();
//		String sql = "select name,url,status,completeSize,fileSize from " + DBHelper.TABLE_LOCAL_DOWNLOAD_INFO + "";
//		Cursor cursor = database.rawQuery(sql, null);
//		while (cursor.moveToNext())
//		{
//			FileStatus fileState = new FileStatus(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
//			list.add(fileState);
//		}
//		cursor.close();
//		return list;
//	}
//
//	public void updateFileDownStatus(int completeSize, int status, String url)
//	{
//		synchronized (file_Lock)
//		{
//			String sql = "update " + DBHelper.TABLE_LOCAL_DOWNLOAD_INFO + " set completeSize=?,status=? where url=?";
//			SQLiteDatabase database = dbHelper.getWritableDatabase();
//			database.beginTransaction();
//			try
//			{
//				Object[] bindArgs = { completeSize, status, url };
//				database.execSQL(sql, bindArgs);
//				database.delete(DBHelper.TABLE_DOWNLOAD_INFO, "url=?", new String[] { url });
//				database.setTransactionSuccessful();
//			}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				database.endTransaction();
//			}
//		}
//	}
//
//	public String getFileName(String url)
//	{
//		String result = "";
//		String sql = "select name from " + DBHelper.TABLE_LOCAL_DOWNLOAD_INFO + " where url=?";
//		SQLiteDatabase database = dbHelper.getReadableDatabase();
//		Cursor cursor = database.rawQuery(sql, new String[] { url });
//		if(cursor.moveToNext())
//		{
//			result = cursor.getString(0);
//		}
//		return result;
//	}
//
//	public void deleteFile(String url)
//	{
//		SQLiteDatabase database = dbHelper.getWritableDatabase();
//		database.beginTransaction();
//		try
//		{
//			database.delete(DBHelper.TABLE_DOWNLOAD_INFO, "url=?", new String[] { url });
//			database.delete(DBHelper.TABLE_LOCAL_DOWNLOAD_INFO, "url=?", new String[] { url });
//			database.setTransactionSuccessful();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			database.endTransaction();
//		}
//	}
//
//	public void closeDb()
//	{
//		dbHelper.close();
//	}
//}
