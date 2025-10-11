package com.spascoding.nonhiltsample

import android.app.Application
import com.spascoding.configmastersdk.ConfigMasterSdk

class NonHiltApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ConfigMasterSdk.initialize(this)
    }
}