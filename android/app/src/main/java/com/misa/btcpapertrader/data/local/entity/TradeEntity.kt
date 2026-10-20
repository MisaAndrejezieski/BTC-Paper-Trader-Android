package com.misa.btcpapertrader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.misa.btcpapertrader.domain.model.TradeModel

/**
 * Tabela de histórico de trades no Room Database.
 */
@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: String,
    val tipo: String,
    val precoBrl: Double,
    val quantidadeBtc: Double,
    val valorBrl: Double,
    val taxaBrl: Double,
    val motivo: String
) {
    fun toDomain() = TradeModel(
        id = id,
        timestamp = timestamp,
        tipo = tipo,
        precoBrl = precoBrl,
        quantidadeBtc = quantidadeBtc,
        valorBrl = valorBrl,
        taxaBrl = taxaBrl,
        motivo = motivo
    )

    companion object {
        fun fromDomain(model: TradeModel) = TradeEntity(
            id = model.id,
            timestamp = model.timestamp,
            tipo = model.tipo,
            precoBrl = model.precoBrl,
            quantidadeBtc = model.quantidadeBtc,
            valorBrl = model.valorBrl,
            taxaBrl = model.taxaBrl,
            motivo = model.motivo
        )
    }
}
