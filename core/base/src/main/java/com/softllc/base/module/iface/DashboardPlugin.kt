package com.softllc.base.module.iface

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

interface DashboardPlugin : Plugin {

    /**
     * use for AppBarConfiguration
     * topLevelDestinationId considered at the top level of your information hierarchy.
     * The Up button will not be displayed when on this destination.
     */
    val topLevelDestinationId : Int


}