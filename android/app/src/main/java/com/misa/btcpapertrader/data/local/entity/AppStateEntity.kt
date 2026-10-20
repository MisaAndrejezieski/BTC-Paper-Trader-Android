package com.misa.btcpapertrader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.misa.btcpapertrader.domain.model.EstadoModel

/**
 * Tabela de estado contábil (linha única com ID=1) no Room Database.
 */
@Entity(tableName = "app_state")
data class AppStateEntity(
    @PrimaryKey val id: Int = 1,
    val capitalBrl: Double,
    val capitalBtc: Double,
    val precoMedioBrl: Double,
    val ultimaCompraTs: String,
    val ultimoPrecoCompraBrl: Double,
    val takeProfitExecutado: Boolean,
    val stopLossExecutado: Boolean,
    val totalCompras: Int,
    val totalVendas: Int,
    val totalTaxasBrl: Double,
    val patrimonioInicialBrl: Double
) {
    fun toDomain() = EstadoModel(
        capitalBrl = capitalBrl,
        capitalBtc = capitalBtc,
        precoMedioBrl = precoMedioBrl,
        ultimaCompraTs = ultimaCompraTs,
        ultimoPrecoCompraBrl = ultimoPrecoCompraBrl,
        takeProfitExecutado = takeProfitExecutado,
        stopLossExecutado = stopLossExecutado,
        totalCompras = totalCompras,
        totalVendas = totalVendas,
        totalTaxasBrl = totalTaxasBrl,
        patrimonioInicialBrl = patrimonioInicialBrl
    )

    companion object {
        fun fromDomain(model: EstadoModel) = AppStateEntity(
            id = 1,
            capitalBrl = model.capitalBrl,
            capitalBtc = model.capitalBtc,
            precoMedioBrl = model.precoMedioBrl,
            ultimaCompraTs = model.ultimaCompraTs,
            ultimoPrecoCompraBrl = model.ultimoPrecoCompraBrl,
            takeProfitExecutado = model.takeProfitExecutado,
            stopLossExecutado = model.stopLossExecutado,
            totalCompras = model.totalCompras,
            totalVendas = model.totalVendas,
            totalTaxasBrl = model.totalTaxasBrl,
            patrimonioInicialBrl = model.patrimonioInicialBrl
        )
    }
}
