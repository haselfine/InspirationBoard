package com.example.PetBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.PetBoard.db.Pet;
import com.example.PetBoard.db.PetRepository;

import java.util.List;

public class PetViewModel extends AndroidViewModel {

    private String mTags;
    private PetRepository mPetRepository;
    private LiveData<List<Pet>> mAllPets;
    private LiveData<List<Pet>> mTaggedPets;

    public PetViewModel(@NonNull Application application){
        super(application);
        mPetRepository = new PetRepository(application);
        mAllPets = mPetRepository.getAllPets();
        mTaggedPets = mPetRepository.getPetByTag(mTags);
    }

    public LiveData<List<Pet>> getAllPets(){
        return mAllPets;
    }

    public LiveData<List<Pet>> getPetByTag(CharSequence s){
        mTags = s.toString();
        return mTaggedPets;
    }

    public void insert(Pet pet){
        mPetRepository.insert(pet);
    }

    public void update(Pet pet){
        mPetRepository.update(pet);
    }

    public void delete(Pet pet){
        mPetRepository.delete(pet);
    }
}
