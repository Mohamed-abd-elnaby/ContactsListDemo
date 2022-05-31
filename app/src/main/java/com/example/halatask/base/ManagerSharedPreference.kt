package com.example.halatask.base

import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

class ManagerSharedPreference() {
    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null
    private val ShareName = "HAla"
    private val LANG = "LANG"
    var lang: String
        get() = sharedPreferences.getString(LANG, Locale.getDefault().language)
            ?: Locale.getDefault().language
        set(language) {
            editor?.putString(LANG, language)
            editor?.commit()
        }

    fun getInterceptorLan(): String {
        return when (lang) {
            "ar" -> "ar-SA"
            "in" -> "id-ID"
            "fr" -> "fr-LU"
            "fa" -> "fa-IR"
            "ur" -> "ur-PK"
            "tr" -> "tr-TR"
            else -> "en-US"
        }
    }

    fun isArabic(): Boolean {
        return lang == "ar"
    }


    fun removeAll() {
        editor?.clear()
        editor?.commit()
    }

    init {
        sharedPreferences =
            MainApp.mainApp.getSharedPreferences(ShareName, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    companion object {
        var instance = ManagerSharedPreference()
    }

}
