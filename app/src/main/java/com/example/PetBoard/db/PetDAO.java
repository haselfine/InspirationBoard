package com.example.PetBoard.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PetDAO {

    @Query("SELECT * FROM pet ORDER BY UPPER(name) DESC")
    LiveData<List<Pet>> getAllPets();

    @Query("SELECT * FROM pet WHERE tags LIKE :tags")
    LiveData<List<Pet>> getPetsByTag(String tags);

    @Insert
    void insert(Pet pet);

    @Insert
    void insert(Pet... pets);

    @Update
    void update(Pet pet);

    @Update
    void update(Pet... pets);

    @Delete
    void delete(Pet pet);

    @Query("DELETE FROM pet WHERE id = :id")
    void delete(int id);
}
