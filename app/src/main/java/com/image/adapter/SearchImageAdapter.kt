package com.image.adapter

import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.image.R
import com.image.activity.DetailActivity
import com.image.activity.MainActivity
import com.image.databinding.ViewImageItemBinding
import com.image.model.data.SearchImageViewModel
import com.image.model.view.MainViewModel
import com.image.model.view.SearchImageViewModelFactory
import com.image.utils.DETAIL
import com.image.utils.MAX_PAGE
import com.image.utils.strDateTime
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 이미지 검색 RecyclerView Adapter 설정
 * @see R.layout.activity_main
 * @param view recyclerView
 * @param searchImageAdapter 이미지 검색 RecyclerView Adapter
 */
@BindingAdapter("searchImageAdapter")
fun setSearchImageAdapter(view: RecyclerView, searchImageAdapter: SearchImageAdapter?) {
    searchImageAdapter?.let {
        val recyclerViewDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = view.context.resources.getDimensionPixelSize(R.dimen.spacing_10dp)
            }
        }

        with(view) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = it
            addItemDecoration(recyclerViewDecoration)
        }
    }
}

/**
 *  이미지 검색 RecyclerView Adapter Item 설정
 * @see R.layout.activity_main
 * @param view recyclerView
 * @param mainViewModel MainActivity의  viewModel
 * @param isViewAttach true : 페이징으로 붙임, false : 새로운 검색
 */
@BindingAdapter("searchImageItem", "searchAdd")
fun setSearchImageItem(view: RecyclerView, mainViewModel: MainViewModel?, isViewAttach: Boolean) {
    mainViewModel?.let { model ->
        val searchList = model.searchList.value
        searchList?.let {
            val adapter = view.adapter as SearchImageAdapter
            val searchImageViewModelList: MutableList<SearchImageViewModel> = mutableListOf()
            it.map { response ->
                with(
                    SearchImageViewModel(
                        response.thumbnailUrl,
                        response.imageUrl,
                        response.displaySiteName,
                        response.docUrl,
                        strDateTime(response.datetime),
                        "${response.width} X ${response.height}"
                    )
                ) {
                    searchImageViewModelList.add(this)
                }
            }
            adapter.addItem(searchImageViewModelList, isViewAttach)
            if (!isViewAttach) {
                view.scrollToPosition(0)
            }
        }

        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //최하단 스크롤 (direction : -1이면 최상단 1이면 최하단)
                if (!recyclerView.canScrollVertically(1) && !model.isEnd && model.pageNum < MAX_PAGE) {
                    model.requestSearchImage(true)
                }
            }
        })
    }
}


/**
 * 이미지 검색 결과 ViewHolder
 * lifecycle에 맞게 사용하기 위해 ViewModelProviders로 Factory 생성
 * @param binding R.layout.view_image_item 레이아웃 바인딩
 */
class SearchImageHolder(val binding: ViewImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    internal fun setData(_searchImageViewModel: SearchImageViewModel) {
        val searchImageViewModel =
            ViewModelProviders.of(binding.root.context as MainActivity, SearchImageViewModelFactory())
                .get(SearchImageViewModel::class.java).apply {
                    thumbnailUrl = _searchImageViewModel.thumbnailUrl
                    imageUrl = _searchImageViewModel.imageUrl
                    displaySiteName = _searchImageViewModel.displaySiteName
                    docUrl = _searchImageViewModel.docUrl
                    datetime = _searchImageViewModel.datetime
                    size = _searchImageViewModel.size
                }

        searchImageViewModel.addDisposable(
            RxView.clicks(binding.ivItemImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    binding.root.context.startActivity(
                        Intent(binding.root.context, DetailActivity::class.java)
                            .putExtra(DETAIL, _searchImageViewModel)
                    )
                })
    }
}

/**
 * 이미지 검색 결과 RecyclerView Adapter
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.21
 * @property searchImageItemList 동시간대 방송 RecyclerView의 Item 리스트
 */
class SearchImageAdapter : RecyclerView.Adapter<SearchImageHolder>() {

    private val searchImageItemList: MutableList<SearchImageViewModel> = mutableListOf()

    /**
     * Item 리스트 Setting
     * @param _searchImageItemList Setting할 Item 리스트
     * @param isViewAttach true : 페이징으로 붙임, false : 새로운 검색
     */
    fun addItem(_searchImageItemList: MutableList<SearchImageViewModel>, isViewAttach: Boolean) {
        if (isViewAttach) {
            val size = searchImageItemList.size
            searchImageItemList.addAll(_searchImageItemList)
            notifyItemInserted(size)
        } else {
            searchImageItemList.clear()
            searchImageItemList.addAll(_searchImageItemList)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchImageHolder {
        val binding: ViewImageItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_image_item, parent, false)
        return SearchImageHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchImageItemList.size
    }

    override fun onBindViewHolder(holder: SearchImageHolder, position: Int) {
        holder.binding.searchImageViewModel = searchImageItemList[position]
        holder.binding.lifecycleOwner = holder.binding.root.context as MainActivity
        holder.setData(searchImageItemList[position])
    }
}