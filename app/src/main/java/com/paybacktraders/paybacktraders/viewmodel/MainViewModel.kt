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
import com.paybacktraders.paybacktraders.model.model.apirequestbody.*
import com.paybacktraders.paybacktraders.model.model.apiresponse.*
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

    private val _clientAll = MutableLiveData<Event<Resource<ResponseClient>>>()
    val clientAll: LiveData<Event<Resource<ResponseClient>>> = _clientAll

    private val _employeeAll = MutableLiveData<Event<Resource<ResponseEmployeeAll>>>()
    val employeeAll: LiveData<Event<Resource<ResponseEmployeeAll>>> = _employeeAll

    private val _contactUsAll = MutableLiveData<Event<Resource<ResponseContactUs>>>()
    val contactUsAll: LiveData<Event<Resource<ResponseContactUs>>> = _contactUsAll

    private val _statusClientRemark = MutableLiveData<Event<Resource<ResponseClientStatusRemark>>>()
    val statusClientRemark: LiveData<Event<Resource<ResponseClientStatusRemark>>> = _statusClientRemark

    private val _brokerAll = MutableLiveData<Event<Resource<ResponseBrokerAll>>>()
    val brokerAll: LiveData<Event<Resource<ResponseBrokerAll>>> = _brokerAll


    private val _addDist = MutableLiveData<Event<Resource<ResponseGlobal>>>()
    val addDist: LiveData<Event<Resource<ResponseGlobal>>> = _addDist

    private val _updateDist = MutableLiveData<Event<Resource<ResponseGlobal>>>()
    val updateDist: LiveData<Event<Resource<ResponseGlobal>>> = _updateDist


    private val _productAll = MutableLiveData<Event<Resource<ProductResponse>>>()
    val productAll: LiveData<Event<Resource<ProductResponse>>> = _productAll
    private val _updateCustomerStatus = MutableLiveData<Event<Resource<ResponseLogin>>>()
    val updateCustomerStatus: LiveData<Event<Resource<ResponseLogin>>> = _updateCustomerStatus

    private val _addProduct = MutableLiveData<Event<Resource<ResponseGlobal>>>()
    val addProduct: LiveData<Event<Resource<ResponseGlobal>>> = _addProduct

    private val _updateProduct = MutableLiveData<Event<Resource<ResponseGlobal>>>()
    val updateProduct: LiveData<Event<Resource<ResponseGlobal>>> = _updateProduct


    private val _dashBOardData = MutableLiveData<Event<Resource<DashBoardResponse>>>()
    val dashBOardData: LiveData<Event<Resource<DashBoardResponse>>> = _dashBOardData

    private val _clientFilter = MutableLiveData<Event<Resource<ResponseClient>>>()
    val clientFilter: LiveData<Event<Resource<ResponseClient>>> = _clientFilter







    fun doLogin(data: HashMap<String, Any>) {
        _login.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.doLogin(data)
            _login.postValue(Event(result))
        }
    }

    fun getDistributor(data: HashMap<String, Any>) {
        _employeeAll.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getDistributor(data)
            _employeeAll.postValue(Event(result))
        }
    }

    fun getCustomerStatus(data: HashMap<String, Any>) {
        _statusClientRemark.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getCustomerStatus(data)
            _statusClientRemark.postValue(Event(result))
        }
    }

    fun getBrokerAll() {
        _brokerAll.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getBrokerAll()
            _brokerAll.postValue(Event(result))
        }
    }

    fun updateCustomerStatus(data: BodyClientStatus) {
        _updateCustomerStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.updateCustomerStatus(data)
            _updateCustomerStatus.postValue(Event(result))
        }
    }


    fun addProduct(data: BodyForAddProduct) {
        _addProduct.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.addProduct(data)
            _addProduct.postValue(Event(result))
        }
    }

    fun updateProduct(data: BodyForUpdateProduct) {
        _updateProduct.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.updateProduct(data)
            _updateProduct.postValue(Event(result))
        }
    }

    fun addDistributor(data: BodyAddDistributor) {
        _addDist.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.addDistributor(data)
            _addDist.postValue(Event(result))
        }
    }

    fun updateDistributor(data: BodyUpdateDistributor) {
        _updateDist.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.updateDistributor(data)
            _updateDist.postValue(Event(result))
        }
    }

    fun getProductAllFilter(data: HashMap<String, Any>) {
        _productAll.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getProductALlFilter(data)
            _productAll.postValue(Event(result))
        }
    }

    fun getClientALlFilter(data: HashMap<String, Any>) {
        _clientFilter.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getClientALlFilter(data)
            _clientFilter.postValue(Event(result))
        }
    }

    fun getDashboardData(data: HashMap<String, Any>) {
        _dashBOardData.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getDashboardData(data)
            _dashBOardData.postValue(Event(result))
        }
    }

    fun getClientAll() {
        _clientAll.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getClientAll()
            _clientAll.postValue(Event(result))
        }
    }

    fun getEmployeeAll() {
        _employeeAll.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getEmployeeAll()
            _employeeAll.postValue(Event(result))
        }
    }


    fun getcontactusall() {
        _contactUsAll.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getcontactusall()
            _contactUsAll.postValue(Event(result))
        }
    }



    fun getProductAll() {
        _productAll.postValue(Event(Resource.Loading()))
        viewModelScope.launch(Dispatchers.Main) {
            val result = repos.getProductAll()
            _productAll.postValue(Event(result))
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


