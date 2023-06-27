package com.example.unfold.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unfold.data.models.Photo

class SearchPhotosPagingSource(
    private val api: Api,
    private val query: String,
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>) = runCatching {
        val page = params.key ?: Api.InitialPage
        val photos = api.searchPhotos(query, page, params.loadSize).results
        LoadResult.Page(
            data = photos,
            prevKey = null,
            nextKey = if (photos.size == params.loadSize) page + 1 else null,
        )
    }.getOrElse { LoadResult.Error(it) }
}
