package com.example.inventoryapplication.common.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database manager
 *
 * @author cong-pv
 * @since 2019-03-05
 */

public class DatabaseManagerUtil {

    /**
     * Open counter
     */
    private boolean mOpenCounter = false;

    /**
     * Instance of Database manager
     */
    private static DatabaseManagerUtil instance;
    /**
     * SQLite helper
     */
    private static SQLiteOpenHelper mDatabaseHelper;
    /**
     * SQLite database
     */
    private SQLiteDatabase mDatabase;

    /**
     * Init
     */
    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {

        if (instance == null) {
            instance = new DatabaseManagerUtil();
            mDatabaseHelper = helper;
        }
    }

    /**
     * Get instance
     */
    public static synchronized DatabaseManagerUtil getInstance() {

        if (instance == null) {
            throw new IllegalStateException(DatabaseManagerUtil.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    /**
     * Open database
     */
    public synchronized SQLiteDatabase openDatabase() {

        if (!mOpenCounter) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
            mOpenCounter = true;
        }
        return mDatabase;
    }

    /**
     * Close database
     */
    public synchronized void closeDatabase() {

        if (mOpenCounter) {
            // Closing database
            mDatabase.close();
            mOpenCounter = false;
        }
    }
}
