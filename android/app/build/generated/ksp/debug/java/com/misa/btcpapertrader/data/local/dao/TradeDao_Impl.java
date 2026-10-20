package com.misa.btcpapertrader.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.misa.btcpapertrader.data.local.entity.TradeEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TradeDao_Impl implements TradeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TradeEntity> __insertionAdapterOfTradeEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TradeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTradeEntity = new EntityInsertionAdapter<TradeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trades` (`id`,`timestamp`,`tipo`,`precoBrl`,`quantidadeBtc`,`valorBrl`,`taxaBrl`,`motivo`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TradeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTimestamp());
        statement.bindString(3, entity.getTipo());
        statement.bindDouble(4, entity.getPrecoBrl());
        statement.bindDouble(5, entity.getQuantidadeBtc());
        statement.bindDouble(6, entity.getValorBrl());
        statement.bindDouble(7, entity.getTaxaBrl());
        statement.bindString(8, entity.getMotivo());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trades";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TradeEntity trade, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTradeEntity.insertAndReturnId(trade);
          __db.setTransactionSuccessful();
          return _result;
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
  public Flow<List<TradeEntity>> getAllTradesFlow() {
    final String _sql = "SELECT * FROM trades ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trades"}, new Callable<List<TradeEntity>>() {
      @Override
      @NonNull
      public List<TradeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo");
          final int _cursorIndexOfPrecoBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "precoBrl");
          final int _cursorIndexOfQuantidadeBtc = CursorUtil.getColumnIndexOrThrow(_cursor, "quantidadeBtc");
          final int _cursorIndexOfValorBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "valorBrl");
          final int _cursorIndexOfTaxaBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "taxaBrl");
          final int _cursorIndexOfMotivo = CursorUtil.getColumnIndexOrThrow(_cursor, "motivo");
          final List<TradeEntity> _result = new ArrayList<TradeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TradeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            final String _tmpTipo;
            _tmpTipo = _cursor.getString(_cursorIndexOfTipo);
            final double _tmpPrecoBrl;
            _tmpPrecoBrl = _cursor.getDouble(_cursorIndexOfPrecoBrl);
            final double _tmpQuantidadeBtc;
            _tmpQuantidadeBtc = _cursor.getDouble(_cursorIndexOfQuantidadeBtc);
            final double _tmpValorBrl;
            _tmpValorBrl = _cursor.getDouble(_cursorIndexOfValorBrl);
            final double _tmpTaxaBrl;
            _tmpTaxaBrl = _cursor.getDouble(_cursorIndexOfTaxaBrl);
            final String _tmpMotivo;
            _tmpMotivo = _cursor.getString(_cursorIndexOfMotivo);
            _item = new TradeEntity(_tmpId,_tmpTimestamp,_tmpTipo,_tmpPrecoBrl,_tmpQuantidadeBtc,_tmpValorBrl,_tmpTaxaBrl,_tmpMotivo);
            _result.add(_item);
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
  public Object getAllTradesAsc(final Continuation<? super List<TradeEntity>> $completion) {
    final String _sql = "SELECT * FROM trades ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TradeEntity>>() {
      @Override
      @NonNull
      public List<TradeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo");
          final int _cursorIndexOfPrecoBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "precoBrl");
          final int _cursorIndexOfQuantidadeBtc = CursorUtil.getColumnIndexOrThrow(_cursor, "quantidadeBtc");
          final int _cursorIndexOfValorBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "valorBrl");
          final int _cursorIndexOfTaxaBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "taxaBrl");
          final int _cursorIndexOfMotivo = CursorUtil.getColumnIndexOrThrow(_cursor, "motivo");
          final List<TradeEntity> _result = new ArrayList<TradeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TradeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            final String _tmpTipo;
            _tmpTipo = _cursor.getString(_cursorIndexOfTipo);
            final double _tmpPrecoBrl;
            _tmpPrecoBrl = _cursor.getDouble(_cursorIndexOfPrecoBrl);
            final double _tmpQuantidadeBtc;
            _tmpQuantidadeBtc = _cursor.getDouble(_cursorIndexOfQuantidadeBtc);
            final double _tmpValorBrl;
            _tmpValorBrl = _cursor.getDouble(_cursorIndexOfValorBrl);
            final double _tmpTaxaBrl;
            _tmpTaxaBrl = _cursor.getDouble(_cursorIndexOfTaxaBrl);
            final String _tmpMotivo;
            _tmpMotivo = _cursor.getString(_cursorIndexOfMotivo);
            _item = new TradeEntity(_tmpId,_tmpTimestamp,_tmpTipo,_tmpPrecoBrl,_tmpQuantidadeBtc,_tmpValorBrl,_tmpTaxaBrl,_tmpMotivo);
            _result.add(_item);
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
