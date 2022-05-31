package com.example.halatask.base.koinConfigration

import com.example.halatask.base.CommonStates
import com.example.halatask.base.MainRepository
import com.example.halatask.base.SingleLiveEvent
import com.example.halatask.repository.BaseClientApi
import com.example.halatask.repository.OfflineSourceHelper
import com.example.halatask.repository.RepositoryClient
import com.example.halatask.viewmodels.ContactViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


object KoinModules {
    private val networkModule = module {
        single { OfflineSourceHelper }
        single { BaseClientApi(get()) }
        single { RepositoryClient(get(), get()) }
        factory { (states: SingleLiveEvent<CommonStates<*>>?) -> MainRepository(get(), states) }
    }
    private val viewModuleModule = module {
        viewModel { ContactViewModel() }
    }

    fun getListModules(): List<Module> {
        return listOf(networkModule, viewModuleModule)
    }
}