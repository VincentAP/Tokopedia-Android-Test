package com.tokopedia.maps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.maps.domain.ILocationUseCase
import com.tokopedia.maps.model.LocationResponse
import com.tokopedia.maps.util.Resource

class MapsViewModel(
    private val locationUseCase: ILocationUseCase
) : ViewModel() {

    private val _locationItem: MutableLiveData<Resource<List<LocationResponse>>> = MutableLiveData()
    val locationItem: LiveData<Resource<List<LocationResponse>>> = _locationItem

    fun getCountryDetail(country: String) {
        _locationItem.value = Resource.loading(null)
        locationUseCase.getCountryDetail(
            country,
            onSuccess = {
                if (it.isEmpty()) _locationItem.value = Resource.empty(null)
                else _locationItem.value = Resource.success(it)
            },
            onError = {
                _locationItem.value = Resource.error(it, null)
            }
        )
    }
}