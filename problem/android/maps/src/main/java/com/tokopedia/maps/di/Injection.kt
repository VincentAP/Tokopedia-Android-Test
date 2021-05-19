package com.tokopedia.maps.di

import com.tokopedia.maps.api.LocationService
import com.tokopedia.maps.data.LocationRepositoryImpl
import com.tokopedia.maps.di.module.NetworkModule
import com.tokopedia.maps.domain.ILocationRepository
import com.tokopedia.maps.domain.ILocationUseCase
import com.tokopedia.maps.domain.LocationInteractor

object Injection {

    private fun injectLocationService(): LocationService =
        NetworkModule.provideLocationService()

    fun injectLocationRepository(): ILocationRepository =
        LocationRepositoryImpl(injectLocationService())

    fun injectLocationUseCase(): ILocationUseCase =
        LocationInteractor(injectLocationRepository())
}