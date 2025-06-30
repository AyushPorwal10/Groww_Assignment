package com.example.growwassignment.hilt

import android.content.Context
import androidx.room.Room
import com.example.growwassignment.BuildConfig
import com.example.growwassignment.watchlist.roomdatabase.AppRoomDatabase
import com.example.growwassignment.watchlist.roomdao.WatchlistDao
import com.example.growwassignment.gainerloser.apis.ApiKeyInterceptor
import com.example.growwassignment.gainerloser.apis.StockMarketAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object SingletonObject{


    @Provides
    @Singleton
    fun providedOkHttpClient() : okhttp3.OkHttpClient{
        return okhttp3.OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.ALPHA_VANTAGE_API_KEY))
            .build()
    }
    @Provides
    @Singleton
    fun getRetrofitObject(client : okhttp3.OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BuildConfig.ALPHA_VANTAGE_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMarketAPI(retrofit: Retrofit) : StockMarketAPI {
        return retrofit.create(StockMarketAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context : Context) : AppRoomDatabase {
        return Room.databaseBuilder(
            context,
            AppRoomDatabase::class.java,
            "watchlist_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(roomDatabase: AppRoomDatabase) : WatchlistDao {
        return roomDatabase.watchlistDao()
    }
}
