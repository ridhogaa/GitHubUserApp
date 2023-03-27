package com.ergea.githubuserapp.di

import android.content.Context
import com.ergea.githubuserapp.BuildConfig
import com.ergea.githubuserapp.data.local.database.AppDatabase
import com.ergea.githubuserapp.data.local.database.dao.FavoriteDao
import com.ergea.githubuserapp.data.local.database.datasource.FavoriteDataSource
import com.ergea.githubuserapp.data.local.database.datasource.FavoriteDataSourceImpl
import com.ergea.githubuserapp.data.local.datastore.SettingsDataStoreManager
import com.ergea.githubuserapp.data.local.datastore.SettingsDataStoreManagerDataSource
import com.ergea.githubuserapp.data.local.datastore.SettingsDataStoreManagerDataSourceImpl
import com.ergea.githubuserapp.data.remote.datasource.GitHubDataSource
import com.ergea.githubuserapp.data.remote.datasource.GitHubDataSourceImpl
import com.ergea.githubuserapp.data.remote.service.ApiService
import com.ergea.githubuserapp.data.repository.LocalRepository
import com.ergea.githubuserapp.data.repository.LocalRepositoryImpl
import com.ergea.githubuserapp.data.repository.RemoteRepository
import com.ergea.githubuserapp.data.repository.RemoteRepositoryImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

object ServiceLocator {

    private fun provideSettingsDataStoreManager(context: Context): SettingsDataStoreManager =
        SettingsDataStoreManager(context)

    private fun provideSettingsDataStoreManagerDataSource(context: Context): SettingsDataStoreManagerDataSource =
        SettingsDataStoreManagerDataSourceImpl(provideSettingsDataStoreManager(context))

    private fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)

    private fun provideFavoriteDao(context: Context): FavoriteDao =
        provideAppDatabase(context).favoriteDao()

    private fun provideFavoriteDataSource(context: Context): FavoriteDataSource =
        FavoriteDataSourceImpl(provideFavoriteDao(context))

    private fun provideRetrofit(): Retrofit {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", BuildConfig.TOKEN)
                .build()
            chain.proceed(requestHeaders)
        }
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    private fun provideGithubDataSource(): GitHubDataSource =
        GitHubDataSourceImpl(provideApiService(provideRetrofit()))

    fun provideLocalRepository(context: Context): LocalRepository =
        LocalRepositoryImpl(
            provideSettingsDataStoreManagerDataSource(context),
            provideFavoriteDataSource(context)
        )

    fun provideRemoteRepository(): RemoteRepository =
        RemoteRepositoryImpl(
            provideGithubDataSource()
        )

}