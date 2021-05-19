package com.tokopedia.maps.util

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

open class BaseRepo {

    protected fun getErrorMessage(t: Throwable): String {
        return when (t) {
            is HttpException -> {
                val responseBody = t.response()?.errorBody()
                when (t.code()) {
                    HttpsURLConnection.HTTP_FORBIDDEN -> ApiException.forbidden()
                    HttpsURLConnection.HTTP_INTERNAL_ERROR -> ApiException.internalServerError()
                    HttpsURLConnection.HTTP_BAD_REQUEST -> ApiException.badRequest()
                    in 400 until 500 -> ApiException.badRequest()
                    in 500 until 600 -> ApiException.internalServerError()
                    else -> getError(responseBody) ?: ApiException.unknownError()
                }
            }
            is UnknownHostException,
            is SocketTimeoutException,
            is ConnectException -> ApiException.noInternetConnection()
            else -> t.localizedMessage ?: ApiException.unknownError()
        }
    }

    private fun getError(responseBody: ResponseBody?): String? {
        return try {
            val jsonObject = JSONObject(responseBody?.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }
}