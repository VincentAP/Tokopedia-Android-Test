package com.tokopedia.maps.domain

import com.tokopedia.maps.model.LocationResponse

interface ILocationRepository {

    fun getCountryDetail(
        country: String,
        onSuccess: (t: List<LocationResponse>) -> Unit,
        onError: ((t: String) -> Unit),
        onFinished: () -> Unit = {}
    )
}