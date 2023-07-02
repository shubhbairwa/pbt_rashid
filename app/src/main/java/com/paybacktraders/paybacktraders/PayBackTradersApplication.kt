package com.paybacktraders.paybacktraders

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PayBackTradersApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize the Prefs class
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}