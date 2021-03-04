package com.softllc.auth.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

/**
 * webview of login screen
 */
class AuthGo2BankSigninActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go2bank_signin)

        val userName = findViewById<TextInputLayout>(R.id.name)
        val loginButton = findViewById<Button>(R.id.button)
        loginButton.setOnClickListener {
            AuthGo2BankServiceImpl._currentUser.tryEmit(AuthGo2BankServiceImpl.AuthUserImpl("login", "login"))
            AuthGo2BankServiceImpl._busy.tryEmit(false)
            AuthGo2BankServiceImpl.inprogrreess = false
            finish()
        }
    }

    override fun onDestroy() {
        AuthGo2BankServiceImpl._busy.tryEmit(false)
        AuthGo2BankServiceImpl.inprogrreess = false
        super.onDestroy()
    }




}