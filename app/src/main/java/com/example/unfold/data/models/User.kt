package com.example.unfold.data.models

data class User(
    val id: String,
    val username: String,
    val name: String,
    val bio: String?,
    val profileImage: Image,
    val location: String?,
    val totalLikes: Int,
    val totalPhotos: Int,
    val totalCollections: Int,
    val downloads: Int?,
    val email: String?,
) {

    data class Image(
        val large: String,
    )
}

val mockUser = User(
    "QPxL2MGqfrw",
    "exampleuser",
    "Joe Example",
    "Just an everyday Joe",
    User.Image("https://m.media-amazon.com/images/I/41-IMhJh%2B-L._SR600%2C315_PIWhiteStrip%2CBottomLeft%2C0%2C35_SCLZZZZZZZ_FMpng_BG255%2C255%2C255.jpg"),
    "San Francisco, CA",
    44,
    123,
    1,
    69,
    "alexosesso@gmail.com",
)
