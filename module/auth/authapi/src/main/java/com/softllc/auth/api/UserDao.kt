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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the User class.
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY name")
    fun getUsers(): Flow<List<UserDb>>

    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUser(userId: String): Flow<UserDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userDbs: List<UserDb>)
}
