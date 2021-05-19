package com.tokopedia.maps.data

import com.tokopedia.maps.api.LocationService
import com.tokopedia.maps.domain.ILocationRepository
import com.tokopedia.maps.model.LocationResponse
import com.tokopedia.maps.util.BaseRepo
import com.tokopedia.maps.util.addToDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocationRepositoryImpl(
    private val locationService: LocationService
) : BaseRepo(), ILocationRepository {

    override fun getCountryDetail(
        country: String,
        onSuccess: (t: List<LocationResponse>) -> Unit,
        onError: (t: String) -> Unit,
        onFinished: () -> Unit
    ) {
        locationService.getByCountryName(country)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate(onFinished)
            .subscribe({
                onSuccess(it)
            }, {
                onError(getErrorMessage(it))
                it.printStackTrace()
            }).addToDisposable()
    }
}