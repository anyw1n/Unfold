package com.example.unfold.data.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoRemoteKeyDao {
    @Query("SELECT * FROM photo_remote_keys WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PhotoRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: PhotoRemoteKey)

    @Query("DELETE FROM photo_remote_keys")
    suspend fun deleteAll()
}
