package com.example.unfold.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.math.roundToInt

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    var likes: Int,
    var likedByUser: Boolean,
    @Embedded val user: User,
    @Embedded val urls: Urls,
    @Ignore val downloads: Int? = null,
    @Ignore val exif: Exif? = null,
    @Ignore val location: Location? = null,
    @Ignore val tags: List<Tag>? = null,
) {

    val aspectRatio get() = width / height.toFloat()

    val blurSize: Pair<Int, Int> get() {
        return BlurQuality to (BlurQuality / aspectRatio).roundToInt()
    }

    constructor(
        id: String,
        width: Int,
        height: Int,
        blurHash: String,
        likes: Int,
        likedByUser: Boolean,
        user: User,
        urls: Urls,
    ) : this(id, width, height, blurHash, likes, likedByUser, user, urls, null, null, null, null)

    data class Urls(
        val regular: String,
        @Ignore val raw: String?,
    ) {

        constructor(regular: String) : this(regular, null)
    }

    data class Exif(
        val make: String?,
        val model: String?,
        val exposureTime: String?,
        val aperture: String?,
        val focalLength: String?,
        val iso: Int?,
    )

    data class Location(
        val name: String?,
        val position: Position,
    )

    data class Position(
        val latitude: Double,
        val longitude: Double,
    )

    companion object {

        private const val BlurQuality = 32

        val Mock = Photo(
            "Dwu85P9SOIk",
            2448,
            3264,
            "LFC${'$'}yHwc8^${'$'}yIAS$%M%00KxukYIp",
            24,
            true,
            User.Mock,
            Urls(
                "https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=1080&fit=max",
                "https://images.unsplash.com/photo-1417325384643-aac51acc9e5d",
            ),
            127,
            Exif("Canon", "Canon EOS 40D", "0.01", "4.97", "37", 100),
            Location("Montreal, Canada", Position(0.0, 0.0)),
            listOf(Tag("man"), Tag("drinking"), Tag("coffee")),
        )
    }
}
