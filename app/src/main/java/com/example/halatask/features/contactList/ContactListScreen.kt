package com.example.halatask.features.contactList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.example.halatask.R
import com.example.halatask.base.BaseFragment
import com.example.halatask.base.ContactStates
import com.example.halatask.databinding.ContactListBinding
import com.example.halatask.viewmodels.ContactViewModel
import org.koin.android.ext.android.inject

class ContactListScreen : BaseFragment() {
    private lateinit var bind: ContactListBinding
    private val contactViewModel: ContactViewModel by inject()
    private val contactsAdapter = ContactsAdapter()
    override fun initialComponent() {
        bind.rvContacts.adapter = contactsAdapter
        contactViewModel.getContactList()
    }

    override fun clicks() {

    }

    override fun getPageName(): String {
        return getString(R.string.listScreenName)
    }

    override fun getInflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        bind = ContactListBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun getStates(): List<LiveData<*>> {
        return listOf(contactViewModel.states)
    }

    override fun invoke(p1: Any?) {
        if (p1 is ContactStates)
            handleSuccessResponse(p1)
    }

    private fun handleSuccessResponse(response: ContactStates) {
        when (response) {
            is ContactStates.GetContactSuccess -> {
                response.data.forEach {
                    contactsAdapter.addNewContact(it)
                }
            }
        }
    }
}