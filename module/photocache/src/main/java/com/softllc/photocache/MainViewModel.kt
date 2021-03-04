package com.softllc.photocache

import androidx.lifecycle.*
import com.softllc.photocache.data.PhotoRow
import com.softllc.photocache.data.PhotoRepository
import java.util.*

class MainViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    fun addPhoto(photoId: String, imageSrc: String) : PhotoRow {
        val photo = PhotoRow(photoId, imageSrc, position = Date().time, rotate = -1)
        photoRepository.insert(photo)
        return photo
    }

}
