package com.softllc.auth.api

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business")
data class BusinessDb(
        @PrimaryKey @ColumnInfo(name = "id") val businessId: String,
        val name: String
) {
    override fun toString() = name
}
