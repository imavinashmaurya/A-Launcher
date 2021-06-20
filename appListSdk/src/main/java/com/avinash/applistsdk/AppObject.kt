package com.avinash.applistsdk

import android.graphics.drawable.Drawable

data class AppObject(
    val appName: String,
    val appPackageName: String,
    val appImage: Drawable,
    val launcherActivity: String,
    val versionCode: String,
    val versionName: String
)