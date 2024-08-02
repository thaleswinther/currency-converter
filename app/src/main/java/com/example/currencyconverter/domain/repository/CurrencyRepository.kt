package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.model.entity.Currency
import com.example.currencyconverter.domain.retrofit.RetrofitInstance
import com.example.currencyconverter.domain.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyRepository(db: AppDatabase) {

    private val currencyDao = db.currencyDao()

    suspend fun getRates(base: String): Currency? {
        return withContext(Dispatchers.IO) {
            var rates = currencyDao.getRates(base)
            if (rates == null) {
                try {
                    val response = RetrofitInstance.api.getLatestRates(base)
                    if (response.result == "success") {
                        rates = Currency(response.base_code, response.rates)
                        currencyDao.insertRates(rates)
                    } else {
                    }
                } catch (e: Exception) {
                }
            }
            rates
        }
    }
}


