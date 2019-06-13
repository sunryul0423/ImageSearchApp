package com.image.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * ISO8601 타입 yyyy-MM-dd'T'HH:mm:ss.SSSZ 변경
 * @param dateTime ISO8601 타입
 * @return yyyy-MM-dd HH:mm (ex:2019-02-21)
 */
fun strDateTime(dateTime: String): String {
    return strFomat(strParse(dateTime, YYYY_MM_DD_T_HH_MM_SS_SSSZ), YYYY_MM_DD_HH_MM)
}

/**
 * date 객체를 format 형식의 날짜로 변환
 * @param date 객체 (Date(), System.currentTimeMillis()...)
 * @param format 날짜 format (ex)yyyyMMddHHmm)
 * @return 날짜 format 형식의 String
 */
fun strFomat(date: Any, format: String): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(date)
}

/**
 * format 형식의 String 값을 Date 형식으로 변환
 * @param date String 날짜
 * @param format 날짜 format (ex)yyyyMMddHHmm)
 * @return 날짜 format 형식의 Date()
 */
fun strParse(date: String, format: String): Date {
    return SimpleDateFormat(format, Locale.getDefault()).parse(date)
}