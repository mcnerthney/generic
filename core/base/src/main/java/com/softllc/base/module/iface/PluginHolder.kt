package com.softllc.base.module.iface

interface PluginHolder<T:Plugin> {
    fun add(plugin: T)
}