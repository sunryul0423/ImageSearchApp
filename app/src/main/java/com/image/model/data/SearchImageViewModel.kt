package com.image.model.data

import com.image.model.view.BaseViewModel
import java.io.Serializable

/**
 * 검색 결과 Item viewModel
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.21
 * @property thumbnailUrl 썸네일 이미지 Url
 * @property imageUrl 이미지 Url
 * @property displaySiteName 출처명
 * @property docUrl 문서 Url
 * @property datetime yyyy-MM-dd HH:mm (ex: 2019-02-21 15:30)
 * @property size 이미지 사이즈 (ex: 600 X 400)
 */
data class SearchImageViewModel(
    var thumbnailUrl: String? = "",
    var imageUrl: String? = "",
    var displaySiteName: String? = "",
    var docUrl: String? = "",
    var datetime: String? = "",
    var size: String? = ""
) : BaseViewModel(), Serializable
