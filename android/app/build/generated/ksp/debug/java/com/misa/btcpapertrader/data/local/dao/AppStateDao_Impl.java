package com.misa.btcpapertrader.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.misa.btcpapertrader.data.local.entity.AppStateEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppStateDao_Impl implements AppStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppStateEntity> __insertionAdapterOfAppStateEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AppStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppStateEntity = new EntityInsertionAdapter<AppStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_state` (`id`,`capitalBrl`,`capitalBtc`,`precoMedioBrl`,`ultimaCompraTs`,`ultimoPrecoCompraBrl`,`takeProfitExecutado`,`stopLossExecutado`,`totalCompras`,`totalVendas`,`totalTaxasBrl`,`patrimonioInicialBrl`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppStateEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getCapitalBrl());
        statement.bindDouble(3, entity.getCapitalBtc());
        statement.bindDouble(4, entity.getPrecoMedioBrl());
        statement.bindString(5, entity.getUltimaCompraTs());
        statement.bindDouble(6, entity.getUltimoPrecoCompraBrl());
        final int _tmp = entity.getTakeProfitExecutado() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.getStopLossExecutado() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        statement.bindLong(9, entity.getTotalCompras());
        statement.bindLong(10, entity.getTotalVendas());
        statement.bindDouble(11, entity.getTotalTaxasBrl());
        statement.bindDouble(12, entity.getPatrimonioInicialBrl());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM app_state";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdate(final AppStateEntity state,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppStateEntity.insert(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AppStateEntity> getAppStateFlow() {
    final String _sql = "SELECT * FROM app_state WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_state"}, new Callable<AppStateEntity>() {
      @Override
      @Nullable
      public AppStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCapitalBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "capitalBrl");
          final int _cursorIndexOfCapitalBtc = CursorUtil.getColumnIndexOrThrow(_cursor, "capitalBtc");
          final int _cursorIndexOfPrecoMedioBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "precoMedioBrl");
          final int _cursorIndexOfUltimaCompraTs = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaCompraTs");
          final int _cursorIndexOfUltimoPrecoCompraBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimoPrecoCompraBrl");
          final int _cursorIndexOfTakeProfitExecutado = CursorUtil.getColumnIndexOrThrow(_cursor, "takeProfitExecutado");
          final int _cursorIndexOfStopLossExecutado = CursorUtil.getColumnIndexOrThrow(_cursor, "stopLossExecutado");
          final int _cursorIndexOfTotalCompras = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCompras");
          final int _cursorIndexOfTotalVendas = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVendas");
          final int _cursorIndexOfTotalTaxasBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTaxasBrl");
          final int _cursorIndexOfPatrimonioInicialBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "patrimonioInicialBrl");
          final AppStateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpCapitalBrl;
            _tmpCapitalBrl = _cursor.getDouble(_cursorIndexOfCapitalBrl);
            final double _tmpCapitalBtc;
            _tmpCapitalBtc = _cursor.getDouble(_cursorIndexOfCapitalBtc);
            final double _tmpPrecoMedioBrl;
            _tmpPrecoMedioBrl = _cursor.getDouble(_cursorIndexOfPrecoMedioBrl);
            final String _tmpUltimaCompraTs;
            _tmpUltimaCompraTs = _cursor.getString(_cursorIndexOfUltimaCompraTs);
            final double _tmpUltimoPrecoCompraBrl;
            _tmpUltimoPrecoCompraBrl = _cursor.getDouble(_cursorIndexOfUltimoPrecoCompraBrl);
            final boolean _tmpTakeProfitExecutado;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTakeProfitExecutado);
            _tmpTakeProfitExecutado = _tmp != 0;
            final boolean _tmpStopLossExecutado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfStopLossExecutado);
            _tmpStopLossExecutado = _tmp_1 != 0;
            final int _tmpTotalCompras;
            _tmpTotalCompras = _cursor.getInt(_cursorIndexOfTotalCompras);
            final int _tmpTotalVendas;
            _tmpTotalVendas = _cursor.getInt(_cursorIndexOfTotalVendas);
            final double _tmpTotalTaxasBrl;
            _tmpTotalTaxasBrl = _cursor.getDouble(_cursorIndexOfTotalTaxasBrl);
            final double _tmpPatrimonioInicialBrl;
            _tmpPatrimonioInicialBrl = _cursor.getDouble(_cursorIndexOfPatrimonioInicialBrl);
            _result = new AppStateEntity(_tmpId,_tmpCapitalBrl,_tmpCapitalBtc,_tmpPrecoMedioBrl,_tmpUltimaCompraTs,_tmpUltimoPrecoCompraBrl,_tmpTakeProfitExecutado,_tmpStopLossExecutado,_tmpTotalCompras,_tmpTotalVendas,_tmpTotalTaxasBrl,_tmpPatrimonioInicialBrl);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAppStateSync(final Continuation<? super AppStateEntity> $completion) {
    final String _sql = "SELECT * FROM app_state WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AppStateEntity>() {
      @Override
      @Nullable
      public AppStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCapitalBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "capitalBrl");
          final int _cursorIndexOfCapitalBtc = CursorUtil.getColumnIndexOrThrow(_cursor, "capitalBtc");
          final int _cursorIndexOfPrecoMedioBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "precoMedioBrl");
          final int _cursorIndexOfUltimaCompraTs = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaCompraTs");
          final int _cursorIndexOfUltimoPrecoCompraBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimoPrecoCompraBrl");
          final int _cursorIndexOfTakeProfitExecutado = CursorUtil.getColumnIndexOrThrow(_cursor, "takeProfitExecutado");
          final int _cursorIndexOfStopLossExecutado = CursorUtil.getColumnIndexOrThrow(_cursor, "stopLossExecutado");
          final int _cursorIndexOfTotalCompras = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCompras");
          final int _cursorIndexOfTotalVendas = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVendas");
          final int _cursorIndexOfTotalTaxasBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTaxasBrl");
          final int _cursorIndexOfPatrimonioInicialBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "patrimonioInicialBrl");
          final AppStateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpCapitalBrl;
            _tmpCapitalBrl = _cursor.getDouble(_cursorIndexOfCapitalBrl);
            final double _tmpCapitalBtc;
            _tmpCapitalBtc = _cursor.getDouble(_cursorIndexOfCapitalBtc);
            final double _tmpPrecoMedioBrl;
            _tmpPrecoMedioBrl = _cursor.getDouble(_cursorIndexOfPrecoMedioBrl);
            final String _tmpUltimaCompraTs;
            _tmpUltimaCompraTs = _cursor.getString(_cursorIndexOfUltimaCompraTs);
            final double _tmpUltimoPrecoCompraBrl;
            _tmpUltimoPrecoCompraBrl = _cursor.getDouble(_cursorIndexOfUltimoPrecoCompraBrl);
            final boolean _tmpTakeProfitExecutado;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTakeProfitExecutado);
            _tmpTakeProfitExecutado = _tmp != 0;
            final boolean _tmpStopLossExecutado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfStopLossExecutado);
            _tmpStopLossExecutado = _tmp_1 != 0;
            final int _tmpTotalCompras;
            _tmpTotalCompras = _cursor.getInt(_cursorIndexOfTotalCompras);
            final int _tmpTotalVendas;
            _tmpTotalVendas = _cursor.getInt(_cursorIndexOfTotalVendas);
            final double _tmpTotalTaxasBrl;
            _tmpTotalTaxasBrl = _cursor.getDouble(_cursorIndexOfTotalTaxasBrl);
            final double _tmpPatrimonioInicialBrl;
            _tmpPatrimonioInicialBrl = _cursor.getDouble(_cursorIndexOfPatrimonioInicialBrl);
            _result = new AppStateEntity(_tmpId,_tmpCapitalBrl,_tmpCapitalBtc,_tmpPrecoMedioBrl,_tmpUltimaCompraTs,_tmpUltimoPrecoCompraBrl,_tmpTakeProfitExecutado,_tmpStopLossExecutado,_tmpTotalCompras,_tmpTotalVendas,_tmpTotalTaxasBrl,_tmpPatrimonioInicialBrl);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
