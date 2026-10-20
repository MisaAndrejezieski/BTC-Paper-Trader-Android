package com.misa.btcpapertrader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.misa.btcpapertrader.data.local.entity.CashFlowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashFlowDao {
    @Query("SELECT * FROM cash_flow ORDER BY id DESC")
    fun getAllCashFlowsFlow(): Flow<List<CashFlowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cashFlow: CashFlowEntity): Long

    @Query("DELETE FROM cash_flow")
    suspend fun deleteAll()
}
