package com.ismail.favoriteapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var avatar: String,
    var name: String,
    var username: String,
    var company: String,
    var location: String,
    var repository: String,
    var following: String,
    var followers: String
) : Parcelable