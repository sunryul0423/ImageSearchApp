package com.image.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding2.view.RxView
import com.image.R
import com.image.databinding.ActivityDetailBinding
import com.image.model.data.SearchImageViewModel
import com.image.model.view.SearchImageViewModelFactory
import com.image.utils.DETAIL
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * 상세화면 View
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.22
 * @property searchImageViewModelFactory viewModel Factory (BindingAdapter에서 Koin을 호출할수 없으므로 lazy 초기화)
 */
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val searchImageViewModelFactory by lazy {
        SearchImageViewModelFactory()
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //이전 데이터
        val intentViewModel = intent.getSerializableExtra(DETAIL) as SearchImageViewModel
        // Lifecycle viewModel 생성
        val searchImageViewModel =
            ViewModelProviders.of(this, searchImageViewModelFactory).get(SearchImageViewModel::class.java).apply {
                thumbnailUrl = intentViewModel.thumbnailUrl
                imageUrl = intentViewModel.imageUrl
                displaySiteName = intentViewModel.displaySiteName
                docUrl = intentViewModel.docUrl
                datetime = intentViewModel.datetime
                size = intentViewModel.size
            }

        supportActionBar?.title = searchImageViewModel.displaySiteName

        //클릭시 브라우저 호출
        searchImageViewModel.addDisposable(RxView.clicks(llDetailView)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(searchImageViewModel.docUrl)))
            })

        viewBinding.searchImageViewModel = searchImageViewModel
        viewBinding.lifecycleOwner = this
    }
}