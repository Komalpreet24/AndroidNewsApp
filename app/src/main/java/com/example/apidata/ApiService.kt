package com.example.apidata

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "73f8be5456a24ab589075fcfe299a763"

interface ApiServiceInterface {

    @GET("v2/top-headlines?apiKey=${API_KEY}")
    fun getHeadlines(@Query("country") country: String, @Query("page") page: Int): Call<ApiData>
}

    object ApiService{
        val apiServiceInstance : ApiServiceInterface
        var logging: HttpLoggingInterceptor = HttpLoggingInterceptor()

        var httpClient = OkHttpClient.Builder()

        init{

            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build()
            apiServiceInstance = retrofit.create(ApiServiceInterface::class.java)

        }

}