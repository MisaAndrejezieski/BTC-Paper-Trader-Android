package com.misa.btcpapertrader.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.misa.btcpapertrader.data.local.dao.AppStateDao
import com.misa.btcpapertrader.data.local.dao.CashFlowDao
import com.misa.btcpapertrader.data.local.dao.TradeDao
import com.misa.btcpapertrader.data.local.entity.AppStateEntity
import com.misa.btcpapertrader.data.local.entity.CashFlowEntity
import com.misa.btcpapertrader.data.local.entity.TradeEntity

@Database(
    entities = [TradeEntity::class, CashFlowEntity::class, AppStateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao
    abstract fun cashFlowDao(): CashFlowDao
    abstract fun appStateDao(): AppStateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "btc_paper_trader.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
