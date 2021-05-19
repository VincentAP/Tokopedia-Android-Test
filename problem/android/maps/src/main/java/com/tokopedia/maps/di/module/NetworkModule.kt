package com.tokopedia.maps.di.module

import com.tokopedia.maps.api.LocationService
import com.tokopedia.maps.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val req = it.request()
            val builder = req.newBuilder()
                .addHeader("x-rapidapi-key", Constant.RAPID_API_KEY)
            it.proceed(builder.build())
        }
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(provideHeaderInterceptor())
            .addInterceptor(logging)
            .build()
    }

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun provideLocationService(): LocationService =
        provideRetrofit().create(LocationService::class.java)
}