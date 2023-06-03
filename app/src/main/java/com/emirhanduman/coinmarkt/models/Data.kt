package com.emirhanduman.coinmarkt.models

data class Data(
    val cryptoCurrencyList: List<CryptoCurrency>,
    val totalCount: String
)