package com.example.unfold.ui.screens.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unfold.data.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    repository: CollectionRepository,
) : ViewModel() {
    val collectionsFlow = repository.collectionsFlow().cachedIn(viewModelScope)
}
