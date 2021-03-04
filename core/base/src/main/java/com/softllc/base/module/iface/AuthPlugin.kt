package com.softllc.base.module.iface

interface AuthPlugin : Plugin {
    override val id: String
        get() = "email_login"

}