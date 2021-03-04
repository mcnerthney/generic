package com.softllc.photocache

import androidx.lifecycle.*
import com.softllc.photocache.data.PhotoRow
import com.softllc.photocache.data.PhotoRepository
import com.softllc.photocache.utilities.runOnIoThread
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _photoList = MediatorLiveData<List<PhotoRow>>()
    private val _photo = MediatorLiveData<PhotoRow>()

    val photos : LiveData<List<PhotoRow>> = _photoList
    val photo : LiveData<PhotoRow> = _photo

    init {
        _photoList.addSource(photoRepository.getAllPhotos().asLiveData(), _photoList::setValue)
    }

    fun setPhotoId(id: String) {
        _photo.addSource(photoRepository.getPhoto(id).asLiveData(), _photo::setValue)
    }
    fun update( photo: PhotoRow ) {
        runOnIoThread {
            photoRepository.insert(photo)
            _photo.postValue(photo)
        }
    }

    fun rotate ( dir : Int ) {
//        val photo = photo.value
//        if ( photo != null ) {
//            update(photo.copy(rotate = dir))
//        }
    }

    fun delete( photo: PhotoRow ) {
        runOnIoThread {
            File(photo.imageUrl).delete()
            photoRepository.delete(photo)
        }
        _photo.postValue(null)

    }


}
