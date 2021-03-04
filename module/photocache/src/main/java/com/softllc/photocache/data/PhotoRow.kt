package com.softllc.photocache.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photos")
data class PhotoRow(
    @PrimaryKey @ColumnInfo(name = "id") val photoId: String,
    val imageUrl: String = "",
    val zoom: Float = 1f,
    val scrollX: Float = 0f,
    val scrollY: Float = 0f,
    val position: Long = 0,
    val rotate: Int = 0,
    val userId: String = ""
) {
    override fun toString() = "$position $photoId $imageUrl $zoom $scrollX $scrollY $rotate"
}
