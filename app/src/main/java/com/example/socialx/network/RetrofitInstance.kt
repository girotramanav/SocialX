package com.example.socialx.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private var retrofit: Retrofit? = null

    private const val BASE_URL = "https://newsapi.org/v2/"

    val retrofitInstance: Retrofit?
        get() {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                return@Interceptor chain.proceed(newRequest.build())
            }
        )
    }.build()
}