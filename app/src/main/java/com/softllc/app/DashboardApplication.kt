package com.softllc.app

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.softllc.base.module.AdminPluginHolder
import com.softllc.base.module.DashboardPluginHolder
import com.softllc.base.module.EmailAuthPlugin
import com.softllc.home.ui.home.HomeDashboardPlugin
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DashboardApplication : Application() {

    private lateinit var _initialized : LiveData<Boolean>
    val initialized : LiveData<Boolean>
        get() { return _initialized }


    private val sso = MutableLiveData<String>()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Timber.d("onCreate")


        val homeDashboardPlugin = HomeDashboardPlugin()
        DashboardPluginHolder.getInstance().add(homeDashboardPlugin)
        val homeInit = homeDashboardPlugin.initialize(this)

        val homeDashboardPlugin2 = HomeDashboardPlugin()
        DashboardPluginHolder.getInstance().add(homeDashboardPlugin2)
        val homeInit2 = homeDashboardPlugin2.initialize(this)

        _initialized = homeInit.combineWith(homeInit2){ left, right ->
            (left ?: false) && (right ?: false)
        }

        val admin = EmailAuthPlugin()
        AdminPluginHolder.getInstance().add(admin)
        val adminInit = admin.initialize(this)

        _initialized = _initialized.combineWith(adminInit){ left, right ->
            (left ?: false) && (right ?: false)
        }

    }
}

fun <T, K, R> LiveData<T>.combineWith(
        liveData: LiveData<K>,
        block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}