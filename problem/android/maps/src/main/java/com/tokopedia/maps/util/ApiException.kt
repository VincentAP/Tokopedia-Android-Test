package com.tokopedia.maps.util

object ApiException {

    fun forbidden() = "Forbidden"

    fun internalServerError() = "Internal Server Error"

    fun badRequest() = "Bad Request"

    fun noInternetConnection() = "No Internet Connection"

    fun unknownError() = "Unknown Error"
}