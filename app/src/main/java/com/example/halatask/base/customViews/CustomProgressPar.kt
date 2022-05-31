package com.example.halatask.base.customViews

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.halatask.R
import com.example.halatask.base.MainApp
import com.example.halatask.databinding.CustomProgressbarBinding


/**
 * Created by fahmy on 17/April/2019
 */
class CustomProgressPar(context: Context) : AlertDialog(context) {
    private lateinit var bind: CustomProgressbarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = CustomProgressbarBinding.inflate(layoutInflater)
        window.takeIf { it != null }?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(bind.root)
        MainApp.mainApp.handler.post {
            val rotate = AnimationUtils.loadAnimation(context, R.anim.rotate)
            bind.animationView.startAnimation(rotate)
        }
    }


}