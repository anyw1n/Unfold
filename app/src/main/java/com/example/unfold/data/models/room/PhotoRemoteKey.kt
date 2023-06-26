package com.example.unfold.data.models.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_remote_keys")
data class PhotoRemoteKey(
    @PrimaryKey val id: String,
    val nextPage: Int?,
)
