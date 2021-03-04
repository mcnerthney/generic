package com.softllc.photocache.data

class Photo(photoRow: PhotoRow)
{
    val photoId: String = photoRow.photoId
    val imageUrl: String = photoRow.imageUrl
    val zoom: Float = photoRow.zoom
    val scrollX: Float = photoRow.scrollX
    val scrollY: Float = photoRow.scrollY
    val position: Long = photoRow.position
    val rotate: Int = photoRow.rotate


    override fun toString() = "$position $photoId $imageUrl $zoom $scrollX $scrollY $rotate"
}

