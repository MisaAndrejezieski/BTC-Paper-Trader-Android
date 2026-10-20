package com.misa.btcpapertrader.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.misa.btcpapertrader.data.local.entity.CashFlowEntity;
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
public final class CashFlowDao_Impl implements CashFlowDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CashFlowEntity> __insertionAdapterOfCashFlowEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CashFlowDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCashFlowEntity = new EntityInsertionAdapter<CashFlowEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cash_flow` (`id`,`timestamp`,`tipo`,`valorBrl`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CashFlowEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTimestamp());
        statement.bindString(3, entity.getTipo());
        statement.bindDouble(4, entity.getValorBrl());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cash_flow";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CashFlowEntity cashFlow,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCashFlowEntity.insertAndReturnId(cashFlow);
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
  public Flow<List<CashFlowEntity>> getAllCashFlowsFlow() {
    final String _sql = "SELECT * FROM cash_flow ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cash_flow"}, new Callable<List<CashFlowEntity>>() {
      @Override
      @NonNull
      public List<CashFlowEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo");
          final int _cursorIndexOfValorBrl = CursorUtil.getColumnIndexOrThrow(_cursor, "valorBrl");
          final List<CashFlowEntity> _result = new ArrayList<CashFlowEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CashFlowEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            final String _tmpTipo;
            _tmpTipo = _cursor.getString(_cursorIndexOfTipo);
            final double _tmpValorBrl;
            _tmpValorBrl = _cursor.getDouble(_cursorIndexOfValorBrl);
            _item = new CashFlowEntity(_tmpId,_tmpTimestamp,_tmpTipo,_tmpValorBrl);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
