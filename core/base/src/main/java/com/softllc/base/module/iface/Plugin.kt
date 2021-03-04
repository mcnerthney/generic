package com.softllc.base.module.iface

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface Plugin {
    val id: String

    val initialized : LiveData<Boolean>
    fun initialize (context: Context) : LiveData<Boolean>

}