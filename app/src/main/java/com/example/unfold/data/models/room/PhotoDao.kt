package com.example.unfold.data.models.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unfold.data.models.Photo

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Photo>)

    @Query("SELECT * FROM photos")
    fun pagingSource(): PagingSource<Int, Photo>

    @Query("DELETE FROM photos")
    suspend fun deleteAll()
}
