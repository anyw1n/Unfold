package com.example.unfold.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Ignore

data class User(
    @ColumnInfo(name = "user_id") val id: String,
    val username: String,
    val name: String,
    @Embedded val profileImage: Image,
    @Ignore val bio: String?,
    @Ignore val location: String?,
    @Ignore val totalLikes: Int?,
    @Ignore val totalPhotos: Int?,
    @Ignore val totalCollections: Int?,
    @Ignore val downloads: Int?,
    @Ignore val email: String?,
) {

    constructor(
        id: String,
        username: String,
        name: String,
        profileImage: Image,
    ) : this(id, username, name, profileImage, null, null, null, null, null, null, null)

    data class Image(
        val large: String,
    )

    companion object {
        val Mock = User(
            "QPxL2MGqfrw",
            "exampleuser",
            "Joe Example",
            Image("https://m.media-amazon.com/images/I/41-IMhJh%2B-L._SR600%2C315_PIWhiteStrip%2CBottomLeft%2C0%2C35_SCLZZZZZZZ_FMpng_BG255%2C255%2C255.jpg"),
            "Just an everyday Joe",
            "San Francisco, CA",
            44,
            123,
            1,
            69,
            "alexosesso@gmail.com",
        )
    }
}
