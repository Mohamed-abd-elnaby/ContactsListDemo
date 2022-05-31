package com.example.halatask.entities.users

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserContact(
    val avatar: String?,
    val bio: String?,
    val birth_day: String?,
    val city: String?,
    val contact: Contact?,
    val country: String?,
    val education: String?,
    val first_name: String?,
    val gender: String?,
    val id: Int = 0,
    val job: Job?,
    val last_name: String?,
    val pet_animal: String?,
    var image: ByteArray? = null
) : Parcelable {
    fun getFullName(): String {
        return "$first_name $last_name"
    }
}