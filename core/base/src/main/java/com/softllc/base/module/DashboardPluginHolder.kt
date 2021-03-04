package com.softllc.base.module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softllc.base.module.iface.DashboardPlugin
import com.softllc.base.module.iface.PluginHolder


class DashboardPluginHolder private constructor() : PluginHolder<DashboardPlugin> {

    private val list = mutableListOf<DashboardPlugin>()
    private val _plugins = MutableLiveData<List<DashboardPlugin>>()
    private val plugins : LiveData<List<DashboardPlugin>> = _plugins

    override fun add(plugin: DashboardPlugin) {
        list.add(plugin)
        _plugins.value = list
    }

    companion object {
        private lateinit var instance: DashboardPluginHolder
        fun getInstance(): DashboardPluginHolder {
            if (!::instance.isInitialized) {
                instance = DashboardPluginHolder()
            }
            return instance
        }
    }
}