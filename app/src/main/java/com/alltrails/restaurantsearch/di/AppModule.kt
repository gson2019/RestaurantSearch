package com.alltrails.restaurantsearch.di

import android.content.Context
import android.content.SharedPreferences
import com.alltrails.restaurantsearch.network.GooglePlaceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(GooglePlaceApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providePlaceApi(retrofit: Retrofit): GooglePlaceApi =
        retrofit.create(GooglePlaceApi::class.java)

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.interceptors().add(HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor.Level.BODY
        })
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideSp(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("favorite_restaurant", Context.MODE_PRIVATE)
    }
}