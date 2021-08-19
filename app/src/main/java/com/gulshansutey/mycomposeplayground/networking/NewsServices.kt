package com.gulshansutey.mycomposeplayground.networking

import com.gulshansutey.mycomposeplayground.model.NewsResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsServices {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("sources") sources: String = "techcrunch",
        @Query("apiKey") apiKey: String = RetrofitModule.API_KEY
    ): NewsResponseModel
}
