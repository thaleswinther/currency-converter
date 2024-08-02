package com.example.currencyconverter.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Currency")
data class Currency(
    @PrimaryKey val base: String,
    val rates: Map<String, Double>
)
