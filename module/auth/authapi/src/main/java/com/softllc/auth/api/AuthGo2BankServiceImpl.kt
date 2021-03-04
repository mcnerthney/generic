package com.softllc.auth.api

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AuthGo2BankServiceImpl : AuthService() {

    init {
        Timber.d("AuthGo2BankServiceImpl()")
    }
    class AuthUserImpl(val accessToken: String, val desc: String) : AuthUser {
        override val id: String
            get() = accessToken
        override val name: String
            get() =  "Go2BankUser $desc"
        override val anonymous: Boolean
            get() = false
        override val authIdp: AuthIdp?
            get() = AuthIdpGo2BankUser(accessToken,accessToken)


    }
    class AuthIdpGo2BankUser(val accessToken: String, val refreshToken: String) : AuthIdp (){

    }


    init {
        _currentUser.tryEmit(AuthUserImpl("", ""))
    }
    companion object {
        val _currentUser = MutableSharedFlow<AuthUser>(
                replay = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        val _busy = MutableSharedFlow<Boolean>(
                replay = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        var inprogrreess = false
    }

    val currentUserFlow: Flow<AuthUser> = _currentUser



    override fun signOut(activity: Activity) : Flow<Boolean> {
        if ( inprogrreess ) return busy
        inprogrreess = true
        _busy.tryEmit(true)

        _currentUser.tryEmit(AuthUserImpl("", ""))
        _busy.tryEmit(false)
        inprogrreess = false
        return busy
    }

    override fun signIn(activity: Activity)  : Flow<Boolean> {
        if ( inprogrreess ) return busy
        inprogrreess = true

        val i = Intent(activity, AuthGo2BankSigninActivity::class.java)
        ContextCompat.startActivity(activity, i, null)
        _busy.tryEmit(true)
        return busy
    }

    override val user: AuthUser
        get() = runBlocking {  AuthFirebaseServiceImpl._currentUser.first() }

    override val currentUser: Flow<AuthUser>
        get() = currentUserFlow

    override val busy: Flow<Boolean>
        get() = _busy

}