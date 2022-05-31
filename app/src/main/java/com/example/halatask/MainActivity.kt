package com.example.halatask

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.halatask.base.MainApp
import com.example.halatask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var hostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        hostFragment = supportFragmentManager.findFragmentById(R.id.homeNavHost) as NavHostFragment
        navController = hostFragment.navController
        bind.btnBack.setOnClickListener {
            onBackPressed()
        }
        MainApp.mainApp.pageName.observeForever { pageName ->
            bind.tvPageName.text = pageName
            when (pageName) {
                getString(R.string.listScreenName) -> {
                    bind.btnBack.visibility = View.INVISIBLE
                    bind.tvPageName.visibility = View.VISIBLE
                }
                getString(R.string.contactDetailsScreenName) -> {
                    bind.btnBack.visibility = View.VISIBLE
                    bind.tvPageName.visibility = View.INVISIBLE
                }
            }
        }
    }


}