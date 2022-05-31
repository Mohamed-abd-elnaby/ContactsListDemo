package com.example.halatask.repository

import com.example.halatask.base.Utility
import com.example.halatask.entities.users.ContactsResponse
import com.google.gson.Gson
import retrofit2.Response

object OfflineSourceHelper {
    fun getContactList(): Response<ContactsResponse> {
        val response = Utility.getJsonDataFromAsset("contacts.json")
        val contactsResponse = Gson().fromJson(response, ContactsResponse::class.java)
        return Response.success(200, contactsResponse)
    }
}