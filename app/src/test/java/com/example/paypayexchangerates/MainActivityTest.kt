package com.example.paypayexchangerates

import com.example.paypayexchangerates.utils.Utils
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActivityTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun validateAmountIfEmpty() {
        val amount = ""
        val assert = Utils.validateAmount(amount)
        assert(!assert)
    }

    @Test
    fun validateAmountIfNotEmpty() {
        val amount = "23.2"
        val assert = Utils.validateAmount(amount)
        assert(assert)
    }
}