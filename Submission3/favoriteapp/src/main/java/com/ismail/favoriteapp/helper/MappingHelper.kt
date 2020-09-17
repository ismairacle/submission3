package com.ismail.favoriteapp.helper

import android.database.Cursor
import com.ismail.favoriteapp.data.DatabaseContract
import com.ismail.favoriteapp.entity.Favorite

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

}