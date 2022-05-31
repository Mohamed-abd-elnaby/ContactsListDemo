package com.example.halatask.base

import com.example.halatask.entities.users.ContactsResponse

sealed class CommonStates<out T> {
    object LoadingShow : CommonStates<Nothing>()
    object HidingShow : CommonStates<Nothing>()
    object NoInternet : CommonStates<Nothing>()
    object EmptyState : CommonStates<Nothing>()
    object LoadingShowMore : CommonStates<Nothing>()
    object HideLoadingMore : CommonStates<Nothing>()
    object SessionMayExpired : CommonStates<Nothing>()
    data class Success<out R>(val data: R) : CommonStates<R>()
    data class Error(val code: Int, val exp: String?) : CommonStates<Nothing>()
}

sealed class ContactStates {
    data class GetContactSuccess(val data: ContactsResponse) : ContactStates()
}
