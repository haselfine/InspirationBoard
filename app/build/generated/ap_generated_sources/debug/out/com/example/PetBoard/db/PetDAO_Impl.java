package com.example.PetBoard.db;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PetDAO_Impl implements PetDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Pet> __insertionAdapterOfPet;

  private final EntityDeletionOrUpdateAdapter<Pet> __deletionAdapterOfPet;

  private final EntityDeletionOrUpdateAdapter<Pet> __updateAdapterOfPet;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public PetDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPet = new EntityInsertionAdapter<Pet>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Pet` (`id`,`name`,`description`,`tags`,`rating`,`photoPath`,`date`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Pet value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        if (value.getTags() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTags());
        }
        if (value.getRating() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindDouble(5, value.getRating());
        }
        if (value.getPhotoPath() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPhotoPath());
        }
        final long _tmp;
        _tmp = Converters.dateToTimestamp(value.getDate());
        stmt.bindLong(7, _tmp);
      }
    };
    this.__deletionAdapterOfPet = new EntityDeletionOrUpdateAdapter<Pet>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Pet` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Pet value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfPet = new EntityDeletionOrUpdateAdapter<Pet>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Pet` SET `id` = ?,`name` = ?,`description` = ?,`tags` = ?,`rating` = ?,`photoPath` = ?,`date` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Pet value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        if (value.getTags() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTags());
        }
        if (value.getRating() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindDouble(5, value.getRating());
        }
        if (value.getPhotoPath() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPhotoPath());
        }
        final long _tmp;
        _tmp = Converters.dateToTimestamp(value.getDate());
        stmt.bindLong(7, _tmp);
        stmt.bindLong(8, value.getId());
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM pet WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Pet pet) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPet.insert(pet);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final Pet... pets) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPet.insert(pets);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Pet pet) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPet.handle(pet);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Pet pet) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPet.handle(pet);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Pet... pets) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPet.handleMultiple(pets);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Pet>> getAllPets() {
    final String _sql = "SELECT * FROM pet ORDER BY UPPER(name) DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"pet"}, false, new Callable<List<Pet>>() {
      @Override
      public List<Pet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final List<Pet> _result = new ArrayList<Pet>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Pet _item;
            _item = new Pet();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item.setName(_tmpName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item.setDescription(_tmpDescription);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            _item.setTags(_tmpTags);
            final Double _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            }
            _item.setRating(_tmpRating);
            final String _tmpPhotoPath;
            _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            _item.setPhotoPath(_tmpPhotoPath);
            final Date _tmpDate;
            final long _tmp;
            _tmp = _cursor.getLong(_cursorIndexOfDate);
            _tmpDate = Converters.dateFromTimestamp(_tmp);
            _item.setDate(_tmpDate);
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
  public LiveData<List<Pet>> getPetsByTag(final String tags) {
    final String _sql = "SELECT * FROM pet WHERE tags LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tags == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tags);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"pet"}, false, new Callable<List<Pet>>() {
      @Override
      public List<Pet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final List<Pet> _result = new ArrayList<Pet>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Pet _item;
            _item = new Pet();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item.setName(_tmpName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item.setDescription(_tmpDescription);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            _item.setTags(_tmpTags);
            final Double _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            }
            _item.setRating(_tmpRating);
            final String _tmpPhotoPath;
            _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            _item.setPhotoPath(_tmpPhotoPath);
            final Date _tmpDate;
            final long _tmp;
            _tmp = _cursor.getLong(_cursorIndexOfDate);
            _tmpDate = Converters.dateFromTimestamp(_tmp);
            _item.setDate(_tmpDate);
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
}
