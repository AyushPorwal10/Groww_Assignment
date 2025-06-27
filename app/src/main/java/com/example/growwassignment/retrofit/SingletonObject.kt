package com.example.growwassignment.retrofit

import com.example.growwassignment.helper.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object SingletonObject{

    private const val BASE_URL = "https://www.alphavantage.co/"
    private const val API_KEY = "4UH8AXKTDLO5X823"

    @Provides
    @Singleton
    fun providedOkHttpClient() : okhttp3.OkHttpClient{
        return okhttp3.OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(API_KEY))
            .build()
    }
    @Provides
    @Singleton
    fun getRetrofitObject(client : okhttp3.OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMarketAPI(retrofit: Retrofit) : StockMarketAPI {
        return retrofit.create(StockMarketAPI::class.java)
    }

}
