package com.image.interfaces

import com.image.model.data.SearchImageResponse
import com.image.utils.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * API 호출 Request 인터페이스
 * @author SR.Park
 * @constructor Kakao Image Api
 * @since 2019.02.20
 * @property getSearchImage 이미지 검색 API 호출
 */
interface ApiRequest {

    /**
     * 카카오 이미지 검색 API
     */
    @Headers(HEADER + APP_KEY)
    @GET(SEARCH_IMAGE)
    fun getSearchImage(@Query(QUERY) searchText: String, @Query(PAGE) page: Int): Single<SearchImageResponse>
}