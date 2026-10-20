package com.misa.btcpapertrader.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Resposta DTO do CoinGecko: /api/v3/simple/price?ids=bitcoin&vs_currencies=brl
 */
data class CoinGeckoResponse(
    @SerializedName("bitcoin")
    val bitcoin: CoinGeckoBtcData?
)

data class CoinGeckoBtcData(
    @SerializedName("brl")
    val brl: Double?
)

/**
 * Resposta DTO da Binance: /api/v3/ticker/price?symbol=BTCBRL
 */
data class BinanceTickerResponse(
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("price")
    val price: String?
)
