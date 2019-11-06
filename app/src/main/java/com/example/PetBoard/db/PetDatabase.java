package com.example.PetBoard.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Pet.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PetDatabase extends RoomDatabase {

    private static volatile PetDatabase INSTANCE;

    public abstract PetDAO petDAO();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Pet ADD COLUMN date INTEGER");
        }
    };

    static PetDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (PetDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PetDatabase.class, "Pet").addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return INSTANCE;
    }


}
