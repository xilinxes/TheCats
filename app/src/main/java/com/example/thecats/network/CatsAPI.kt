package com.example.thecats.network

import com.example.thecats.network.model.CatsRequestModel
import io.reactivex.Observable
import retrofit2.http.GET

interface CatsAPI {
    @GET("search?limit=10")
    fun getCats(): Observable<List<CatsRequestModel>>
}