package com.example.thecats.network.model

import com.example.thecats.model.CatsModel

data class CatsRequestModel(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)

fun CatsRequestModel.toCatsModel() = CatsModel(id = id, url = url, height = height)