package com.image.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.image.model.data.SearchImageViewModel

/**
 * SearchImageViewModel Fractory 객체
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.22
 */
@Suppress("UNCHECKED_CAST")
class SearchImageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchImageViewModel() as T
    }
}