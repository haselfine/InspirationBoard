package com.example.PetBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.PetBoard.db.Pet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        PetListFragment.PetClickedListener,
        ButtonFragment.ButtonClickedListener,
        EditPetFragment.SaveChangesListener,
        PetDetailFragment.ExpandButtonListener{


    Pet clickedPet; //variable for knowing which pet is selected, pass to different fragments

    private static final String TAG = "MAIN_ACTIVITY";

    private static final String TAG_LIST = "PetListFragment";
    private static final String TAG_BUTTON = "ButtonFragment";
    private static final String TAG_DETAIL = "DetailFragment";
    private static final String TAG_EDIT = "EditFragment";
    private static final String TAG_EXPAND = "ExpandedFragment";

    private String mCurrentImagePath;

    private static int REQUEST_CODE_TAKE_PICTURE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        PetListFragment petListFragment = PetListFragment.newInstance();
        ButtonFragment buttonFragment = ButtonFragment.newInstance();
        PetDetailFragment petDetailFragment = PetDetailFragment.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.pet_list_container, petListFragment, TAG_LIST);
        ft.add(R.id.pet_detail_container, petDetailFragment, TAG_DETAIL);
        ft.add(R.id.button_container, buttonFragment, TAG_BUTTON);

        ft.commit();
    }

    @Override
    public void buttonClicked(View button){
        int buttonID = button.getId();
        boolean edit;
        switch (buttonID){
            case R.id.add_button:
                edit = false; //this variable lets the method know which way to load the edit fragment. In this case, it's adding so edit is false.
                callEditFragment(edit);
                break;
            case R.id.delete_button:
                petToDelete(clickedPet); //sends to list fragment for delete popup

                FragmentManager fm = getSupportFragmentManager();
                PetDetailFragment detailFragment = (PetDetailFragment) fm.findFragmentByTag(TAG_DETAIL);
                Pet pet = detailFragment.defaultPet(); //just in case pet list is empty, sets to a default pet, to avoid seeing format strings
                                                    //there's a better way to do this that checks if the list is empty, but I don't have enough time to write that right now
                detailFragment.setPet(pet); //set to default pet

                break;
            case R.id.edit_button:
                edit = true; //we are editing, so edit is true
                callEditFragment(edit);
                break;
        }
    }

    public void petToDelete(Pet pet){ //sends to list fragment since that's where I wrote it first, and that's where the database connection is
        FragmentManager fm = getSupportFragmentManager();
        PetListFragment petListFragment = (PetListFragment) fm.findFragmentByTag(TAG_LIST);
        try{
            petListFragment.deletePet(pet);
        } catch (NullPointerException npe){ //just in case there's no pet to delete
            Toast.makeText(this, "Pet does not exist", Toast.LENGTH_LONG).show();
        }
    }

    private void callEditFragment(boolean edit) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(edit){ //sent from switch statement. If editing, it creates a new instance with the pet details already there
            EditPetFragment editPetFragment = EditPetFragment.newInstance(clickedPet);
            ft.replace(android.R.id.content, editPetFragment,TAG_EDIT);
        } else { //not editing, fields are blank
            EditPetFragment editPetFragment = EditPetFragment.newInstance();
            ft.replace(android.R.id.content, editPetFragment,TAG_EDIT);
        }

        ft.addToBackStack(TAG_EDIT); //allow back button

        ft.commit();
    }



    @Override
    public void onPetClicked(Pet pet){
        if(pet != null){
            clickedPet = pet; //sends pet from list fragment
            FragmentManager fm = getSupportFragmentManager();
            PetDetailFragment detailFragment = (PetDetailFragment) fm.findFragmentByTag(TAG_DETAIL);//makes that the global variable for other uses
            detailFragment.setPet(pet); //sets the details for that pet in detail fragment
        }
    }

    @Override
    public void saveChanges(Pet pet, Boolean edit){ //on save from edit fragment, sends the pet to list fragment for database storage and to update the list on screen
        FragmentManager fm = getSupportFragmentManager();
        PetListFragment petListFragment = (PetListFragment) fm.findFragmentByTag(TAG_LIST);
        if(pet != null){
            petListFragment.updateList(pet, edit);
        }

        FragmentTransaction ft = fm.beginTransaction();
        EditPetFragment editPetFragment = (EditPetFragment) fm.findFragmentByTag(TAG_EDIT);
        if (editPetFragment != null){
            ft.remove(editPetFragment); //close edit fragment
        }

        ft.commit();


    }

    @Override
    public void expand(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PetDetailFragment petDetailFragment = (PetDetailFragment) fm.findFragmentByTag(TAG_DETAIL);
        if(clickedPet == null){
            Pet pet = petDetailFragment.mPets.get(0);
            ExpandedFragment expandedFragment = ExpandedFragment.newInstance(pet); //shows details for first pet in expanded fragment
            ft.add(android.R.id.content, expandedFragment, TAG_EXPAND);
        } else {
            ExpandedFragment expandedFragment = ExpandedFragment.newInstance(clickedPet); //shows details for currently selected pet in expanded fragment
            ft.add(android.R.id.content, expandedFragment, TAG_EXPAND);
        }



        ft.addToBackStack(TAG_EXPAND);

        ft.commit();
    }

    @Override
    public void takePicture(View button){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //send to image capture intent

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){ //makes sure there's an image
            try{
                File imageFile = createImageFile(); //create file for image
                if(imageFile != null){ //use URI for image, retrieve from intent
                    Uri imageURI = FileProvider.getUriForFile(this, "com.example.PetBoard.fileprovider", imageFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                    startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
                } else {
                    Log.e(TAG, "Image file is null");
                }
            } catch (IOException e){
                Log.e(TAG, "Error creating image file" + e);
            }
        }

    }

    private File createImageFile() throws IOException{
        String imageFilename = "PET_" + new Date().getTime(); //unique file name due to time created
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //file path
        File imageFile = File.createTempFile( //create file name/type/path
                imageFilename,
                ".jpg",
                storageDir
        );

        mCurrentImagePath = imageFile.getAbsolutePath(); //formalize path
        return imageFile;
    }


    private void requestSaveImageToMediaStore(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == //permission already granted
                PackageManager.PERMISSION_GRANTED){
            saveImage(); //permission already granted? Save.

        } else { //Ask for permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            saveImage(); //permission granted? save.
        } else { //let user no image won't be saved
            Toast.makeText(this, "Images will NOT be saved to media store", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(){ //save image to file path
        try{
            MediaStore.Images.Media.insertImage(getContentResolver(), mCurrentImagePath, "Pet", "PetImage");
            try {
                FragmentManager fm = getSupportFragmentManager();

                EditPetFragment editPetFragment = (EditPetFragment) fm.findFragmentByTag(TAG_EDIT);
                editPetFragment.setImageFilePath(mCurrentImagePath);
            } catch (NullPointerException npe){
                Log.d(TAG, "EditPetFragment is null");
            }
        } catch (IOException e){
            Log.e(TAG, "Image file not found", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult for request code " + requestCode +
                    " and current path " + mCurrentImagePath); //retrieve file path, add to list
            requestSaveImageToMediaStore(); //send file path to media store
        } else if (resultCode == RESULT_CANCELED) {
            mCurrentImagePath = "";
        }
    }
}
