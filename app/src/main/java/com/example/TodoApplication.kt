package com.example

import android.app.Application
import com.example.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Prevent KoinAppAlreadyStartedException during multi-threaded Robolectric unit tests
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@TodoApplication)
                modules(appModule)
            }
        }
    }
}
