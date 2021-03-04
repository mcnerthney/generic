package com.softllc.auth.api

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

abstract class AuthService {
    abstract fun signOut(activity: Activity) : Flow<Boolean>
    abstract fun signIn(activity: Activity): Flow<Boolean>

    abstract val user : AuthUser
    abstract val currentUser : Flow<AuthUser>
    abstract val busy: Flow<Boolean>

}