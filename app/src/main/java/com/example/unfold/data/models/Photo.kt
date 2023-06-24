package com.example.unfold.data.models

import android.graphics.Bitmap
import com.example.unfold.util.BlurHashDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Photo(
    val id: String,
    private val width: Int,
    private val height: Int,
    private val blurHash: String,
    val likes: Int,
    val likedByUser: Boolean,
    val user: User,
    val urls: Urls,
    val downloads: Int?,
    val exif: Exif?,
    val location: Location?,
    val tags: List<Tag>?,
) {

    private val aspectRatio = width / height.toDouble()

    var blurBitmap: Bitmap? = null

    suspend fun loadBlurBitmap() = withContext(Dispatchers.Default) {
        blurBitmap =
            BlurHashDecoder.decode(blurHash, BlurQuality, (BlurQuality / aspectRatio).toInt())
    }

    data class Urls(
        val regular: String,
        val raw: String,
    )

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

    private companion object {

        private const val BlurQuality = 1080
    }
}

val photoMock = Photo(
    "Dwu85P9SOIk",
    2448,
    3264,
    "LFC${'$'}yHwc8^${'$'}yIAS$%M%00KxukYIp",
    24,
    true,
    mockUser,
    Photo.Urls(
        "https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=1080&fit=max",
        "https://images.unsplash.com/photo-1417325384643-aac51acc9e5d",
    ),
    127,
    Photo.Exif("Canon", "Canon EOS 40D", "0.01", "4.97", "37", 100),
    Photo.Location("Montreal, Canada", Photo.Position(0.0, 0.0)),
    listOf(Tag("man"), Tag("drinking"), Tag("coffee")),
)
