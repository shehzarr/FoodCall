package com.example.foodcall.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodcall.R;
import com.example.foodcall.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DB_Helper extends OrmLiteSqliteOpenHelper {

    private static final String DataBase_Name = "User.db";
    private static int DataBase_Version = 3;
    private SQLiteDatabase sqLiteDatabase;

    private Dao<User, Integer> contactDao = null;
    private RuntimeExceptionDao<User, Integer> contactRuntimeDao = null;

    public DB_Helper(Context context) {
        super(context, DataBase_Name, null, DataBase_Version, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<User, Integer> getContactDao() throws SQLException {
        if (contactDao == null) {
            contactDao = getDao(User.class);
        }
        return contactDao;
    }

    public RuntimeExceptionDao<User, Integer> getContactRuntimeDao() {
        if (contactRuntimeDao == null) {
            contactRuntimeDao = getRuntimeExceptionDao(User.class);
        }
        return contactRuntimeDao;
    }
}
