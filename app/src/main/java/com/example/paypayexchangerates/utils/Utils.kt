package com.example.paypayexchangerates.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object Utils {
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    fun validateAmount(str: String): Boolean {
        return str.isNotEmpty()
    }
}

fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith { str ->
    when (val value = this[str]) {
        is JSONArray -> {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else -> value
    }
}

@SuppressLint("CheckResult")
fun View.setSafeOnClickListener(onClick: (View) -> Unit) {
    RxView.clicks(this).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe {
        onClick(this)
    }
}