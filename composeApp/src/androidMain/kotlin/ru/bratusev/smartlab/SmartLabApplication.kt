package ru.bratusev.smartlab

import android.app.Application
import androidx.compose.ui.platform.LocalInspectionMode
import org.koin.android.ext.koin.androidContext
import ru.bratusev.smartlab.di.initKoin

class SmartLabApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@SmartLabApplication)
        }
    }
}