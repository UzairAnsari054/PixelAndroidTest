package com.uzair.pixel.test

import android.app.Application
import com.uzair.pixel.test.di.AppContainerDI

class StackOverflowApplication : Application() {
    lateinit var containerDI: AppContainerDI

    override fun onCreate() {
        super.onCreate()
        containerDI = AppContainerDI(this)
    }
}
