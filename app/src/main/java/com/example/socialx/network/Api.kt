package com.example.socialx.network

import com.example.socialx.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("top-headlines")
    fun getNews(@Query("country") code : String , @Query("apiKey") key : String) : Call<NewsResponse>

    @GET("everything")
    fun getTopicNews(@Query("q") topic : String , @Query("apiKey") key : String) : Call<NewsResponse>
}