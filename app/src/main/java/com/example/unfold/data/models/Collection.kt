package com.example.unfold.data.models

data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    val totalPhotos: Int,
    val coverPhoto: Photo,
    val user: User,
    val tags: List<Tag>,
)

val collectionMock = Collection(
    "0",
    "WOMAN PARADE",
    "A powerful collection of powerful images",
    11,
    photoMock,
    mockUser,
    listOf(Tag("man"), Tag("drinking"), Tag("coffee")),
)
