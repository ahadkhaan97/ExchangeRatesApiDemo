package com.example.paypayexchangerates.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.paypayexchangerates.model.allCurrencies.AllCurrenciesResponse
import com.example.paypayexchangerates.model.exchangeRates.ExchangeRatesResponse
import com.example.paypayexchangerates.repository.ExchangeRepository

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: ExchangeRepository? = null
    private var currencyResponseLiveData: MutableLiveData<AllCurrenciesResponse?>? = null
    private var exchangeRatesResponseLiveData: MutableLiveData<ExchangeRatesResponse?>? = null

    fun init(baseUrl: String, apiKey: String) {
        repository = ExchangeRepository(baseUrl, apiKey)
        currencyResponseLiveData = repository?.getAllCurrenciesLiveData()
        exchangeRatesResponseLiveData = repository?.getExchangeRatesLiveData()
    }

    fun getAllCurrencies() {
        repository?.getAllCurrencies()
    }

    fun getAllCurrenciesLiveData(): MutableLiveData<AllCurrenciesResponse?>? {
        return currencyResponseLiveData
    }

    fun getExchangeRates() {
        repository?.getExchangeRates()
    }

    fun getExchangeRatesLiveData(): MutableLiveData<ExchangeRatesResponse?>? {
        return exchangeRatesResponseLiveData
    }
}