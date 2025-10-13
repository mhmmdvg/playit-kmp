package com.playit.presentation.ui.components

import androidx.compose.foundation.lazy.LazyListScope
import com.playit.data.remote.resources.Resource

fun <T> LazyListScope.stateWrapper(
    resource: Resource<T>,
    onLoading: LazyListScope.() -> Unit,
    onFailure: LazyListScope.(String?) -> Unit,
    onSuccess: LazyListScope.(T?) -> Unit,
) {
    when (resource) {
        is Resource.Loading -> onLoading()
        is Resource.Error -> onFailure(resource.message)
        is Resource.Success -> onSuccess(resource.data)
    }
}

