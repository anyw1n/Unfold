package com.example.unfold.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.example.unfold.data.models.Photo
import com.example.unfold.util.BlurHashDecoder

@Composable
fun AsyncPhoto(
    model: Photo,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds,
    saveAspectRatio: Boolean = true,
) {
    val bitmap = remember {
        derivedStateOf {
            val (width, height) = model.blurSize
            BlurHashDecoder.decode(model.blurHash, width, height)
        }
    }

    SubcomposeAsyncImage(
        model = model.urls.regular,
        contentDescription = null,
        contentScale = contentScale,
        loading = {
            bitmap.value?.asImageBitmap()?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        },
        modifier = modifier.run { if (saveAspectRatio) aspectRatio(model.aspectRatio) else this },
    )
}
