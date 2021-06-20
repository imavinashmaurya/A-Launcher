package com.avinash.applistsdk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import com.avinash.applistsdk.R


class PackageChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // fetching package names from extras
        val packageName = intent?.data?.encodedSchemeSpecificPart
        Log.e("BroadCast", intent?.action + packageName.toString())
        when (intent?.action) {
            Intent.ACTION_PACKAGE_REMOVED -> Toast.makeText(
                context,
                context?.getString(
                    R.string.app_removed,
                    packageName.toString()
                ),
                Toast.LENGTH_LONG
            ).show()

            Intent.ACTION_PACKAGE_ADDED -> Toast.makeText(
                context,
                context?.getString(
                    R.string.app_added,
                    packageName.toString()
                ),
                Toast.LENGTH_LONG
            ).show()
        }
        context?.packageManager?.let { AppListUtils.getInstalledAppList(it) }
    }

}