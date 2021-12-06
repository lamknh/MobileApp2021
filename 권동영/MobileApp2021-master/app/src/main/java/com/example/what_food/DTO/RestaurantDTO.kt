package com.example.what_food.DTO
import com.google.gson.annotations.SerializedName

data class RestaurantDTO (
    val status : String,
    val total : Int,
    @SerializedName("data")
    val restaurantData : MutableList<Data>?) {
    data class Data(
        val cnt : String? = null,
        val OPENDATA_ID : String? = null,
        val GNG_CS : String? = null, // 주소
        val FD_CS : String? = null, // 분류
        val BZ_NM : String? = null, // 식당 이름
        val TLNO : String? = null,
        val MBZ_HR : String? = null,
        val SEAT_CNT : String? = null,
        val PKPL : String? = null,
        val HP : String? = null,
        val PSB_FRN : String? = null,
        val BKN_YN : String? = null,
        val INFN_FCL : String? = null,
        val BRFT_YN : String? = null,
        val DSSRT_YN : String? = null,
        val MNU : String? = null, // 메뉴
        // val SMPL_DESC : String? = null,
        // val SBW : String? = null,
        // val BUS : String? = null
    )
}