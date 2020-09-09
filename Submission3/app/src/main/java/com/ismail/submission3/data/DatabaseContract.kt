package com.ismail.submission3.data

import android.provider.BaseColumns

class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_table"
            const val AVATAR = "avatar_url"
            const val USERNAME = "login"
        }
    }
}