package com.example.PetBoard;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.PetBoard.db.Pet;

import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetDetailFragment extends Fragment {

    TextView mName;
    TextView mDescription;
    TextView mTags;
    RatingBar mRating;
    ImageButton mImage;

    Pet mPet;

    private PetViewModel mPetViewModel;
    private List<Pet> mPets;

    private static final String TAG = "WISH_LIST_FRAGMENT";

    private PetListAdapter petListAdapter;

    public static PetDetailFragment newInstance(){
        PetDetailFragment fragment = new PetDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mPetViewModel = ViewModelProviders.of(this).get(PetViewModel.class);

        final Observer<List<Pet>> petListObserver = new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                Collections.sort(pets);
                PetDetailFragment.this.mPets = pets;
                if(mPets.size() > 0){
                    mPet = mPets.get(0);
                    setAttributes(mPet);
                } else {
                    mPet = new Pet();
                    mPet.setName("Fluffy");
                    mPet.setDescription("Very fluffy");
                    mPet.setRating(3.0);
                    mPet.setTags("cat, siberian");
                }
            }
        };


        mPetViewModel.getAllPets().observe(this, petListObserver);
    }

    @Override
    public void onStart(){
        super.onStart();
        mPetViewModel = ViewModelProviders.of(this).get(PetViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_detail, container, false);
        
        mName = view.findViewById(R.id.name_detail_TextView);
        mDescription = view.findViewById(R.id.pet_description_textView);
        mTags = view.findViewById(R.id.tags_detail_textView);
        mRating = view.findViewById(R.id.pet_ratingBar);
        mImage = view.findViewById(R.id.pet_imageButton);

        if(mPet == null){
            mPet = new Pet();
            mPet.setName("Fluffy");
            mPet.setDescription("Very fluffy");
            mPet.setRating(3.0);
            mPet.setTags("cat, siberian");
        }

        return view;
    }

    public void setPet(Pet pet){
        this.mPet = pet;
        setAttributes(mPet);
    }

    public void setAttributes(Pet pet) {
        mName.setText(getString(R.string.pet_name,  pet.getName()));
        mDescription.setText(getString(R.string.pet_description, pet.getDescription()));
        mTags.setText(getString(R.string.pet_tags, pet.getTags()));
        mRating.setRating(pet.getRating().floatValue());
    }

}
