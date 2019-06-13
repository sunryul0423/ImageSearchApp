package com.image.model.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.image.adapter.SearchImageAdapter
import com.image.interfaces.ApiRequest
import com.image.model.data.SearchImageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 메인화면(검색화면) viewModel
 * @author SR.Park
 * @constructor Kakao Image Api
 * @param apiRequest API Interface
 * @property isViewAttach true : 페이징으로 붙임, false : 새로운 검색
 * @property isDataEmpty true : 데이터 없음, false : 데이터 있음
 * @property searchList API Response 리스트
 * @property searchImageAdapter recyclerView Adapter
 * @property pageNum 현재 페이징 번호
 * @property isEnd 페이징 가능 여부
 * @property searchText 검색 키워드
 */
class MainViewModel(private val apiRequest: ApiRequest) : BaseViewModel() {

    private val _isViewAttach = MutableLiveData<Boolean>()
    private val _isDataEmpty = MutableLiveData<Boolean>()
    private val _searchList = MutableLiveData<List<SearchImageResponse.Documents>>()

    val isViewAttach: LiveData<Boolean> get() = _isViewAttach
    val isDataEmpty: LiveData<Boolean> get() = _isDataEmpty
    val searchList: LiveData<List<SearchImageResponse.Documents>> get() = _searchList
    val searchImageAdapter = SearchImageAdapter()

    var pageNum = 1
    var isEnd: Boolean = true
    var searchText: String = ""

    /**
     * 검색 API 호출
     */
    fun requestSearchImage(isViewAttach: Boolean) {
        progress.value = true
        if (isViewAttach) {
            pageNum++
        } else {
            pageNum = 1
        }
        addDisposable(
            apiRequest.getSearchImage(searchText, pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it.meta.isEnd) {
                        isEnd = it.meta.isEnd
                    }
                    _searchList.value = it.documents
                    _isDataEmpty.value = it.documents.isNullOrEmpty()
                    _isViewAttach.value = isViewAttach
                    progress.value = false
                }, {
                    errorMsg.value = it.message
                    progress.value = false
                })
        )
    }
}