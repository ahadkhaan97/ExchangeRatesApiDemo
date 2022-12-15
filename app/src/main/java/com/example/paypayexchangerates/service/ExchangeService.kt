package com.example.paypayexchangerates.service

import com.example.paypayexchangerates.model.allCurrencies.AllCurrenciesResponse
import com.example.paypayexchangerates.model.exchangeRates.ExchangeRatesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeService {
    @GET("/currencies.json")
    fun getAllCurrencies(): Call<AllCurrenciesResponse?>?

    @GET("/latest.json")
    fun getExchangeRates(
        @Query("app_id") apiKey: String
    ): Call<ExchangeRatesResponse?>?
}