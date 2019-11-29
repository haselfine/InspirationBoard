package com.example.PetBoard;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.PetBoard.db.Pet;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;


public class EditPetFragment extends Fragment implements View.OnClickListener {

    interface SaveChangesListener{
        void saveChanges(Pet pet, Boolean edited); //saves edit/added pet. sends to main, which sends to the list fragment
        void takePicture(View button); //this sends to main which handles the picture taking process
    }

    private static final String TAG = "EDIT FRAGMENT";
    private static final String ARGS_EDIT = "edit argument";

    private SaveChangesListener mSaveChangesListener;

    EditText mEditName;
    EditText mEditDescription;
    EditText mEditTags;
    ImageButton mEditPetImage;
    RatingBar mEditRatingBar;

    Pet mPet;
    Boolean isEditing;
    Button mSaveButton;

    public EditPetFragment() {
        // Required empty public constructor
    }

    public static EditPetFragment newInstance(){ //this one is for adding a pet
        EditPetFragment editPetFragment = new EditPetFragment();
        return editPetFragment;
    }

    public static EditPetFragment newInstance(Pet pet){ //this one is for editing a pet, sends pet in bundle
        final Bundle args = new Bundle();
        args.putParcelable(ARGS_EDIT, pet);
        EditPetFragment editPetFragment = new EditPetFragment();
        editPetFragment.setArguments(args);
        return editPetFragment;
    }

    @Override
    public void onStart(){ //i originally had something in here and removed it. I kept this part just in case I need to add something later
        super.onStart();


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAttach");

        if (context instanceof SaveChangesListener) { //attaches listener
            mSaveChangesListener = (SaveChangesListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SaveChangesListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_edit_pet, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        isEditing = false;

        mEditName = view.findViewById(R.id.name_editText);
        mEditDescription = view.findViewById(R.id.description_editText);
        mEditTags = view.findViewById(R.id.tags_editText);
        mEditRatingBar = view.findViewById(R.id.pet_edit_ratingBar);
        mEditPetImage = view.findViewById(R.id.pet_imageButton);
        mEditPetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveChangesListener.takePicture(mEditPetImage); //sends to main with button id so it can be loaded later
            }
        });

        mSaveButton = view.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(this);

        if(getArguments() != null && getArguments().getParcelable(ARGS_EDIT)!=null){ //as long as arguments/bundle have information...

            isEditing = true;
            mPet = getArguments().getParcelable(ARGS_EDIT);

            setCurrentAttributes(mPet);

        } else {
            Log.d(TAG, "Did not receive pet.");
            isEditing = false;
            mPet = new Pet();
        }

        return view;
    }
    @Override
    public void onClick(View button){
        if(mEditName.toString().equals("") //check to make sure edit texts aren't empty
            || mEditDescription.toString().equals("")
            || mEditTags.toString().equals("")){
            Toast.makeText(EditPetFragment.this.getContext(), "Pet must have a name, description, and tags", Toast.LENGTH_LONG).show();
        }
        mPet.setName(mEditName.getText().toString()); //sets name to what is in the field
        mPet.setDescription(mEditDescription.getText().toString()); //sets description like name
        mPet.setTags(mEditTags.getText().toString()); //same as above
        mPet.setRating(Double.valueOf(mEditRatingBar.getRating())); //reads rating bar. Needs to convert to double because that was
                                                                //the original data type I had and I didn't want to do another migration
        Date date = new Date(); //gets current date/time
        mPet.setDate(date); //sets to variable
        mSaveChangesListener.saveChanges(mPet, isEditing); //sends info to main to send to list to save to list and database
    }

    private void setCurrentAttributes(Pet pet) {
        mEditName.setText(pet.getName()); //if editing, sets the fields to their current attributes
        mEditDescription.setText(pet.getDescription());
        mEditTags.setText(pet.getTags());
        mEditRatingBar.setRating(pet.getRating().floatValue());
        loadImage();
    }

    public void setImageFilePath(String filePath){ //this is used in main to retrieve/set the filepath to the pet that's being edited/added
        mPet.setPhotoPath(filePath);
        loadImage();
    }

    public void loadImage() {

        ImageButton imageButton = mEditPetImage; //use index of image button
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
