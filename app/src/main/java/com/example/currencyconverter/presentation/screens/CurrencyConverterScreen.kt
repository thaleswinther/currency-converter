package com.example.currencyconverter.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.R
import com.example.currencyconverter.presentation.viewmodel.CurrencyViewModel
import java.text.DecimalFormat

@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel) {
    var baseCurrency by remember { mutableStateOf("") }
    var targetCurrency by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var conversionResult by remember { mutableStateOf<Double?>(null) }
    var isRatesLoaded by remember { mutableStateOf(false) }
    val rates by viewModel.rates
    val focusManager = LocalFocusManager.current

    val currencies = rates?.keys?.toList() ?: listOf()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.currency_converter),
            style = MaterialTheme.typography.h4.copy(fontSize = 30.sp),
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        CurrencyDropdownField(
            label = stringResource(id = R.string.base_currency),
            selectedCurrency = baseCurrency,
            currencies = currencies,
            onCurrencySelected = { baseCurrency = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        CurrencyDropdownField(
            label = stringResource(id = R.string.target_currency),
            selectedCurrency = targetCurrency,
            currencies = currencies,
            onCurrencySelected = { targetCurrency = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = amount,
            onValueChange = {
                amount = it
            },
            label = { Text(stringResource(id = R.string.amount)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    viewModel.getRates(baseCurrency)
                    isRatesLoaded = true
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(stringResource(id = R.string.get_rates))
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    rates?.let {
                        val baseRate = it[baseCurrency]
                        val targetRate = it[targetCurrency]
                        if (baseRate != null && targetRate != null) {
                            val value = amount.toDoubleOrNull() ?: 0.0
                            conversionResult = value * (targetRate / baseRate)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = isRatesLoaded
            ) {
                Text(stringResource(id = R.string.convert))
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        conversionResult?.let {
            val formattedResult = DecimalFormat("#.####").format(it)
            Text(
                text = stringResource(id = R.string.converted_amount, formattedResult, targetCurrency),
                style = MaterialTheme.typography.h6.copy(fontSize = 24.sp),
                color = Color(0xFF00C853),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun CurrencyDropdownField(
    label: String,
    selectedCurrency: String,
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var dropdownWidth by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column {
            TextField(
                value = selectedCurrency,
                onValueChange = { },
                readOnly = true,
                label = { Text(label) },
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(id = R.string.dropdown_description)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        dropdownWidth = coordinates.size.width
                    }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { dropdownWidth.toDp() })
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }) {
                        Text(currency)
                    }
                }
            }
        }
    }
}
