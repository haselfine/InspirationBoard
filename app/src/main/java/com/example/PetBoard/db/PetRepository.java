package com.example.PetBoard.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PetRepository {

    private PetDAO petDAO;

    public PetRepository(Application application){
        PetDatabase db = PetDatabase.getDatabase(application);
        petDAO = db.petDAO();
    }

    public LiveData<List<Pet>> getAllPets(){
        return petDAO.getAllPets();
    }

    public LiveData<List<Pet>> getPetByTag(String tags){
        return petDAO.getPetsByTag(tags);
    }

    public void insert(Pet pet){
        new InsertPetAsync(petDAO).execute(pet);
    }

    public void update(Pet pet){
        new UpdatePetAsync(petDAO).execute(pet);
    }

    public void delete(Pet pet){
        new DeletePetAsync(petDAO).execute(pet);
    }

    private static class InsertPetAsync extends AsyncTask<Pet, Void, Void>{
        private PetDAO petDAO;

        InsertPetAsync(PetDAO petDAO){
            this.petDAO = petDAO;
        }

        @Override
        protected Void doInBackground(Pet... pets){
            petDAO.insert(pets);
            return null;
        }
    }

    private static class UpdatePetAsync extends AsyncTask<Pet, Void, Void>{
        private PetDAO petDAO;

        UpdatePetAsync(PetDAO petDAO){
            this.petDAO = petDAO;
        }

        @Override
        protected Void doInBackground(Pet... pets){
            petDAO.update(pets);
            return null;
        }
    }

    private static class DeletePetAsync extends AsyncTask<Pet, Void, Void>{
        private PetDAO petDAO;

        DeletePetAsync(PetDAO petDAO){
            this.petDAO = petDAO;
        }

        @Override
        protected Void doInBackground(Pet... pets){
            petDAO.delete(pets[0]);
            return null;
        }
    }
}
