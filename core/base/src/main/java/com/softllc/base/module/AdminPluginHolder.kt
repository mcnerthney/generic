package com.softllc.base.module

import com.softllc.base.module.iface.AuthPlugin
import com.softllc.base.module.iface.PluginHolder


class AdminPluginHolder private constructor(): PluginHolder<AuthPlugin> {
    private val plugins = mutableListOf<AuthPlugin>()
    override fun add(plugin: AuthPlugin) {
        plugins.add(plugin)
    }

    companion object {
        private lateinit var instance : AdminPluginHolder
        fun getInstance() : AdminPluginHolder {
            if ( !::instance.isInitialized ) {
                instance = AdminPluginHolder()
            }
            return instance
        }
    }
}