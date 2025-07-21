package com.example.unfold.ui.screens.photo

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unfold.R
import com.example.unfold.data.models.Photo
import com.example.unfold.ui.common.PhotoItem
import com.example.unfold.util.Downloader

data class PhotoUiState(
    val loading: Boolean = false,
    val photo: Photo? = null,
    val error: String? = null,
    val download: String? = null,
)

@Composable
fun PhotoScreen(showSnackbar: (String) -> Unit) {
    val viewModel = hiltViewModel<PhotoViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.error) {
        if (uiState.error == null) return@LaunchedEffect
        showSnackbar(uiState.error)
        viewModel.errorShown()
    }

    val context = LocalContext.current
    LaunchedEffect(uiState.download) {
        if (uiState.download == null || uiState.photo?.id == null) return@LaunchedEffect
        Downloader(context).download(uiState.download, uiState.photo.id)
        viewModel.downloaded()
    }

    if (uiState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val photo = uiState.photo ?: return
    LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        item {
            Spacer(modifier = Modifier.height(36.dp))
        }
        item {
            PhotoItem(photo, viewModel::like)
        }
        if (photo.location?.name != null) {
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 6.dp, top = 4.dp)
                        .clickable {
                            val position = photo.location.position
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = "geo:${position.latitude},${position.longitude}".toUri()
                            }
                            context.startActivity(intent)
                        },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = photo.location.name,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
        }
        item {
            Text(
                text = photo.tags?.joinToString(" ") { "#${it.title}" } ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
            )
        }
        item {
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, top = 16.dp, end = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Made with: ${photo.exif?.make ?: ""}\n" +
                        "Model: ${photo.exif?.model ?: ""}\n" +
                        "Exposure: ${photo.exif?.exposureTime ?: ""}\n" +
                        "Aperture: ${photo.exif?.aperture ?: ""}\n" +
                        "Focal Length: ${photo.exif?.focalLength ?: ""}\n" +
                        "ISO: ${photo.exif?.iso ?: ""}",
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "About @${photo.user.username}:\n" + (photo.user.bio ?: ""))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://unsplash.com/photos/${photo.id}")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, null))
                }) {
                    Text(text = stringResource(R.string.share))
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.clickable { viewModel.download() },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.download),
                        textDecoration = TextDecoration.Underline,
                    )
                    Text(text = " (${photo.downloads ?: "0"})")
                    Icon(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}
