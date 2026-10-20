package com.misa.btcpapertrader.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.misa.btcpapertrader.data.local.dao.AppStateDao;
import com.misa.btcpapertrader.data.local.dao.AppStateDao_Impl;
import com.misa.btcpapertrader.data.local.dao.CashFlowDao;
import com.misa.btcpapertrader.data.local.dao.CashFlowDao_Impl;
import com.misa.btcpapertrader.data.local.dao.TradeDao;
import com.misa.btcpapertrader.data.local.dao.TradeDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TradeDao _tradeDao;

  private volatile CashFlowDao _cashFlowDao;

  private volatile AppStateDao _appStateDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `trades` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` TEXT NOT NULL, `tipo` TEXT NOT NULL, `precoBrl` REAL NOT NULL, `quantidadeBtc` REAL NOT NULL, `valorBrl` REAL NOT NULL, `taxaBrl` REAL NOT NULL, `motivo` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cash_flow` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` TEXT NOT NULL, `tipo` TEXT NOT NULL, `valorBrl` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_state` (`id` INTEGER NOT NULL, `capitalBrl` REAL NOT NULL, `capitalBtc` REAL NOT NULL, `precoMedioBrl` REAL NOT NULL, `ultimaCompraTs` TEXT NOT NULL, `ultimoPrecoCompraBrl` REAL NOT NULL, `takeProfitExecutado` INTEGER NOT NULL, `stopLossExecutado` INTEGER NOT NULL, `totalCompras` INTEGER NOT NULL, `totalVendas` INTEGER NOT NULL, `totalTaxasBrl` REAL NOT NULL, `patrimonioInicialBrl` REAL NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c7fb50c07ca2d5f2791886bd403b0667')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `trades`");
        db.execSQL("DROP TABLE IF EXISTS `cash_flow`");
        db.execSQL("DROP TABLE IF EXISTS `app_state`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTrades = new HashMap<String, TableInfo.Column>(8);
        _columnsTrades.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("timestamp", new TableInfo.Column("timestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("tipo", new TableInfo.Column("tipo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("precoBrl", new TableInfo.Column("precoBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("quantidadeBtc", new TableInfo.Column("quantidadeBtc", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("valorBrl", new TableInfo.Column("valorBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("taxaBrl", new TableInfo.Column("taxaBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("motivo", new TableInfo.Column("motivo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrades = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrades = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrades = new TableInfo("trades", _columnsTrades, _foreignKeysTrades, _indicesTrades);
        final TableInfo _existingTrades = TableInfo.read(db, "trades");
        if (!_infoTrades.equals(_existingTrades)) {
          return new RoomOpenHelper.ValidationResult(false, "trades(com.misa.btcpapertrader.data.local.entity.TradeEntity).\n"
                  + " Expected:\n" + _infoTrades + "\n"
                  + " Found:\n" + _existingTrades);
        }
        final HashMap<String, TableInfo.Column> _columnsCashFlow = new HashMap<String, TableInfo.Column>(4);
        _columnsCashFlow.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashFlow.put("timestamp", new TableInfo.Column("timestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashFlow.put("tipo", new TableInfo.Column("tipo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCashFlow.put("valorBrl", new TableInfo.Column("valorBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCashFlow = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCashFlow = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCashFlow = new TableInfo("cash_flow", _columnsCashFlow, _foreignKeysCashFlow, _indicesCashFlow);
        final TableInfo _existingCashFlow = TableInfo.read(db, "cash_flow");
        if (!_infoCashFlow.equals(_existingCashFlow)) {
          return new RoomOpenHelper.ValidationResult(false, "cash_flow(com.misa.btcpapertrader.data.local.entity.CashFlowEntity).\n"
                  + " Expected:\n" + _infoCashFlow + "\n"
                  + " Found:\n" + _existingCashFlow);
        }
        final HashMap<String, TableInfo.Column> _columnsAppState = new HashMap<String, TableInfo.Column>(12);
        _columnsAppState.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("capitalBrl", new TableInfo.Column("capitalBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("capitalBtc", new TableInfo.Column("capitalBtc", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("precoMedioBrl", new TableInfo.Column("precoMedioBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("ultimaCompraTs", new TableInfo.Column("ultimaCompraTs", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("ultimoPrecoCompraBrl", new TableInfo.Column("ultimoPrecoCompraBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("takeProfitExecutado", new TableInfo.Column("takeProfitExecutado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("stopLossExecutado", new TableInfo.Column("stopLossExecutado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("totalCompras", new TableInfo.Column("totalCompras", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("totalVendas", new TableInfo.Column("totalVendas", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("totalTaxasBrl", new TableInfo.Column("totalTaxasBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppState.put("patrimonioInicialBrl", new TableInfo.Column("patrimonioInicialBrl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppState = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppState = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppState = new TableInfo("app_state", _columnsAppState, _foreignKeysAppState, _indicesAppState);
        final TableInfo _existingAppState = TableInfo.read(db, "app_state");
        if (!_infoAppState.equals(_existingAppState)) {
          return new RoomOpenHelper.ValidationResult(false, "app_state(com.misa.btcpapertrader.data.local.entity.AppStateEntity).\n"
                  + " Expected:\n" + _infoAppState + "\n"
                  + " Found:\n" + _existingAppState);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c7fb50c07ca2d5f2791886bd403b0667", "eeef6436462b7d627e98aec13060ff75");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "trades","cash_flow","app_state");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `trades`");
      _db.execSQL("DELETE FROM `cash_flow`");
      _db.execSQL("DELETE FROM `app_state`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TradeDao.class, TradeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CashFlowDao.class, CashFlowDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppStateDao.class, AppStateDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TradeDao tradeDao() {
    if (_tradeDao != null) {
      return _tradeDao;
    } else {
      synchronized(this) {
        if(_tradeDao == null) {
          _tradeDao = new TradeDao_Impl(this);
        }
        return _tradeDao;
      }
    }
  }

  @Override
  public CashFlowDao cashFlowDao() {
    if (_cashFlowDao != null) {
      return _cashFlowDao;
    } else {
      synchronized(this) {
        if(_cashFlowDao == null) {
          _cashFlowDao = new CashFlowDao_Impl(this);
        }
        return _cashFlowDao;
      }
    }
  }

  @Override
  public AppStateDao appStateDao() {
    if (_appStateDao != null) {
      return _appStateDao;
    } else {
      synchronized(this) {
        if(_appStateDao == null) {
          _appStateDao = new AppStateDao_Impl(this);
        }
        return _appStateDao;
      }
    }
  }
}
