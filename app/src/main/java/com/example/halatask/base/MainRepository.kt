package com.example.halatask.base


import com.example.halatask.repository.RepositoryClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Created by mohamed abd elnaby on 16/April/2020
 */

class MainRepository(
    var repositoryClient: RepositoryClient,
    var states: SingleLiveEvent<CommonStates<*>>?
) {
    fun fetchData(
        showProgress: Boolean = true,
        isMore: Boolean = false,
        worker: suspend CoroutineScope.() -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (isInternetConnected()) {
                    if (showProgress && !isMore)
                        MainApp.mainApp.handler.post {
                            (states as SingleLiveEvent<CommonStates<*>>).value =
                                CommonStates.LoadingShow
                        }
                    else if (isMore)
                        MainApp.mainApp.handler.post {
                            (states as SingleLiveEvent<CommonStates<*>>).value =
                                CommonStates.LoadingShowMore
                        }
                    worker()

                } else {
                    MainApp.mainApp.handler.post {
                        (states as SingleLiveEvent<CommonStates<*>>).value = CommonStates.NoInternet
                    }
                }
            } catch (ex: Exception) {
                MainApp.mainApp.handler.post {
                    (states as SingleLiveEvent<CommonStates<*>>).value =
                        CommonStates.Error(418, ex.message ?: ex.localizedMessage ?: ex.toString())
                }
            }
        }
    }

    fun <T> handleResponse(
        response: Response<T>,
        isMore: Boolean? = false,
        callBackSuccess: (response: T) -> Unit,
        callBackFailed: ((Response<T>) -> Unit)? = null,
    ) {
        MainApp.mainApp.handler.post {
            if (isMore == true)
                (states as SingleLiveEvent<CommonStates<*>>).value =
                    CommonStates.Success(CommonStates.HideLoadingMore)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    callBackSuccess(body)
                } else {
                    (states as SingleLiveEvent<CommonStates<*>>).value =
                        CommonStates.Error(response.code(), response.errorBody()?.string())
                }
            } else {
                if (callBackFailed != null)
                    callBackFailed(response)
                else
                    (states as SingleLiveEvent<CommonStates<*>>).value =
                        CommonStates.Error(response.code(), response.errorBody()?.string())
            }
        }

    }
}


