package com.example.halatask.repository

import com.example.halatask.base.Utility
import com.example.halatask.entities.users.ContactsResponse
import retrofit2.Response


/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

class RepositoryClient(baseClient: BaseClientApi, var offlineHelper: OfflineSourceHelper) {
    private val endPoint =
        baseClient.getRetrofitClient().create(EndPoint::class.java)

    suspend fun getContactList(source: Int): Response<ContactsResponse> {
        return when (source) {
            Utility.networkSource -> endPoint.getContactList()
            Utility.offlineSource -> offlineHelper.getContactList()
            else -> {
                return Response.error(418, null)
            }
        }
    }

}