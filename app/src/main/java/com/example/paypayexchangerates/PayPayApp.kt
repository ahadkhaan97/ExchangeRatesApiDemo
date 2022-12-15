package com.example.paypayexchangerates

import android.app.Application
import io.paperdb.Paper

class PayPayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Paper.init(applicationContext)
    }
}