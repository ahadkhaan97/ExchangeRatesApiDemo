package com.example.paypayexchangerates.model.exchangeRates


import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
    @SerializedName("base")
    val base: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("disclaimer")
    val disclaimer: String = "",
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("license")
    val license: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("rates")
    val rates: HashMap<String, Double> = HashMap(),
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("timestamp")
    val timestamp: Int = 0
)