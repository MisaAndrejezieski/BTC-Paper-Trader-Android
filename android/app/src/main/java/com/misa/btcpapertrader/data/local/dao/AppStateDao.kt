package com.misa.btcpapertrader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.misa.btcpapertrader.data.local.entity.AppStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao {
    @Query("SELECT * FROM app_state WHERE id = 1 LIMIT 1")
    fun getAppStateFlow(): Flow<AppStateEntity?>

    @Query("SELECT * FROM app_state WHERE id = 1 LIMIT 1")
    suspend fun getAppStateSync(): AppStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(state: AppStateEntity)

    @Query("DELETE FROM app_state")
    suspend fun deleteAll()
}
