package com.tokopedia.maps.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

object RxHelper {

    internal var compositeDisposable: CompositeDisposable? = null

    fun clear() {
        compositeDisposable?.clear()
    }
}

fun Disposable.addToDisposable() {
    if (RxHelper.compositeDisposable == null) RxHelper.compositeDisposable = CompositeDisposable()
    RxHelper.compositeDisposable?.add(this)
}