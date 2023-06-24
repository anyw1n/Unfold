package com.example.unfold.ui.screens.ribbon

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unfold.R
import com.example.unfold.data.models.photoMock
import com.example.unfold.ui.common.PhotoItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RibbonScreen(photoTap: (String) -> Unit) {
    val viewModel = hiltViewModel<RibbonViewModel>()

    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(horizontal = 8.dp),
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalItemSpacing = 10.dp,
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.top_today),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 21.dp, bottom = 24.dp),
                )
                PhotoItem(photoMock, photoTap)
                Spacer(modifier = Modifier.height(7.dp))
            }
        }
        items(List(10) { photoMock }) {
            PhotoItem(it, photoTap)
        }
    }
}
