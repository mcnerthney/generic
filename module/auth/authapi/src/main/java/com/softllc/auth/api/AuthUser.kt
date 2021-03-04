package com.softllc.auth.api

interface AuthUser {
    val id : String
    val name : String
    val anonymous : Boolean
    val authIdp : AuthIdp?
}

