package com.example.PetBoard;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.PetBoard.db.Pet;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandedFragment extends Fragment {

    TextView mName;
    TextView mDescription;
    TextView mTags;
    RatingBar mRating;
    ImageButton mImage;
    String mFilePath;

    Pet mPet;

    private PetViewModel mPetViewModel;
    private List<Pet> mPets;

    private static final String TAG = "EXPAND_FRAGMENT";
    private static final String ARGS_EXPAND = "Expand arguments";


    public static ExpandedFragment newInstance(){
        ExpandedFragment fragment = new ExpandedFragment();
        return fragment;
    }

    public static ExpandedFragment newInstance(Pet pet){
        final Bundle args = new Bundle();
        args.putParcelable(ARGS_EXPAND, pet);
        ExpandedFragment fragment = new ExpandedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expanded, container, false);

        mName = view.findViewById(R.id.expand_name_detail_TextView);
        mDescription = view.findViewById(R.id.expand_pet_description_textView);
        mTags = view.findViewById(R.id.expand_tags_detail_textView);
        mRating = view.findViewById(R.id.expand_pet_ratingBar);
        mImage = view.findViewById(R.id.expand_pet_imageButton);

        if(getArguments() != null && getArguments().getParcelable(ARGS_EXPAND)!=null){ //as long as arguments/bundle have information...

            mPet = getArguments().getParcelable(ARGS_EXPAND);

            setPet(mPet);

        } else {
            Log.d(TAG, "Did not receive pet.");
            mPet = new Pet();
        }

        if(mPet == null){
            mPet = defaultPet();
        }
        setPet(mPet);
        return view;
    }

    public Pet defaultPet(){
        Pet pet = new Pet();
        pet.setName("Fluffy");
        pet.setDescription("Very fluffy");
        pet.setRating(3.0);
        pet.setTags("cat, siberian");
        return pet;
    }

    public void setPet(Pet pet){
        this.mPet = pet;
        setAttributes(mPet);
    }

    public void setAttributes(Pet pet) {
        if(pet == null){
            pet = defaultPet();
        }
        mName.setText(getString(R.string.pet_name,  pet.getName()));
        mDescription.setText(getString(R.string.pet_description, pet.getDescription()));
        mTags.setText(getString(R.string.pet_tags, pet.getTags()));
        loadImage();
        mRating.setRating(pet.getRating().floatValue());
    }

    public Pet getCurrentPet(){
        return mPet;
    }

    public void loadImage() {

        ImageButton imageButton = mImage; //use index of image button
        String path = mPet.getPhotoPath(); //retrieve index of image button to find file

        if (path != null && !path.isEmpty()){ //check if empty
            Picasso.get() //use picasso to load file into image button
                    .load(new File(path))
                    .error(android.R.drawable.stat_notify_error)
                    .fit()
                    .centerCrop()
                    .into(imageButton, new Callback(){
                        @Override
                        public void onSuccess(){
                            Log.d(TAG, "Image loaded");
                        }
                        @Override
                        public void onError(Exception e){
                            Log.e(TAG, "error loading image", e);
                        }
                    });
        }
    }
}
