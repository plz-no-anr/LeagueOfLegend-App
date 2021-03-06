package com.psg.leagueoflegend_app.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LoLApp: Application() {

    companion object{
        lateinit var INSTANCE: LoLApp
        fun getContext(): Context = INSTANCE.applicationContext
        fun getGson(): Gson = Gson()
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
//        startKoin {
//            androidLogger(Level.NONE)
//            androidContext(this@LoLApp)
//            modules(
//                apiModule,
//                localDataModule,
//                remoteDataModule,
//                repositoryModule,
//                useCaseModule,
//                viewModelModule,
//                jsonModule
//            )
//        }
    }






}