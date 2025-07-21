package com.example.unfold.data.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.unfold.data.models.Photo

@Database(
    entities = [Photo::class, PhotoRemoteKey::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun photoRemoteKeyDao(): PhotoRemoteKeyDao

    companion object {

        fun create(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "db").build()
    }
}
