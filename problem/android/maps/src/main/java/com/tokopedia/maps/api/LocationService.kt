package com.tokopedia.maps.api

import com.tokopedia.maps.model.LocationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationService {

    @GET("name/{country}")
    fun getByCountryName(
        @Path("country") country: String
    ): Single<List<LocationResponse>>
}