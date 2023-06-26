package com.example.unfold.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unfold.data.models.Collection
import javax.inject.Inject

class CollectionPagingSource @Inject constructor(
    private val api: Api,
) : PagingSource<Int, Collection>() {

    override fun getRefreshKey(state: PagingState<Int, Collection>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>) = runCatching {
        val page = params.key ?: Api.InitialPage
        val collections = api.getCollections(page, params.loadSize)
        LoadResult.Page(
            data = collections,
            prevKey = null,
            nextKey = if (collections.size == params.loadSize) page + 1 else null,
        )
    }.getOrElse { LoadResult.Error(it) }
}
