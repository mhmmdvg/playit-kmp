package com.playit.presentation.ui.components

import androidx.compose.runtime.Composable
import com.playit.data.remote.resources.Resource

@Composable
fun <T> StateWrapper(
    resource: Resource<T>,
    onLoading: @Composable () -> Unit,
    onFailure: @Composable (String?) -> Unit,
    onSuccess: @Composable (T?) -> Unit,
) {
    when (resource) {
        is Resource.Loading -> onLoading()
        is Resource.Error -> onFailure(resource.message)
        is Resource.Success -> onSuccess(resource.data)
    }
}