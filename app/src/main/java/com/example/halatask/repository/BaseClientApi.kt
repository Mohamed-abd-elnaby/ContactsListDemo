package com.example.halatask.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.halatask.R
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

class BaseClientApi(var context: Context) {
    private val base_url = context.getString(R.string.apiUrl)
    private val writeTimeOut = 120L
    private val readTimeOut = 120L
    private val timeOutUnit = TimeUnit.SECONDS

    fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .client(UnsafeOkHttpClient().getUnsafeOkHttpClient(false))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val gson: Gson
        get() = GsonBuilder()
            .setLenient()
            .create()

    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }


    inner class UnsafeOkHttpClient {
        fun getUnsafeOkHttpClient(isGis: Boolean): OkHttpClient {
            return try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                    @SuppressLint("CustomX509TrustManager")
                    object : X509TrustManager {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate?> {
                            return arrayOf()
                        }
                    }
                )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                builder.callTimeout(2, TimeUnit.MINUTES)
                builder.writeTimeout(2, TimeUnit.MINUTES)
                builder.readTimeout(2, TimeUnit.MINUTES)
                builder.connectTimeout(2, TimeUnit.MINUTES)
                builder.sslSocketFactory(
                    sslSocketFactory,
                    (trustAllCerts[0] as X509TrustManager)
                )
                builder.hostnameVerifier { _, _ -> true }
                if (BuildConfig.DEBUG)
                    builder.addInterceptor(loggingInterceptor)
                if (!isGis)
                    builder.addInterceptor(AuthenticationInterceptor())
                builder.writeTimeout(writeTimeOut, timeOutUnit)
                builder.readTimeout(readTimeOut, timeOutUnit)
                builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }


    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FIELD)
    annotation class Exclude

    class AnnotationExclusionStrategy : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes): Boolean {
            return f.getAnnotation(Exclude::class.java) != null
        }

        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
            return false
        }
    }

}
