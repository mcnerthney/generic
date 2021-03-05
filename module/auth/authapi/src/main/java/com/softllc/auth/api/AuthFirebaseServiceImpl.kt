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
import javax.inject.Singleton

class AuthFirebaseServiceImpl : AuthService() {

    init {
        Timber.d("AuthFirebaseServiceImpl()")
    }
    class AuthUserImpl(val user: FirebaseUser?) : AuthUser {
        override val id: String
            get() = user?.uid ?: ""
        override val name: String
            get() = user?.displayName ?: ""
        override val anonymous: Boolean
            get() = user?.isAnonymous ?: false
        override val authIdp: AuthIdp?
            get() = if ( user == null ) null else AuthIdpFirebaseUser(user)


    }
    class AuthIdpFirebaseUser(val firebaseUser: FirebaseUser) : AuthIdp (){

    }


    init {
        AuthFirebaseServiceImpl._currentUser.tryEmit(AuthFirebaseServiceImpl.AuthUserImpl(FirebaseAuth.getInstance().currentUser))
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
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener {
                    AuthFirebaseServiceImpl._currentUser.tryEmit(AuthFirebaseServiceImpl.AuthUserImpl(FirebaseAuth.getInstance().currentUser))
                    _busy.tryEmit(false)
                    inprogrreess = false
                }
        _busy.tryEmit(true)
        return busy
    }

    override fun signIn(activity: Activity)  : Flow<Boolean> {
        if ( inprogrreess ) return busy
        inprogrreess = true

        val i = Intent(activity, AuthFirebaseSigninActivity::class.java)
        ContextCompat.startActivity(activity, i, null)
        _busy.tryEmit(true)
        return busy
    }

    override val user: AuthUser
        get() = runBlocking {  _currentUser.first() }

    override val currentUser: Flow<AuthUser>
        get() = currentUserFlow

    override val busy: Flow<Boolean>
        get() = _busy

}