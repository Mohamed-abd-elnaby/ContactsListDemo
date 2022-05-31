package com.example.halatask.repository

import com.example.halatask.entities.users.ContactsResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

interface EndPoint {
    @GET("GetContacts")
    suspend fun getContactList(): Response<ContactsResponse>


}