package com.softllc.photocache

import android.content.Context
import androidx.lifecycle.*
import com.softllc.auth.api.AuthService
import com.softllc.base.core.Event
import com.softllc.base.core.Failure
import com.softllc.photocache.Analytic.LogAnalyticEvent
import com.softllc.photocache.data.Photo
import com.softllc.photocache.data.PhotoRow
import com.softllc.photocache.data.PhotoRepository
import com.softllc.photocache.usecase.AddPhotoUseCase
import com.softllc.photocache.utilities.runOnIoThread
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    private val _failure: MutableLiveData<Event<Failure>> = MutableLiveData()
    val failure: LiveData<Event<Failure>> = _failure

    protected val _busy: MutableLiveData<Boolean> = MutableLiveData(false)
    val busy: LiveData<Boolean> = _busy

    protected fun handleFailure(failure: Failure) {
        _failure.postValue(Event(failure))
        _busy.postValue(false)
    }
}

@HiltViewModel
class FreezeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val photoRepository: PhotoRepository,
    private val addPhotoUseCase: AddPhotoUseCase,
    val authService: AuthService

) : BaseViewModel() {

    private val _photoList = MediatorLiveData<List<Photo>>()
    private val _photo = MediatorLiveData<Event<Photo>>()

    val photos: LiveData<List<Photo>> = _photoList
    val photo: LiveData<Event<Photo>> = _photo

    init {
        _photoList.addSource(photoRepository.getAllPhotos().asLiveData()) {
            _photoList.postValue(it.map {Photo(it)})
        }
    }

    fun rotate(dir: Int) {
//        val photo = photo.value
//        if ( photo != null ) {
//            update(photo.copy(rotate = dir))
//        }
    }

    fun addPhoto( imageSrc: String): String {
        _busy.postValue(true)
        val photoId = UUID.randomUUID().toString()
        viewModelScope.launch {
            addPhotoUseCase(AddPhotoUseCase.Param( photoId, imageSrc)) {
                launch {
                    it.collect {
                        it.fold(
                                ::handleFailure
                        ) {
                            _photo.postValue(Event(it))
                            _busy.postValue(false)
                        }
                    }
                }
            }
        }
        return photoId
    }


    fun deleteAll() {
        val photos = _photoList.value
        if (photos != null) {
            runOnIoThread {
                for (photo in photos) {
                    File(photo.imageUrl).delete()
                    photoRepository.delete(PhotoRow(photoId = photo.photoId))
                }
                context.LogAnalyticEvent(Analytic.Event.DELETE_ALL)
            }
        }

    }
}


