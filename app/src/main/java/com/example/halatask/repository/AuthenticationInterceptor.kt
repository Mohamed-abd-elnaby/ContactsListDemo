package com.example.halatask.repository

import com.example.halatask.R
import com.example.halatask.base.MainApp
import com.example.halatask.base.ManagerSharedPreference

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

class AuthenticationInterceptor : Interceptor {
    //toDo need to be configure with network
    private val credentials = MainApp.mainApp.getString(R.string.apiKey)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val newReq: Request
        val currentURL = oldRequest.url
        val newUrl = currentURL.newBuilder()
            .addQueryParameter("culture", ManagerSharedPreference.instance.getInterceptorLan())
            .build()
        newReq = oldRequest.newBuilder()
            .addHeader("ApiKey", credentials)
            .url(newUrl)
            .build()
        return try {
            chain.proceed(newReq)
        } catch (e: IOException) {
            Response.Builder()
                .code(418)
                .body("{${e}}".toResponseBody(null))
                .request(newReq)
                .message(e.localizedMessage ?: e.toString())
                .protocol(Protocol.HTTP_1_1)
                .build()
        }


    }

}


