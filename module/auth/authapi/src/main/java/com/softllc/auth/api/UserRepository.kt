/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.softllc.auth.api

import android.content.Context
import androidx.lifecycle.asLiveData
import com.softllc.base.core.Either
import com.softllc.base.core.Failure
import com.softllc.base.core.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


class AuthUserNotFound() : Failure.FeatureFailure()

class GetAuthUserUseCase
@Inject constructor(
       @ApplicationContext private val context: Context,
        private val authService: AuthService)
    : UseCase<AuthUser, GetAuthUserUseCase.Param>() {

    override fun run( none: Param) : Flow<Either<Failure, AuthUser>> = flow {
        val user = authService.currentUser.first()
        if ( user?.id.isNullOrBlank() || user == null ) {
            emit(Either.Left(AuthUserNotFound()))
        }
        else {
            emit(Either.Right(user))
        }
    }

    class Param


}



@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getUsers() = userDao.getUsers()

    fun getUser(userId: String) = userDao.getUser(userId)

}
