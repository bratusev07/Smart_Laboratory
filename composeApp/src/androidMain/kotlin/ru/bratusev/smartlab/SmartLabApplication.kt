package ru.bratusev.smartlab

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import ru.bratusev.smartlab.di.initKoin
import org.koin.android.ext.koin.androidContext

class SmartLabApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(this)
        initKoin {
            androidContext(this@SmartLabApplication)
        }
    }
}