package com.ismail.submission3.helper

import android.database.Cursor
import android.provider.ContactsContract
import com.ismail.submission3.data.DatabaseContract
import com.ismail.submission3.entity.Favorite

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        notesCursor?.apply {
            while (moveToNext()) {
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                favoriteList.add(Favorite(avatar, username))
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(cursor: Cursor): Favorite {
        var favorite: Favorite
        cursor.apply {
            moveToFirst()
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            favorite = Favorite(avatar, username)
        }
        return favorite
    }
}