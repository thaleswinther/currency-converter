package com.example.currencyconverter.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.repository.CurrencyRepository
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {

    var rates = mutableStateOf<Map<String, Double>?>(null)
        private set

    fun getRates(base: String) {
        viewModelScope.launch {
            rates.value = repository.getRates(base)?.rates
        }
    }
}


