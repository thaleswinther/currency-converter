package com.example.currencyconverter.domain.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.domain.model.entity.Currency

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM Currency WHERE base = :base")
    suspend fun getRates(base: String): Currency?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(currency: Currency)
}
