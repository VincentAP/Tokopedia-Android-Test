package com.tokopedia.maps.domain

import com.tokopedia.maps.model.LocationResponse

class LocationInteractor(
    private val locationRepository: ILocationRepository
) : ILocationUseCase {

    override fun getCountryDetail(
        country: String,
        onSuccess: (t: List<LocationResponse>) -> Unit,
        onError: (t: String) -> Unit,
        onFinished: () -> Unit
    ) {
        locationRepository.getCountryDetail(
            country, onSuccess, onError, onFinished
        )
    }
}