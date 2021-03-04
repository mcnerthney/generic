
package com.softllc.photocache.data

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow


@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE id = :photoId ")
    fun getPhoto(photoId: String): Flow<PhotoRow>

    @Query("SELECT * FROM photos ORDER BY position")
    fun getAllPhotos(): Flow<List<PhotoRow>>

    @Insert(onConflict = REPLACE)
    fun insert(photos: PhotoRow)

    @Delete
    fun delete(photo: PhotoRow)

}
