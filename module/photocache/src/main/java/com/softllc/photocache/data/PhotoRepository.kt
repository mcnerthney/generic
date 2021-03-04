package com.softllc.photocache.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(
        private val photoDao: PhotoDao) {

    fun getPhoto(photoId: String): Flow<PhotoRow> {
        return photoDao.getPhoto(photoId)
    }

    fun getAllPhotos(): Flow<List<PhotoRow>> {
        return photoDao.getAllPhotos()
    }


    fun insert(photo: PhotoRow)  {
        photoDao.insert(photo)
    }



    fun delete(photo: PhotoRow) {
        photoDao.delete(photo)
    }


}
