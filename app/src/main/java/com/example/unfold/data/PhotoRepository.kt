package com.example.unfold.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.unfold.data.models.room.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(
    private val api: Api,
    db: AppDatabase,
    photoRemoteMediator: PhotoRemoteMediator,
) {

    private val photoDao = db.photoDao()

    @OptIn(ExperimentalPagingApi::class)
    val photosFlow = Pager(
        config = PagingConfig(pageSize = Api.Limit),
        remoteMediator = photoRemoteMediator,
    ) {
        photoDao.pagingSource()
    }.flow

    fun collectionPhotosFlow(collectionId: String) = Pager(
        config = PagingConfig(pageSize = Api.Limit),
    ) {
        CollectionPhotosPagingSource(api, collectionId)
    }.flow

    fun likedPhotosFlow(username: String) = Pager(
        config = PagingConfig(pageSize = Api.Limit),
    ) {
        LikedPhotosPagingSource(api, username)
    }.flow
}
