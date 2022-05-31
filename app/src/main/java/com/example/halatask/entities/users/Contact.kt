package com.example.halatask.entities.users

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val address: String?,
    val email: String?,
    val phone_number: String?
) : Parcelable