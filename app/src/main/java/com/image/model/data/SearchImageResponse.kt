package com.image.model.data

import com.google.gson.annotations.SerializedName

/**
 * 카카오 이미지 검색 API Response
 * @param meta 페이징관련 Object
 * @param documents 이미지 정보 Object
 */
data class SearchImageResponse(
    val meta: Meta,
    val documents: List<Documents>
) {
    /**
     * @param totalCount 검색어에 검색된 문서수
     * @param pageableCount total_count 중에 노출가능 문서수
     * @param isEnd 현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음.
     */
    data class Meta(
        @SerializedName("total_count")
        val totalCount: Int,
        @SerializedName("pageable_count")
        val pageableCount: Int,
        @SerializedName("is_end")
        val isEnd: Boolean
    )

    /**
     * @param collection 컬렉션
     * @param thumbnailUrl 이미지 썸네일 URL
     * @param imageUrl 이미지 URL
     * @param width 이미지의 가로 크기
     * @param height 이미지의 세로 크기
     * @param displaySiteName 출처명
     * @param docUrl 문서 URL
     * @param datetime 문서 작성시간. ISO 8601
     */
    data class Documents(
        val collection: String,
        @SerializedName("thumbnail_url")
        val thumbnailUrl: String,
        @SerializedName("image_url")
        val imageUrl: String,
        val width: Int,
        val height: Int,
        @SerializedName("display_sitename")
        val displaySiteName: String,
        @SerializedName("doc_url")
        val docUrl: String,
        val datetime: String
    )
}