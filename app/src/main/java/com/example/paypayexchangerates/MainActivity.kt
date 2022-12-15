package com.example.paypayexchangerates

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paypayexchangerates.adapters.CurrenciesAdapter
import com.example.paypayexchangerates.databinding.ActivityMainBinding
import com.example.paypayexchangerates.model.allCurrencies.AllCurrenciesResponse
import com.example.paypayexchangerates.model.exchangeRates.ExchangeRatesResponse
import com.example.paypayexchangerates.model.recycler.RecyclerItem
import com.example.paypayexchangerates.utils.Utils
import com.example.paypayexchangerates.utils.setSafeOnClickListener
import com.example.paypayexchangerates.utils.toMap
import com.google.gson.Gson
import com.jakewharton.rxbinding2.widget.RxTextView
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import io.paperdb.Paper
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currenciesStringList = ArrayList<PowerMenuItem>()
    private var recyclerList = ArrayList<RecyclerItem>()

    private var exchangeRates = HashMap<String, Double>()
    private var currencyNames = HashMap<String, String>()

    private var selectedCurrency = "USD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initListeners()
        initUI()
    }

    private fun initUI() {
        binding.recycler.apply {
            adapter = CurrenciesAdapter(recyclerList)
            layoutManager = GridLayoutManager(this@MainActivity, 3)
        }
    }

    private fun initData() {
        val currencies =
            Paper.book().read<AllCurrenciesResponse>(getString(R.string.all_currencies))

        val jsonObj = JSONObject(Gson().toJson(currencies))
        val map = jsonObj.toMap()
        map.forEach {
            currenciesStringList.add(PowerMenuItem(it.value.toString(), it.key))
            currencyNames[it.key] = it.value.toString()
        }

        Paper.book().read<ExchangeRatesResponse>(getString(R.string.exchange_rates)).let {
            if (it?.rates != null) {
                exchangeRates = it.rates
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun performConversion(toString: String) {

        if (Utils.validateAmount(binding.currencyAmount.text.toString())) {
            Toast.makeText(this, getString(R.string.error_amount_empty), Toast.LENGTH_SHORT).show()
            return
        }

        val convertedValue = toString.toDouble() / (exchangeRates[selectedCurrency] ?: 1.0)

        recyclerList.clear()
        exchangeRates.forEach {
            if (it.key != selectedCurrency) {
                recyclerList.add(
                    RecyclerItem(
                        it.key,
                        currencyNames[it.key] ?: "N/A",
                        it.value * convertedValue
                    )
                )
            }
        }

        recyclerList.sortBy { it.short }
        binding.recycler.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "CheckResult")
    private fun initListeners() {

        binding.currencyDropdown.text =
            getString(R.string.select_currency) + " - " + selectedCurrency

        val powerMenu = PowerMenu.Builder(this)
            .addItemList(currenciesStringList)
            .setAnimation(MenuAnimation.DROP_DOWN)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setWidth(Resources.getSystem().displayMetrics.widthPixels)
            .setTextGravity(Gravity.CENTER)
            .setTextSize(12)
            .setDividerHeight(1)
            .setTextTypeface(ResourcesCompat.getFont(this, R.font.gilroy_medium)!!)
            .build()

        powerMenu.setOnMenuItemClickListener { _, item ->
            selectedCurrency = item.tag.toString()
            binding.currencyDropdown.text = getString(R.string.select_currency) + " - " + item.title
            performConversion(binding.currencyAmount.text.toString())
            powerMenu.dismiss()
        }

        binding.currencyDropdown.setSafeOnClickListener {
            powerMenu.showAsDropDown(binding.currencyDropdown)
        }

        RxTextView.textChanges(binding.currencyAmount)
            .map(CharSequence::toString)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (Utils.validateAmount(it.toString())) {
                    performConversion(it.toString())
                } else {
                    recyclerList.clear()
                    binding.recycler.adapter?.notifyDataSetChanged()
                }
            }
    }
}