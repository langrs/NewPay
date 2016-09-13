package com.unioncloud.newpay.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.unioncloud.newpay.data.entity.notice.NoticeEntity;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by cwj on 16/9/12.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String TABLE_NAME = "new_pay.db";
    private static int DB_VERSION = 1;

    private static DatabaseHelper instance;

    private HashMap<String, Dao> daoMap = new HashMap<String, Dao>();

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context.getApplicationContext(), TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource,
                    NoticeEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int
            oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NoticeEntity.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daoMap.containsKey(className)) {
            dao = daoMap.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daoMap.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        daoMap.clear();
    }
}
