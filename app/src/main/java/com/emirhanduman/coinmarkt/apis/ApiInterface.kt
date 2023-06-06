package com.emirhanduman.coinmarkt.apis

import com.emirhanduman.coinmarkt.models.MarketModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    //"suspend fun" a function that can be paused and resumed at a later time.
    //"Coroutines basics" read the documentation in detail laterw
    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    suspend fun getMarketData() : Response<MarketModel>
}