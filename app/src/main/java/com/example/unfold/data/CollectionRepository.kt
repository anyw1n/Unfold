package com.example.unfold.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val collectionPagingSource: CollectionPagingSource,
) {
    fun collectionsFlow() = Pager(config = PagingConfig(pageSize = Api.Limit)) {
        collectionPagingSource
    }.flow
}
