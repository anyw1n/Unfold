package com.example.unfold.ui.screens.photo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unfold.R
import com.example.unfold.data.models.photoMock
import com.example.unfold.ui.common.PhotoItem

@Composable
fun PhotoScreen() {
    val viewModel = hiltViewModel<PhotoViewModel>()
    val photo = photoMock

    LazyColumn(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(36.dp))
        }
        item {
            PhotoItem(photoMock)
        }
        item {
            Row(modifier = Modifier.padding(start = 6.dp, top = 4.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = photo.location?.name ?: "",
                    modifier = Modifier.padding(start = 6.dp),
                )
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
                    text = "Made with: ${photo.exif?.make ?: ""}\n" + "Model: ${photo.exif?.model ?: ""}\n" + "Exposure: ${photo.exif?.exposureTime ?: ""}\n" + "Aperture: ${photo.exif?.aperture ?: ""}\n" + "Focal Length: ${photo.exif?.focalLength ?: ""}\n" + "ISO: ${photo.exif?.iso ?: ""}",
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "About @${photo.user.username}:\n" + photo.user.bio,
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Download",
                    textDecoration = TextDecoration.Underline,
                )
                Text(text = " (${photo.downloads})")
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
