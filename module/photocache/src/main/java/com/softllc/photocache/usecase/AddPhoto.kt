package com.softllc.photocache.usecase

import android.content.Context
import com.softllc.auth.api.AuthService
import com.softllc.auth.api.GetAuthUserUseCase
import com.softllc.base.core.*
import com.softllc.photocache.data.Photo
import com.softllc.photocache.data.PhotoRow
import com.softllc.photocache.data.PhotoRepository
import com.softllc.photocache.utilities.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.*
import javax.inject.Inject


class AddPhotoAuthUserNotFound(val param: AddPhotoUseCase.Param ) : Failure.FeatureFailure()

class AddPhotoUseCase
@Inject constructor(
        @ApplicationContext private val context: Context,
        private val getAuthUserUseCase: GetAuthUserUseCase,
        private val photoRepository: PhotoRepository,
        private val authService: AuthService)
    : UseCase<Photo, AddPhotoUseCase.Param>() {

    override fun run(params: Param): Flow<Either<Failure, Photo>> = channelFlow {
        var user = authService.user
        fun insertPhoto() {
            launch(Dispatchers.IO) {
                val photo = PhotoRow(params.photoId, "", position = Date().time, rotate = -1)
                photoRepository.insert(photo)
                val filename = FileUtils(context).upload(user.id, params.photoId, params.imageSrc)
                //copyToFirebaseStorage(filename)
                val copyPhoto = photo.copy(imageUrl = filename)
                photoRepository.insert(copyPhoto)  // update with image path
                send(Either.Right(Photo(copyPhoto)))
            }
        }

        if ( user.id.isNullOrBlank() ) {
            send(Either.Left(AddPhotoAuthUserNotFound(params)))
        }
        else {
            insertPhoto()
        }



//        getAuthUserUseCase(GetAuthUserUseCase.Param())
//        {
//            launch {
//                it.collect { userResult ->
//                    userResult.fold(
//                            { failure ->
//                                launch {
//                                    send(Either.Left(AddPhotoAuthUserNotFound(params)))
//                                }
//                            },
//                            { userId ->
//                                launch(Dispatchers.IO) {
//                                    val photo = Photo(params.photoId, "", position = Date().time, rotate = -1)
//                                    photoRepository.insert(photo)
//                                    val filename = FileUtils(context).upload(userId.id, params.photoId, params.imageSrc)
//                                    //copyToFirebaseStorage(filename)
//                                    val copyPhoto = photo.copy(imageUrl = filename)
//                                    photoRepository.insert(copyPhoto)  // update with image path
//                                    send(Either.Right(copyPhoto))
//                                }
//                            }
//                    )
//                }
//            }
//        }
    }

    class Param(val photoId: String, val imageSrc: String)
}


//fun addPhoto(photoId: String, imageSrc: String) : Photo {
//    val photo = Photo(photoId, imageSrc, position = Date().time, rotate = -1)
//    photoRepository.insert(photo)
//    return photo
//}

