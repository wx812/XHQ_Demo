package com.xhq.demo.db.db_sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xhq.demo.db.SQLParam;
import com.xhq.demo.db.db_sql.entityConfig.ModelConfig.ModelEntityFactory;
import com.xhq.demo.db.db_sql.interf.IDBCallback;
import com.xhq.demo.db.db_sql.interf.IDbWorker;
import com.xhq.demo.tools.fileTools.FileUtils;
import com.xhq.demo.tools.fileTools.StorageUtils;

import java.io.File;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{
    private static SQLiteDatabase readdb;
    private static SQLiteDatabase writedb;
    private static SQLiteDatabase db;
    private static final String APPDIR = "JuJia";
    private static final String DEFAULT_DATABASE_NAME = "user.db";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //初始化数据库
    public static void init(String uc) throws Exception{
        String sdDir = StorageUtils.getSDPath();
        if (!sdDir.equals("")) {
            //创建该APP的目录
            FileUtils.makeDirs(sdDir + "/" + APPDIR + "/" + uc);
        }

        String finNme = sdDir + "/" + APPDIR + "/" + uc + "/";
        String dbFileName = finNme + DEFAULT_DATABASE_NAME;
        FileUtils.makeFile(dbFileName);

        File file = new File(finNme + "/" + DEFAULT_DATABASE_NAME);
        if (!file.exists())
            file.mkdir();
        String fileName =  finNme + "/" + DEFAULT_DATABASE_NAME;

        db = SQLiteDatabase.openOrCreateDatabase(fileName, null);
        readdb = SQLiteDatabase.openDatabase(fileName, null, SQLiteDatabase.OPEN_READONLY);
        writedb = SQLiteDatabase.openDatabase(fileName, null, SQLiteDatabase.OPEN_READWRITE);
        //
        List<String> sqlList = ModelEntityFactory.getInstance().buildCreateSql();
        for (String createSql : sqlList) {
            writedb.execSQL(createSql);//插入表
        }
        ModelEntityFactory.getInstance().updateTableVersion();
    }

    public static void initEx(String fileName) {

        db = SQLiteDatabase.openOrCreateDatabase(fileName, null);
        readdb = SQLiteDatabase.openDatabase(fileName, null, SQLiteDatabase.OPEN_READONLY);
        writedb = SQLiteDatabase.openDatabase(fileName, null, SQLiteDatabase.OPEN_READWRITE);

    }

    //关闭数据库连接
    public static void closeDatabase(){
        db.close();
        readdb.close();
        writedb.close();
    }

    //事务代码
    public static void doTransaction(IDbWorker worker) throws Exception{
        writedb.beginTransaction();
        try {
            worker.work();
            writedb.setTransactionSuccessful();
        } finally {
            writedb.endTransaction();//结束事务,有两种情况：commit,rollback,
            //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
        }
    }

    //查询
    public static <T> T query(String sql, final IDBCallback rse, Object... paras) throws Exception{
        //noinspection unchecked
        String[] a = new String[paras.length];
        for (int i = 0; i < paras.length; i++) {
            a[i] = paras[i].toString();
        }
        Cursor cursor = readdb.rawQuery(sql, a);
        UtilCursor uc = new UtilCursor();
        uc.rs = cursor;
        try {
            return (T) rse.extractData(uc);
        } finally {
            cursor.close();
        }
    }

    //更新
    public static void update(SQLParam sqlParam){
        if (!sqlParam.hasLob)
            writedb.beginTransaction();
        try {
            writedb.execSQL(sqlParam.sql, sqlParam.paras);
            writedb.setTransactionSuccessful();
        } finally {
            writedb.endTransaction();//结束事务,有两种情况：commit,rollback,
            //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
        }
    }

    public static void update(String sql, Object... paras){
        writedb.beginTransaction();
        try {
            writedb.execSQL(sql, paras);
            writedb.setTransactionSuccessful();
        } finally {
            writedb.endTransaction();//结束事务,有两种情况：commit,rollback,
            //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
        }
    }

    /**
     * 批量执行同一sql
     *
     * @param s     待执行sql
     * @param paras 参数组
     *              //     * @param types 参数类别
     */
    public static void updateBatchSameSql(String s, Object[] paras){
        writedb.beginTransaction();
        try {
            for (Object para : paras) {
                writedb.execSQL(s, paras);
            }
            writedb.setTransactionSuccessful();
        } finally {
            writedb.endTransaction();//结束事务,有两种情况：commit,rollback,
            //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
        }
    }

    /**
     * 判断传入的sql能否查询出数据；
     *
     * @param sql 查询sql
     * @return 如果有数据存在则返回 true,否则=false
     */
    public static boolean recordIsExists(String sql, Object... o) throws Exception{
        IDBCallback<Boolean> rse = new IDBCallback<Boolean>() {
            public Boolean extractData(UtilCursor rs){
                return (rs.next());
            }
        };
        return (Boolean) query(sql, rse, o);
    }

    //查询记录级中的记录数量
    public static int queryCount(String sql, Object... params) throws Exception{
        String sqlCount = "SELECT COUNT(0) count FROM(" + sql + ") AAA";
        IDBCallback<Integer> rse = new IDBCallback<Integer>() {
            public Integer extractData(UtilCursor rs){
                rs.next();
                return rs.getIntNotException("count");
            }
        };
        return (Integer) query(sqlCount, rse, params);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//         readdb = getReadableDatabase();
//         writedb = getReadableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public static void main(String [] args){
//        initEx("D:\\user(1).db");
//        DbEngine.query("select * from ")
//    }
}
