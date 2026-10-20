package com.misa.btcpapertrader.data.repository

import com.misa.btcpapertrader.data.remote.BinanceApi
import com.misa.btcpapertrader.data.remote.CoinGeckoApi
import com.misa.btcpapertrader.domain.model.CotacaoModel
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
}
