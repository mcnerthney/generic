package com.softllc.home.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softllc.base.module.iface.DashboardPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeDashboardPlugin (): DashboardPlugin {
    override val topLevelDestinationId: Int
        get() = TODO("Not yet implemented")
    override val id: String
        get() = "Home"

    private val _initialized = MutableLiveData<Boolean>()
    init {
        _initialized.postValue(false)
    }

    override val initialized: LiveData<Boolean>
        get() = _initialized

    private var initializedCalled = false
    override fun initialize(context: Context): LiveData<Boolean> {
        if ( initializedCalled ) return initialized
        initializedCalled = true
        fun work(i: Int) {
            val wait = (5..10).random().toLong() * 1000
            Thread.sleep(wait)
            println("Work $i:$wait done")
        }


        val ioScope = CoroutineScope(Dispatchers.Default)
        ioScope.launch {
            work(this.hashCode())
            _initialized.postValue(true)
        }


        return _initialized
    }

}