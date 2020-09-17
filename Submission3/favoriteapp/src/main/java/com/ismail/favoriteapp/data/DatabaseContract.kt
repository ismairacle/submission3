package com.ismail.favoriteapp.data

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {

            private const val AUTHORITY = "com.ismail.submission3"
            private const val SCHEME = "content"

            private const val TABLE_NAME = "favorite_table"
            const val AVATAR = "avatar_url"
            const val USERNAME = "login"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}