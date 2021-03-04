package com.softllc.app.ui.home

import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.softllc.auth.api.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BusinessRepository,
    val authService: AuthService
) : ViewModel()
{
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val currentUser: LiveData<AuthUser?> = authService.currentUser.asLiveData()

    val businesses: LiveData<List<BusinessDb>> = repository.getBusinesses().asLiveData()

    val signInBusy : LiveData<Boolean> = authService.busy.asLiveData()

}