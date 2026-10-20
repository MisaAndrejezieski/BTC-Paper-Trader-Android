package com.misa.btcpapertrader.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.misa.btcpapertrader.data.local.AppDatabase
import com.misa.btcpapertrader.data.repository.PriceRepository
import com.misa.btcpapertrader.data.repository.TradeRepository
import com.misa.btcpapertrader.domain.model.CashFlowModel
import com.misa.btcpapertrader.domain.model.CotacaoModel
import com.misa.btcpapertrader.domain.model.EstadoModel
import com.misa.btcpapertrader.domain.model.TradeModel
import com.misa.btcpapertrader.domain.util.Formatter
import com.misa.btcpapertrader.domain.util.NotificationHelper
import com.misa.btcpapertrader.domain.util.TradingAnalytics
import com.misa.btcpapertrader.domain.util.TradingMetrics
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val tradeRepository = TradeRepository(db)
    private val priceRepository = PriceRepository()

    private val _cotacaoState = MutableStateFlow<CotacaoModel?>(null)
    val cotacaoState: StateFlow<CotacaoModel?> = _cotacaoState.asStateFlow()

    private val _precoHistoryState = MutableStateFlow<List<Double>>(emptyList())
    val precoHistoryState: StateFlow<List<Double>> = _precoHistoryState.asStateFlow()

    private val _estadoState = MutableStateFlow(EstadoModel())
    val estadoState: StateFlow<EstadoModel> = _estadoState.asStateFlow()

    private val _tradesState = MutableStateFlow<List<TradeModel>>(emptyList())
    val tradesState: StateFlow<List<TradeModel>> = _tradesState.asStateFlow()

    private val _cashFlowsState = MutableStateFlow<List<CashFlowModel>>(emptyList())
    val cashFlowsState: StateFlow<List<CashFlowModel>> = _cashFlowsState.asStateFlow()

    private val _metricasState = MutableStateFlow(
        TradingMetrics(null, null, 0.0, 0, 0, 0)
    )
    val metricasState: StateFlow<TradingMetrics> = _metricasState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensagemFeedback = MutableStateFlow<String?>(null)
    val mensagemFeedback: StateFlow<String?> = _mensagemFeedback.asStateFlow()

    private var autoRefreshJob: Job? = null

    init {
        // Observa o estado do banco de dados Room em tempo real
        viewModelScope.launch {
            tradeRepository.estadoFlow.collectLatest { estado ->
                _estadoState.value = estado
            }
        }

        viewModelScope.launch {
            tradeRepository.tradesFlow.collectLatest { trades ->
                _tradesState.value = trades
                _metricasState.value = TradingAnalytics.calcularMetricas(trades)
            }
        }

        viewModelScope.launch {
            tradeRepository.cashFlowsFlow.collectLatest { cashFlows ->
                _cashFlowsState.value = cashFlows
            }
        }

        // Primeira atualização e início do loop de atualização periódica
        atualizarCotacao()
        iniciarLoopCotacao()
    }

    fun limparMensagem() {
        _mensagemFeedback.value = null
    }

    private fun iniciarLoopCotacao() {
        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(30.seconds) // Atualiza a cada 30 segundos
                atualizarCotacao(silencioso = true)
            }
        }
    }

    fun atualizarCotacao(silencioso: Boolean = false) {
        viewModelScope.launch {
            if (!silencioso) _isLoading.value = true
            try {
                val cotacao = priceRepository.buscarCotacao()
                _cotacaoState.value = cotacao

                val currentList = _precoHistoryState.value.toMutableList()
                if (currentList.isEmpty() || currentList.last() != cotacao.precoBrl) {
                    currentList.add(cotacao.precoBrl)
                    if (currentList.size > 50) currentList.removeAt(0)
                    if (currentList.size == 1) {
                        val base = cotacao.precoBrl
                        currentList.clear()
                        currentList.addAll(listOf(base * 0.998, base * 0.999, base * 1.001, base))
                    }
                    _precoHistoryState.value = currentList
                }

                // Avalia e executa estratégia automática se estiver online
                if (cotacao.atual) {
                    val tradeExecutado = tradeRepository.avaliarEstrategia(cotacao.precoBrl)
                    if (tradeExecutado != null) {
                        _mensagemFeedback.value = "Estratégia Automática: Executou ${tradeExecutado.tipo} (${tradeExecutado.motivo})"
                        NotificationHelper.showTradeNotification(getApplication(), tradeExecutado)
                    }
                }
            } catch (e: Exception) {
                if (!silencioso) {
                    _mensagemFeedback.value = "Erro ao buscar cotação: ${e.localizedMessage}"
                }
            } finally {
                if (!silencioso) _isLoading.value = false
            }
        }
    }

    fun depositar(valorBrl: Double) {
        viewModelScope.launch {
            try {
                tradeRepository.depositar(valorBrl)
                _mensagemFeedback.value = "Depósito de ${Formatter.formatarMoeda(valorBrl)} realizado com sucesso!"
            } catch (e: Exception) {
                _mensagemFeedback.value = "Erro no depósito: ${e.localizedMessage}"
            }
        }
    }

    fun sacar(valorBrl: Double) {
        viewModelScope.launch {
            try {
                tradeRepository.sacar(valorBrl)
                _mensagemFeedback.value = "Saque de ${Formatter.formatarMoeda(valorBrl)} realizado com sucesso!"
            } catch (e: Exception) {
                _mensagemFeedback.value = "Erro no saque: ${e.localizedMessage}"
            }
        }
    }

    fun comprarManual(valorBrl: Double) {
        val cotacao = _cotacaoState.value
        if (cotacao == null || !cotacao.atual) {
            _mensagemFeedback.value = "Não é possível comprar sem cotação atualizada."
            return
        }

        viewModelScope.launch {
            try {
                val trade = tradeRepository.comprarManual(cotacao.precoBrl, valorBrl)
                _mensagemFeedback.value = "Compra manual de ${Formatter.formatarBtc(trade.quantidadeBtc)} realizada!"
            } catch (e: Exception) {
                _mensagemFeedback.value = "Erro na compra: ${e.localizedMessage}"
            }
        }
    }

    fun venderManual(quantidadeBtc: Double) {
        val cotacao = _cotacaoState.value
        if (cotacao == null || !cotacao.atual) {
            _mensagemFeedback.value = "Não é possível vender sem cotação atualizada."
            return
        }

        viewModelScope.launch {
            try {
                val trade = tradeRepository.venderManual(cotacao.precoBrl, quantidadeBtc)
                _mensagemFeedback.value = "Venda manual de ${Formatter.formatarBtc(trade.quantidadeBtc)} realizada!"
            } catch (e: Exception) {
                _mensagemFeedback.value = "Erro na venda: ${e.localizedMessage}"
            }
        }
    }

    fun resetarSimulacao() {
        viewModelScope.launch {
            try {
                tradeRepository.resetarSimulacao()
                _mensagemFeedback.value = "Simulação resetada com sucesso."
            } catch (e: Exception) {
                _mensagemFeedback.value = "Erro ao resetar: ${e.localizedMessage}"
            }
        }
    }
}
