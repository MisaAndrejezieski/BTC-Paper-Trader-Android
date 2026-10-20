package com.misa.btcpapertrader.data.repository

import com.misa.btcpapertrader.data.local.AppDatabase
import com.misa.btcpapertrader.data.local.entity.AppStateEntity
import com.misa.btcpapertrader.data.local.entity.CashFlowEntity
import com.misa.btcpapertrader.data.local.entity.TradeEntity
import com.misa.btcpapertrader.domain.model.CashFlowModel
import com.misa.btcpapertrader.domain.model.EstrategiaAcao
import com.misa.btcpapertrader.domain.model.EstadoModel
import com.misa.btcpapertrader.domain.model.TradeModel
import com.misa.btcpapertrader.domain.usecase.TradingEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositório central da aplicação que orquestra persistência no Room Database
 * e execução da lógica de trading do TradingEngine.
 */
class TradeRepository(private val db: AppDatabase) {

    private val appStateDao = db.appStateDao()
    private val tradeDao = db.tradeDao()
    private val cashFlowDao = db.cashFlowDao()

    val estadoFlow: Flow<EstadoModel> = appStateDao.getAppStateFlow().map { entity ->
        entity?.toDomain() ?: EstadoModel()
    }

    val tradesFlow: Flow<List<TradeModel>> = tradeDao.getAllTradesFlow().map { list ->
        list.map { it.toDomain() }
    }

    val cashFlowsFlow: Flow<List<CashFlowModel>> = cashFlowDao.getAllCashFlowsFlow().map { list ->
        list.map { it.toDomain() }
    }

    private suspend fun getEstadoAtual(): EstadoModel {
        return appStateDao.getAppStateSync()?.toDomain() ?: EstadoModel()
    }

    private suspend fun salvarEstado(estado: EstadoModel) {
        appStateDao.insertOrUpdate(AppStateEntity.fromDomain(estado))
    }

    /**
     * Executa um depósito fictício.
     */
    suspend fun depositar(valorBrl: Double): CashFlowModel {
        val estadoAtual = getEstadoAtual()
        val (novoEstado, cashFlow) = TradingEngine.registrarDeposito(estadoAtual, valorBrl)
        salvarEstado(novoEstado)
        cashFlowDao.insert(CashFlowEntity.fromDomain(cashFlow))
        return cashFlow
    }

    /**
     * Executa um saque fictício.
     */
    suspend fun sacar(valorBrl: Double): CashFlowModel {
        val estadoAtual = getEstadoAtual()
        val (novoEstado, cashFlow) = TradingEngine.registrarSaque(estadoAtual, valorBrl)
        salvarEstado(novoEstado)
        cashFlowDao.insert(CashFlowEntity.fromDomain(cashFlow))
        return cashFlow
    }

    /**
     * Executa uma compra manual.
     */
    suspend fun comprarManual(precoBrl: Double, valorBrl: Double): TradeModel {
        val estadoAtual = getEstadoAtual()
        val (novoEstado, trade) = TradingEngine.executarCompra(estadoAtual, precoBrl, valorBrl, motivo = "manual")
        salvarEstado(novoEstado)
        tradeDao.insert(TradeEntity.fromDomain(trade))
        return trade
    }

    /**
     * Executa uma venda manual.
     */
    suspend fun venderManual(precoBrl: Double, quantidadeBtc: Double): TradeModel {
        val estadoAtual = getEstadoAtual()
        val (novoEstado, trade) = TradingEngine.executarVendaQuantidade(estadoAtual, precoBrl, quantidadeBtc, motivo = "manual")
        salvarEstado(novoEstado)
        tradeDao.insert(TradeEntity.fromDomain(trade))
        return trade
    }

    /**
     * Avalia a estratégia automática e executa trade se necessário (Compra extra, Take Profit, Stop Loss).
     */
    suspend fun avaliarEstrategia(precoBrl: Double): TradeModel? {
        val estadoAtual = getEstadoAtual()
        val acao = TradingEngine.decidirAcao(estadoAtual, precoBrl)

        return when (acao) {
            is EstrategiaAcao.Compra -> {
                val (novoEstado, trade) = TradingEngine.executarCompra(
                    estado = estadoAtual,
                    precoBrl = precoBrl,
                    valorBrlDesejado = acao.valorBrl,
                    motivo = acao.motivo
                )
                salvarEstado(novoEstado)
                tradeDao.insert(TradeEntity.fromDomain(trade))
                trade
            }
            is EstrategiaAcao.Venda -> {
                val pair = TradingEngine.executarVendaPercentual(
                    estado = estadoAtual,
                    precoBrl = precoBrl,
                    percentual = acao.percentual,
                    motivo = acao.motivo
                )
                if (pair != null) {
                    val (novoEstado, trade) = pair
                    salvarEstado(novoEstado)
                    tradeDao.insert(TradeEntity.fromDomain(trade))
                    trade
                } else null
            }
            is EstrategiaAcao.Nada -> null
        }
    }

    /**
     * Reseta completamente os dados de simulação.
     */
    suspend fun resetarSimulacao() {
        tradeDao.deleteAll()
        cashFlowDao.deleteAll()
        appStateDao.deleteAll()
    }
}
