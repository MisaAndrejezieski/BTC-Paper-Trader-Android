package com.misa.btcpapertrader.data.remote

import com.misa.btcpapertrader.data.remote.model.BinanceTickerResponse
import com.misa.btcpapertrader.data.remote.model.CoinGeckoMarketChartResponse
import com.misa.btcpapertrader.data.remote.model.CoinGeckoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("api/v3/simple/price")
    suspend fun getBtcPrice(
        @Query("ids") ids: String = "bitcoin",
        @Query("vs_currencies") vsCurrencies: String = "brl"
    ): Response<CoinGeckoResponse>

    @GET("api/v3/coins/bitcoin/market_chart")
    suspend fun getMarketChart(
        @Query("vs_currency") vsCurrency: String = "brl",
        @Query("days") days: String
    ): Response<CoinGeckoMarketChartResponse>

    companion object {
        const val BASE_URL = "https://api.coingecko.com/"
    }
}

interface BinanceApi {
    @GET("api/v3/ticker/price")
    suspend fun getBtcBrlPrice(
        @Query("symbol") symbol: String = "BTCBRL"
    ): Response<BinanceTickerResponse>

    companion object {
        const val BASE_URL = "https://api.binance.com/"
    }
}
