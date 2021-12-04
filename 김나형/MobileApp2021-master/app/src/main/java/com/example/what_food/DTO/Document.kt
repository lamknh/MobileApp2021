package com.example.what_food.DTO

data class Document(
    val address: AddressX,
    val address_name: String,
    val address_type: String,
    val road_address: RoadAddress,
    val x: String,
    val y: String
)