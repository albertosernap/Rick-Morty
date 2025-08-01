package com.albertoserna.rickmorty

import android.app.Application
import com.albertoserna.rickmorty.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RickMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@RickMortyApp)
            modules(appModule)
        }
    }
}