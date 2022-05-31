package com.example.halatask.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData


abstract class BaseFragment : Fragment(), (Any?) -> Unit {
    private var viewItem: View? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionCallback?.let { it(true) }
        } else {
            permissionCallback?.let { it(false) }
        }
    }
    var permissionCallback: ((isGranted: Boolean) -> Unit)? = null
    fun requestPermission(
        permission: String,
        message: String?,
        permissionCallback: ((isGranted: Boolean) -> Unit)? = null
    ) {
        this.permissionCallback = {
            permissionCallback?.let { t -> t(it) }
            this.permissionCallback = null
            if (!it)
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission
                    )
                ) {
                    if (!message.isNullOrEmpty())
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
                            .show()
                }
        }

        if (checkPermission(permission))
            permissionCallback?.let { t -> t(true) }
        else
            requestPermissionLauncher.launch(permission)
    }

    fun requestPermission(
        permission: String,
        permissionCallback: ((isGranted: Boolean) -> Unit)? = null
    ) {
        requestPermission(permission, null, permissionCallback)
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewItem = getInflateView(inflater, container)
        return if (viewItem == null) {
            Log.e(
                Utility.logInitialFailed,
                "Failed initial view you must initial your view in child fragment or activity "
            )
            null
        } else
            viewItem
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialComponent()
        clicks()
        getStates().forEach { data ->
            data.observe(viewLifecycleOwner) { it1 ->
                if (it1 is CommonStates<*>)
                    handleResponse(it1) {
                        this(it)
                    }
            }
        }
        MainApp.mainApp.pageName.value = getPageName()
    }


    abstract fun initialComponent()
    abstract fun getPageName(): String
    abstract fun clicks()
    abstract fun getInflateView(inflater: LayoutInflater, container: ViewGroup?): View
    abstract fun getStates(): List<LiveData<*>>

    private fun handleResponse(response: CommonStates<*>, result: (Any?) -> Unit) {
        when (response) {
            is CommonStates.LoadingShow -> {
                showLoading()
                setCanClickable(false)
            }
            is CommonStates.HidingShow -> {
                hideLoading()
            }
            is CommonStates.NoInternet -> {
                hideLoading()
                //handle Network connection error here

            }
            is CommonStates.Error -> {
                hideLoading()
                //handle error message here
            }
            is CommonStates.Success -> {
                hideLoading()
                result(response.data)
            }
            is CommonStates.EmptyState -> {
                hideLoading()
                showEmptyView()
            }
            else -> {}
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        getStates().forEach {
            it.removeObserver(this)
        }

    }

    open fun showEmptyView() {
        hideLoading()
    }


}