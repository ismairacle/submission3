package com.ismail.favoriteapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var avatar: String,
    var username: String
) : Parcelable