package com.gulshansutey.mycomposeplayground

import com.gulshansutey.mycomposeplayground.networking.NewsServices
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsServices: NewsServices) {

    fun fetchNews() = flow {
        val response = newsServices.getNews()
        emit(response)
    }

}
