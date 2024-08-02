package com.example.currencyconverter.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.currencyconverter.api.Converters
import com.example.currencyconverter.domain.model.entity.Currency
import com.example.currencyconverter.domain.model.dao.CurrencyDao

@Database(entities = [Currency::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "currency_database")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
