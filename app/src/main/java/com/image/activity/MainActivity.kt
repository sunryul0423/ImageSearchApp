package com.image.activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding2.widget.RxTextView
import com.image.databinding.ActivityMainBinding
import com.image.model.view.MainViewModel
import com.image.model.view.MainViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

/**
 * 메인화면(검색화면) View
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.20
 * @property mainViewModelFactory viewModel Factory
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = com.image.R.layout.activity_main

    private val mainViewModelFactory: MainViewModelFactory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        //RxBinding으로 입력창 제어
        mainViewModel.addDisposable(RxTextView.textChanges(etSearchTitle)
            .debounce(1, TimeUnit.SECONDS)
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mainViewModel.searchText = it.toString()
                mainViewModel.requestSearchImage(false)
            })

        // LiveData observe
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mainViewModel.isDataEmpty.observe(this, Observer {
            inputMethodManager.hideSoftInputFromWindow(etSearchTitle.windowToken, 0)
        })
        mainViewModel.networkError.observe(this, Observer {
            networkErrorToast()
        })
        mainViewModel.isProgress.observe(this, Observer {
            if (it) {
                progressDialog.show()
            } else {
                progressDialog.cancel()
            }
        })
        viewBinding.mainViewModel = mainViewModel
        viewBinding.lifecycleOwner = this
    }
}
