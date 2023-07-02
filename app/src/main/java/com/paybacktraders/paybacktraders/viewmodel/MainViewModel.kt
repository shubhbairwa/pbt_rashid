package com.paybacktraders.paybacktraders.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paybacktraders.paybacktraders.apihelper.Resource
import com.paybacktraders.paybacktraders.api.Apis
import com.paybacktraders.paybacktraders.apihelper.Event
import com.paybacktraders.paybacktraders.model.model.apiresponse.ResponseLogin
import com.paybacktraders.paybacktraders.repository.MainRepos
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.HashMap

class MainViewModel(
    val app: Application,
    private val repos: MainRepos,
    private val dispatchers: CoroutineDispatcher = Dispatchers.Main,
    val fanxApi: Apis
) : AndroidViewModel(app) {

    private val _login = MutableLiveData<Event<Resource<ResponseLogin>>>()
    val login: LiveData<Event<Resource<ResponseLogin>>> = _login







    fun doLogin(data: HashMap<String, Any>) {
        _login.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.doLogin(data)
            _login.postValue(Event(result))
        }
    }


    private fun hasInternetConnection(): Boolean { // you can check anywhere by this method
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetworkState = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetworkState) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }


        }
        connectivityManager.activeNetworkInfo?.run {
            return when (type) {
                TYPE_WIFI -> true
                TYPE_MOBILE -> true
                TYPE_ETHERNET -> true
                else -> false

            }


        }
        return false
    }


}

private const val TAG = "MainViewModel"


