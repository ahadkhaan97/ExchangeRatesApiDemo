package com.example.paypayexchangerates.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.paypayexchangerates.MainActivity
import com.example.paypayexchangerates.R
import com.example.paypayexchangerates.databinding.ActivitySplashBinding
import com.example.paypayexchangerates.model.allCurrencies.AllCurrenciesResponse
import com.example.paypayexchangerates.utils.Utils.isOnline
import io.paperdb.Paper

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkData()
    }

    private fun checkData() {
        if (isOnline(this)) {
            getCurrenciesFromService()
        } else {
            if (Paper.book()
                    .read<AllCurrenciesResponse>(getString(R.string.all_currencies)) == null ||
                Paper.book()
                    .read<AllCurrenciesResponse>(getString(R.string.all_currencies)) == null
            ) {
                binding.noInternetTv.text = getString(R.string.no_internet)
                binding.noInternetTv.visibility = VISIBLE
            } else {
                home()
            }
        }
    }

    private fun getCurrenciesFromService() {
        viewModel.init(getString(R.string.base_url), getString(R.string.app_id))

        viewModel.getAllCurrenciesLiveData().let {
            it?.observe(
                this
            ) { response ->
                if (response != null && response.error.isNullOrEmpty()) {
                    Paper.book().write(getString(R.string.all_currencies), response)
                    viewModel.getExchangeRates()
                } else {
                    apiError(response?.message ?: getString(R.string.api_error))
                }
            }
        }

        viewModel.getExchangeRatesLiveData().let {
            it?.observe(
                this
            ) { response ->
                if (response != null && !response.error) {
                    Paper.book().write(getString(R.string.exchange_rates), response)
                    home()
                } else {
                    apiError(response?.message ?: getString(R.string.api_error))
                }
            }
        }

        viewModel.getAllCurrencies()
    }

    private fun apiError(message: String) {
        binding.noInternetTv.text = message
        binding.noInternetTv.visibility = VISIBLE
    }

    private fun home() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}