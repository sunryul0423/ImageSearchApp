package com.image.model.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 공통적으로 필요한 변수,함수 정의한 ViewModel
 * Rx에 관련된 메소드 사용시 메모리 해제를 위해 lifecycleOwner(ViewModelProviders) 꼭 설정해준다.
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.20
 * @property compositeDisposable Rx 구독 객체
 * @property networkError 네트워크 에러 메세지
 * @property isProgress 프로그래스 실행여부
 * @property addDisposable RxJava2를 이용한 메모리 할당
 * @property onCleared RxJava2를 이용한 메모리 해제
 */
abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    protected val errorMsg = MutableLiveData<String>()
    protected val progress = MutableLiveData<Boolean>().apply { value = false }
    val networkError: LiveData<String> get() = errorMsg
    val isProgress: LiveData<Boolean> get() = progress

    /**```
     * 비동기 작업인 Rx와 API 통신은 CompositeDisposable로 관리
     * ViewModel은 Activity의 Lifecycle과 연결되어 메모리릭 현상 예방
     * ```
     */
    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * View가 파괴되면 CompositeDisposable을 비워줌으로 메모리 관리
     */
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}