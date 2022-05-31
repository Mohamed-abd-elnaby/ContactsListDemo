package com.example.halatask.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.halatask.BuildConfig
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


object Utility {
    const val PermissionCode = 601
    const val networkSource = 1
    const val offlineSource = 2
    const val logInitialFailed = "Initial Failed"
    fun dp(context: Context, value: Int): Int {
        return ceil((context.resources.displayMetrics.density * value).toDouble()).toInt()
    }

    fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString =
                MainApp.mainApp.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: Exception) {
            if (BuildConfig.DEBUG)
                ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun parsingDate(dateFromServer: String): String {
        return try {
            val format = SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH)
            val date = format.parse(dateFromServer)

            //            String date = dateFromServer.replace("/Date(", "").replace(")/", "");
            //            Date d = new Date(Long.valueOf(dateFromServer));
            val pattern = "dd/MM/yyyy"
            val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
            dateFormat.format(date ?: Date())

        } catch (ex: Exception) {
            if (BuildConfig.DEBUG)
                ex.printStackTrace()
            ""
        }

    }

    fun isEmailValid(email: String): Boolean {
        return (email.isNotEmpty() && email.contains('@', true))
    }

    fun isPhoneValid(phone: String): Boolean {
        return phone.isNotEmpty() && (phone.length == 11 ||
                phone.length == 14)
    }

    fun isPasswordValid(password: String): Boolean {
        return (password.isNotEmpty() && password.length > 4)
    }


    fun parsingDateSpecial(dateFromServer: String): String {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH)
            val date =
                dateFormat.parse(dateFromServer)//You will get date object relative to server/client timezone wherever it is parsed
            val formatter =
                SimpleDateFormat(
                    "dd MMM hh:mm",
                    Locale.ENGLISH
                ) //If you need time just put specific format for time like 'HH:mm:ss'
            formatter.format(date ?: Date())

        } catch (ex: Exception) {
            if (BuildConfig.DEBUG)
                ex.printStackTrace()
            ""
        }

    }

    fun hideSoftKeyboard(activity: Activity) {
        val weakReference = WeakReference(activity)
        val inputMethodManager =
            weakReference.get()
                ?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus
        if (currentFocus != null)
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    fun showSoftKeyboard(activity: Activity, view: View) {
        val weakReference = WeakReference(activity)
        val inputMethodManager =
            weakReference.get()
                ?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun updateResources(
        context: Context,
        language: String
    ): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration =
            context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

}
