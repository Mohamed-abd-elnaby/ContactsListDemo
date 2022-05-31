package com.example.halatask.base

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.halatask.BuildConfig
import com.example.halatask.base.koinConfigration.KoinModules
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level


class MainApp : Application() {
    lateinit var gson: Gson
        private set
    lateinit var handler: Handler
    var pageName = MutableLiveData<String>()

    override fun onCreate() {
        super.onCreate()
        initializeClass()
        mainApp = this
        handler = Handler(Looper.getMainLooper())
        gson = Gson()
    }

    private fun initializeClass() {

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MainApp)
            modules(KoinModules.getListModules())
        }

    }


    fun msg(msg: String) {
        handler.post {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        lateinit var mainApp: MainApp

    }


}

