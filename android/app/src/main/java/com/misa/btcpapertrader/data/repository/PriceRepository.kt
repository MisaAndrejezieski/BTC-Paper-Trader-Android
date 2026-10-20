package com.misa.btcpapertrader.data.repository

import com.misa.btcpapertrader.data.remote.BinanceApi
import com.misa.btcpapertrader.data.remote.CoinGeckoApi
import com.misa.btcpapertrader.domain.model.CotacaoModel
import com.misa.btcpapertrader.domain.model.PricePoint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Repositório responsável por obter a cotação do BTC com fallback (CoinGecko -> Binance -> Cache).
 * Réplica fiel da cascata de busca de fetcher.py.
 */
class PriceRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val coinGeckoApi: CoinGeckoApi = Retrofit.Builder()
        .baseUrl(CoinGeckoApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CoinGeckoApi::class.java)

    private val binanceApi: BinanceApi = Retrofit.Builder()
        .baseUrl(BinanceApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BinanceApi::class.java)

    @Volatile
    private var lastCachedPrice: CotacaoModel? = null

    private fun agoraIsoUtc(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }

    /**
     * Tenta buscar na CoinGecko.
     */
    private suspend fun fetchCoinGecko(): Double? {
        return try {
            val response = coinGeckoApi.getBtcPrice()
            if (response.isSuccessful) {
                val price = response.body()?.bitcoin?.brl
                if (price != null && price > 0 && price.isFinite()) price else null
            } else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Tenta buscar na Binance.
     */
    private suspend fun fetchBinance(): Double? {
        return try {
            val response = binanceApi.getBtcBrlPrice()
            if (response.isSuccessful) {
                val priceStr = response.body()?.price
                val price = priceStr?.toDoubleOrNull()
                if (price != null && price > 0 && price.isFinite()) price else null
            } else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Executa a busca em cascata.
     */
    suspend fun buscarCotacao(): CotacaoModel {
        // 1. CoinGecko
        fetchCoinGecko()?.let { preco ->
            val cotacao = CotacaoModel(
                precoBrl = preco,
                fonte = "CoinGecko",
                timestampIso = agoraIsoUtc(),
                atual = true
            )
            lastCachedPrice = cotacao
            return cotacao
        }

        // 2. Binance
        fetchBinance()?.let { preco ->
            val cotacao = CotacaoModel(
                precoBrl = preco,
                fonte = "Binance BTC/BRL",
                timestampIso = agoraIsoUtc(),
                atual = true
            )
            lastCachedPrice = cotacao
            return cotacao
        }

        // 3. Cache em memória
        lastCachedPrice?.let { cached ->
            return cached.copy(atual = false, fonte = "Cache (${cached.fonte})")
        }

        // Se falhar e não houver cache
        throw IllegalStateException("Não foi possível obter a cotação e nenhum cache está disponível.")
    }

    /**
     * Busca histórico de preços para o gráfico de tendência (CoinGecko Market Chart com fallback/geração).
     */
    suspend fun fetchMarketChart(days: String): List<PricePoint> {
        return try {
            val response = coinGeckoApi.getMarketChart(vsCurrency = "brl", days = days)
            if (response.isSuccessful && response.body()?.prices != null) {
                response.body()?.prices!!.mapNotNull { item ->
                    if (item.size >= 2) {
                        PricePoint(
                            timestamp = item[0].toLong(),
                            priceBrl = item[1]
                        )
                    } else null
                }
            } else {
                generateFallbackHistory(days)
            }
        } catch (e: Exception) {
            generateFallbackHistory(days)
        }
    }

    private fun generateFallbackHistory(days: String): List<PricePoint> {
        val currentPrice = lastCachedPrice?.precoBrl ?: 350000.0
        val count = when (days) {
            "1" -> 24
            "7" -> 28
            "30" -> 30
            "365" -> 52
            else -> 50
        }
        val now = System.currentTimeMillis()
        val intervalMillis = when (days) {
            "1" -> TimeUnit.HOURS.toMillis(1)
            "7" -> TimeUnit.HOURS.toMillis(6)
            "30" -> TimeUnit.DAYS.toMillis(1)
            "365" -> TimeUnit.DAYS.toMillis(7)
            else -> TimeUnit.DAYS.toMillis(30)
        }

        val list = mutableListOf<PricePoint>()
        var p = currentPrice * 0.98
        val random = java.util.Random(42)
        for (i in count downTo 0) {
            val ts = now - (i * intervalMillis)
            val variation = (random.nextDouble() - 0.49) * 0.005
            p *= (1.0 + variation)
            list.add(PricePoint(timestamp = ts, priceBrl = p))
        }
        if (list.isNotEmpty()) {
            list[list.lastIndex] = PricePoint(timestamp = now, priceBrl = currentPrice)
        }
        return list
    }
}
