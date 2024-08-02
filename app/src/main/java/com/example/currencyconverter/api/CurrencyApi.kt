package com.example.currencyconverter.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest")
    suspend fun getLatestRates(@Query("base") base: String): CurrencyResponse
}

