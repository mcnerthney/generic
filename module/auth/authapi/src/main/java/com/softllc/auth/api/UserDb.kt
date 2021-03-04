package com.softllc.auth.api

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDb(
        @PrimaryKey @ColumnInfo(name = "id") val userId: String,
        val name: String
) {
    override fun toString() = name
}
