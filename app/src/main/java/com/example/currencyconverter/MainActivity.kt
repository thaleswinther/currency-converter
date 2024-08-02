package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.currencyconverter.domain.database.AppDatabase.Companion.getInstance
import com.example.currencyconverter.domain.repository.CurrencyRepository
import com.example.currencyconverter.presentation.screens.CurrencyConverterScreen
import com.example.currencyconverter.presentation.viewmodel.CurrencyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = getInstance(applicationContext)
        val repository = CurrencyRepository(db)
        val viewModel = CurrencyViewModel(repository)

        setContent {
            CurrencyConverterScreen(viewModel)
        }
    }
}


