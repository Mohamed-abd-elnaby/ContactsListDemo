package com.example.halatask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.halatask.base.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class ContactViewModel : ViewModel(), KoinComponent {
    private var _states = SingleLiveEvent<CommonStates<ContactStates>>()
    var states: LiveData<CommonStates<ContactStates>> = _states
    private val mainRepository: MainRepository by inject { parametersOf(_states) }
    fun getContactList() {
        mainRepository.apply {
            fetchData {
                handleResponse(
                    repositoryClient.getContactList(Utility.offlineSource), callBackSuccess = {
                        states?.value = CommonStates.Success(ContactStates.GetContactSuccess(it))
                    })
            }
        }
    }
}