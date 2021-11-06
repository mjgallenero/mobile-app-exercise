package com.michelle.gallenero.mobileappexercise.utils

import com.michelle.gallenero.mobileappexercise.model.SearchPhotoResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiManager {
    companion object {
        private const val BASE_URL = "https://api.flickr.com/services/rest/"
        const val API_KEY = "96358825614a5d3b1a1c3fd87fca2b47"

        // create the instance of the service for calling the API
        fun getInstance(): ApiManager {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient()
                    .newBuilder()
                    .connectTimeout(
                        10L,
                        TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor()).build())
                .build()
                .create(ApiManager::class.java)
        }
    }

    /**
     * Get the raw data of the response from calling the API
     */
    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&&api_key=$API_KEY")
    suspend fun searchImages(
        @Query("text") text: String,
        @Query("page") page: Int? = 1
    ): Response<SearchPhotoResponse>
}