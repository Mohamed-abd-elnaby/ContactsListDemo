package com.example.halatask.base

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.halatask.BuildConfig
import com.example.halatask.R
import com.example.halatask.base.customViews.CustomProgressPar
import java.io.ByteArrayOutputStream


/**
 * Created by Fahmy on 16/April/2019
 */


private var progressBar: CustomProgressPar? = null
fun Activity.showLoading() {
    MainApp.mainApp.handler.post {
        try {
            progressBar?.cancel()
            progressBar?.dismiss()
            progressBar = CustomProgressPar(this)
            progressBar?.setCancelable(false)
            progressBar?.show()
        } catch (ex: Exception) {
            if (BuildConfig.DEBUG)
                ex.printStackTrace()
        }
    }
}

fun RecyclerView.ViewHolder.loadImage(
    context: Context,
    url: String,

    callback: ((ByteArray?) -> Unit)? = null
) {
    Glide.with(context).load(url).placeholder(R.drawable.ic_baseline_image_24)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                callback?.let { it(null) }
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                val bitmap = (resource as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val bitmapData: ByteArray = stream.toByteArray()
                callback?.let { it(bitmapData) }
                return true
            }
        }).submit()
}

fun Fragment.showLoading() {
    requireActivity().showLoading()
}

fun hideLoading() {
    MainApp.mainApp.handler.post {
        progressBar?.cancel()
        progressBar?.dismiss()
    }


}

fun setCanClickable(canClickable: Boolean) {
    MainApp.mainApp.handler.post {
        progressBar?.setCancelable(canClickable)
    }
}

fun isInternetConnected(): Boolean {
    return isInternetAvailable(MainApp.mainApp.applicationContext)
}

fun isInternetAvailable(context: Context): Boolean {
    val result: Boolean
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
        else -> false
    }


    return result
}


fun Fragment.requestPermission(permission: Array<String>): Boolean {
    var iSGranted = true
    permission.forEach {
        if (!checkPermissionPrivate(it, requireActivity()))
            iSGranted = false
    }
    if (!iSGranted) {
        ActivityCompat.requestPermissions(requireActivity(), permission, Utility.PermissionCode)

    }
    return iSGranted

}


private fun checkPermissionPrivate(permission: String, activity: Activity): Boolean {
    return ContextCompat.checkSelfPermission(
        activity,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}


fun disableEditText(editText: EditText) {
    disableEditTextPrivate(editText)
}

fun disableEditTextPrivate(editText: EditText) {
    editText.isFocusable = false
    editText.isEnabled = false
    editText.isCursorVisible = false
    editText.keyListener = null
    editText.setBackgroundColor(Color.TRANSPARENT)
}