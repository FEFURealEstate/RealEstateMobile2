package ru.fefu.nedviga.data.network

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.fefu.nedviga.data.network.CustomHeaderInterceptor
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var INSTANCE: App
    }

    val sharedPreferences by lazy {
        getSharedPreferences("shared_prefs", MODE_PRIVATE)
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .callTimeout(10L, TimeUnit.SECONDS)
            .addInterceptor(CustomHeaderInterceptor(sharedPreferences))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fefu.t.feip.co/")
            .client(okHttpClient)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}