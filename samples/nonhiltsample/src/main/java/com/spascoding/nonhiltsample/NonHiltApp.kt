package com.spascoding.nonhiltsample

import android.app.Application
import com.spascoding.configmastersdk.ConfigMasterSdk

class NonHiltApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ConfigMasterSdk.initialize(this)
        ConfigMasterSdk.insertJsonAsync(
            "NonHiltSample",
            """
                {
                    "card_count": "5",
                    "theme": "dark",
                    "api_url": "https://api.demo.com"
                }
            """
        )
    }
}