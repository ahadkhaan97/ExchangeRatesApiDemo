package com.example.paypayexchangerates.repository

import androidx.lifecycle.MutableLiveData
import com.example.paypayexchangerates.model.allCurrencies.AllCurrenciesResponse
import com.example.paypayexchangerates.model.exchangeRates.ExchangeRatesResponse
import com.example.paypayexchangerates.service.ExchangeService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ExchangeRepository(baseUrl: String, private val apiKey: String) {
    private var exchangeService: ExchangeService? = null

    private var allCurrenciesLiveData = MutableLiveData<AllCurrenciesResponse?>()
    private var exchangeRatesLiveData = MutableLiveData<ExchangeRatesResponse?>()

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        exchangeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeService::class.java)
    }

    fun getAllCurrencies() {
        exchangeService?.getAllCurrencies()
            ?.enqueue(object : Callback<AllCurrenciesResponse?> {
                override fun onResponse(
                    call: Call<AllCurrenciesResponse?>,
                    response: Response<AllCurrenciesResponse?>
                ) {
                    if (response.body() != null) {
                        allCurrenciesLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<AllCurrenciesResponse?>, t: Throwable) {
                    allCurrenciesLiveData.postValue(null)
                }
            })
    }

    fun getAllCurrenciesLiveData(): MutableLiveData<AllCurrenciesResponse?> {
        return allCurrenciesLiveData
    }

    fun getExchangeRates() {
        exchangeService?.getExchangeRates(apiKey = apiKey)
            ?.enqueue(object : Callback<ExchangeRatesResponse?> {
                override fun onResponse(
                    call: Call<ExchangeRatesResponse?>,
                    response: Response<ExchangeRatesResponse?>
                ) {
                    if (response.body() != null) {
                        exchangeRatesLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ExchangeRatesResponse?>, t: Throwable) {
                    exchangeRatesLiveData.postValue(null)
                }
            })
    }

    fun getExchangeRatesLiveData(): MutableLiveData<ExchangeRatesResponse?> {
        return exchangeRatesLiveData
    }
}