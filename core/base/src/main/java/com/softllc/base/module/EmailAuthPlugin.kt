package com.softllc.base.module

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softllc.base.module.iface.AuthPlugin

class EmailAuthPlugin : AuthPlugin {
    override val id: String
        get() = "Email_Login"


    val _initialized = MutableLiveData<Boolean>()
    init {
        _initialized.value = false
    }

    override val initialized: LiveData<Boolean>
        get() = _initialized

    override fun initialize(context: Context): LiveData<Boolean> {
        _initialized.value = true
        return _initialized
    }

}