package com.example.book

import android.app.Application
import com.google.android.material.color.DynamicColors

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}