package com.avinash.applistsdk

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object AppListUtils {

    private val appMutableList: MutableLiveData<ArrayList<AppObject>> = MutableLiveData()
    val appList: LiveData<ArrayList<AppObject>> get() = appMutableList

    fun getInstalledAppList(packageManager: PackageManager) {
        val list: ArrayList<AppObject> = ArrayList<AppObject>()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val appList = packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in appList) {
            val appName = resolveInfo.activityInfo.loadLabel(packageManager).toString()
            val appPackageName = resolveInfo.activityInfo.packageName
            val appImage = resolveInfo.activityInfo.loadIcon(packageManager)
            val launcherActivity = resolveInfo.activityInfo.name
            val versionCode = getVersionCode(packageManager, resolveInfo)
            val versionName = getVersionName(packageManager, resolveInfo)
            val app = AppObject(
                appName,
                appPackageName,
                appImage,
                launcherActivity,
                versionCode,
                versionName
            )
            if (!list.contains(app)) list.add(app)
        }
        list.sortBy { it.appName }
        appMutableList.postValue(list)
    }


    private fun getVersionCode(packageManager: PackageManager, resolveInfo: ResolveInfo): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(
                resolveInfo.activityInfo.packageName,
                0
            ).longVersionCode.toString()
        } else {
            packageManager.getPackageInfo(
                resolveInfo.activityInfo.packageName,
                0
            ).versionCode.toString()
        }
    }

    private fun getVersionName(packageManager: PackageManager, resolveInfo: ResolveInfo): String {
        return packageManager.getPackageInfo(
            resolveInfo.activityInfo.packageName,
            0
        ).versionName
    }
}