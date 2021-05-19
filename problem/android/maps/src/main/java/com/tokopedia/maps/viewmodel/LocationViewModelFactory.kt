package com.tokopedia.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.maps.domain.ILocationUseCase

class LocationViewModelFactory(
    private val locationUseCase: ILocationUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ILocationUseCase::class.java)
            .newInstance(locationUseCase)
    }
}