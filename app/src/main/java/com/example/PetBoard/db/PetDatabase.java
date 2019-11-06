package com.example.PetBoard.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {Pet.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PetDatabase extends RoomDatabase {

    private static volatile PetDatabase INSTANCE;

    public abstract PetDAO petDAO();

    static PetDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (PetDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PetDatabase.class, "Pet").build();
                }
            }
        }
        return INSTANCE;
    }
}
