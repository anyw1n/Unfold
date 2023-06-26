package com.example.unfold.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.unfold.data.models.Photo
import com.example.unfold.data.models.room.AppDatabase
import com.example.unfold.data.models.room.PhotoRemoteKey
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator @Inject constructor(
    private val db: AppDatabase,
    private val api: Api,
) : RemoteMediator<Int, Photo>() {

    private val photoDao = db.photoDao()
    private val photoRemoteKeyDao = db.photoRemoteKeyDao()

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Photo>) = runCatching {
        val page = when (loadType) {
            LoadType.PREPEND -> return@runCatching true
            LoadType.REFRESH -> Api.InitialPage
            LoadType.APPEND -> state.lastItemOrNull()?.id?.let {
                photoRemoteKeyDao.getById(it)?.nextPage
            } ?: return@runCatching false
        }
        val limit = state.config.pageSize

        val photos = api.getPhotos(page, limit)
        val nextPage = if (photos.size == limit) page + 1 else null

        db.withTransaction {
            if (loadType == LoadType.REFRESH) {
                photoRemoteKeyDao.deleteAll()
                photoDao.deleteAll()
            }

            if (nextPage != null) {
                photoRemoteKeyDao.insert(PhotoRemoteKey(photos.last().id, nextPage))
            }
            photoDao.insertAll(photos)
        }

        nextPage == null
    }.fold(
        onSuccess = { MediatorResult.Success(it) },
        onFailure = { MediatorResult.Error(it) },
    )
}
