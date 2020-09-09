package com.ismail.submission3.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.ismail.submission3.data.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.ismail.submission3.data.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.ismail.submission3.data.DatabaseContract.FavoriteColumns.Companion.USERNAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbfavorite"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$AVATAR TEXT NOT NULL," +
                "$USERNAME TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}