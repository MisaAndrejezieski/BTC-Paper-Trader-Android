package com.misa.btcpapertrader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.misa.btcpapertrader.domain.model.CashFlowModel

/**
 * Tabela de fluxo de caixa (depósitos e saques) no Room Database.
 */
@Entity(tableName = "cash_flow")
data class CashFlowEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: String,
    val tipo: String,
    val valorBrl: Double
) {
    fun toDomain() = CashFlowModel(
        id = id,
        timestamp = timestamp,
        tipo = tipo,
        valorBrl = valorBrl
    )

    companion object {
        fun fromDomain(model: CashFlowModel) = CashFlowEntity(
            id = model.id,
            timestamp = model.timestamp,
            tipo = model.tipo,
            valorBrl = model.valorBrl
        )
    }
}
