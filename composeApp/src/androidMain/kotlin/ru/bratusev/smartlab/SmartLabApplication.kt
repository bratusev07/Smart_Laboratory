package ru.bratusev.smartlab

import android.app.Application
import ru.bratusev.smartlab.di.initKoin
import org.koin.android.ext.koin.androidContext

class SmartLabApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@SmartLabApplication)
        }
    }
}