package com.example.PetBoard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.PetBoard.db.Pet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PetListFragment extends Fragment implements PetListListener{



    public interface PetClickedListener{
        void petClicked(Pet pet);
    }

    private PetClickedListener mPetClickedListener;

    private EditText mSearchBar;

    private PetViewModel mPetViewModel;
    private List<Pet> mPets;

    private static final String TAG = "LIST_FRAGMENT";

    private Observer<List<Pet>> mPetListObserver;

    private PetListAdapter petListAdapter;

    public PetListFragment() {

    }

    public static PetListFragment newInstance() {
        PetListFragment fragment = new PetListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        displayResults(); //loads from observer/viewmodel
    }

    private void displayResults() {
        mPetViewModel = ViewModelProviders.of(this).get(PetViewModel.class);

        mPetListObserver = new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) { //this is kind of a frankenstein monster from other labs. I have both a local list which is sorted
                                                    //as well as the database... Pretty sure the local isn't necessary, but I haven't checked yet
                Log.d(TAG, "Pets changed: " + pets);
                Collections.sort(pets);
                PetListFragment.this.mPets = pets;
                PetListFragment.this.petListAdapter.setPets(pets);
                PetListFragment.this.petListAdapter.notifyDataSetChanged();
            }
        };

        mPetViewModel.getAllPets().observe(this, mPetListObserver);
    }

    private void displayResults(CharSequence s) { //this is what I came up with when trying to use the search bar. Does not work.

        mPetListObserver.onChanged(mPets);

        mPetViewModel.getPetByTag(s).observe(this, mPetListObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);



        RecyclerView recyclerView = view.findViewById(R.id.pet_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        petListAdapter = new PetListAdapter(this.getContext(), this);
        petListAdapter.setPets(mPets);
        recyclerView.setAdapter(petListAdapter);



        mSearchBar = view.findViewById(R.id.search_EditText);
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) { //this is all I could come up with for my search function
                /*if(s.toString().equals("")){
                    displayResults();
                }
                displayResults(s);*/
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context){
        Log.d(TAG, "onAttach");
        super.onAttach(context);

        if(context instanceof PetClickedListener){ //attach listener
            mPetClickedListener = (PetClickedListener) context;
            Log.d(TAG, "On attach configured listener " + mPetClickedListener);
        } else {
            throw new RuntimeException(context.toString() + " must implement PetSelectedListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mPetViewModel = ViewModelProviders.of(this).get(PetViewModel.class);
    }

    @Override
    public void onListClick(int position){
        Pet pet = mPets.get(position);
        mPetClickedListener.petClicked(pet); //sends pet clicked to main which sends to the detail fragment to display
    }

    @Override
    public void onListLongClick(final int position){ //sends to delete popup
        final Pet pet = mPets.get(position);

        if(getActivity() == null){
            return;
        }
        deletePet(pet);
    }

    public void deletePet(final Pet pet){ //delete pop up
        //I could add the check if the list is empty here to display the default pet, but I don't have time

        AlertDialog confirmDeleteDialog = new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.delete_pet_message, pet.getName()))
                .setTitle(R.string.delete_pet)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPetViewModel.delete(pet); //informs viewmodel of delete
                        petListAdapter.notifyItemRemoved(mPets.indexOf(pet)); //informs adapter of delete
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        confirmDeleteDialog.show();
    }

    public void updateList(Pet pet) { //another frankenstein monster of local lists and database list
        if(mPets.contains(pet)){ //checks local list (to differentiate from an edited item and an added item)
            int petIndex = mPets.indexOf(pet); //index to inform adapter
            mPets.remove(pet); //removes from local list
            mPetViewModel.update(pet); //updates the database entry
            petListAdapter.notifyItemRemoved(petIndex); //deletes pet from recycler view

            mPets.add(pet); //adds the pet back to local list with new information
            petListAdapter.notifyItemInserted(mPets.size()-1); //adds pet back to adapter/recyclerview
        } else { //if we're adding a new pet
            mPets.add(pet); //just add to local list
            mPetViewModel.insert(pet); //add to view model/database
            petListAdapter.notifyItemInserted(mPets.size()-1); //add to recyclerview
        }
    }
}



